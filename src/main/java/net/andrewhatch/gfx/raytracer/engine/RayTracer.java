package net.andrewhatch.gfx.raytracer.engine;

import com.google.common.base.Charsets;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import net.andrewhatch.gfx.raytracer.display.RayTracerDisplayer;
import net.andrewhatch.gfx.raytracer.documentreaders.SceneParser;
import net.andrewhatch.gfx.raytracer.events.RayTraceFinished;
import net.andrewhatch.gfx.raytracer.events.RayTraceStarted;
import net.andrewhatch.gfx.raytracer.events.RayTracedLine;
import net.andrewhatch.gfx.raytracer.scene.camera.Camera;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;

public class RayTracer {
  private static final Logger logger = LoggerFactory.getLogger(RayTracer.class);

  private final Optional<String> sourceFile;
  private final boolean saveImage;
  private final boolean displayImage;

  private SceneParser parser;

  int frame = 1;

  private EventBus rayTracingEventBus;
  private BufferedImage renderedImage;
  private Image actualRenderedImage;

  @Inject
  public RayTracer(final SceneParser parser,
                   final EventBus eventBus,
                   final @Named("sourceFile") Optional<String> sourceFile,
                   final @Named("saveImage") boolean saveImage,
                   final @Named("displayImage") boolean displayImage) {
    this.sourceFile = sourceFile;

    this.parser = parser;
    this.saveImage = saveImage;
    this.displayImage = displayImage;
    this.rayTracingEventBus = eventBus;
    this.rayTracingEventBus.register(this);
  }

  public void rayTraceFilePath() throws IOException {
    final String filePath = sourceFile.orElseThrow(() ->
        new IllegalArgumentException("Must supply a source file"));

    parser.parse(new String(Files.readAllBytes(Paths.get(filePath)), Charsets.UTF_8));

    final Scene parsed_scene = parser.getScene();
    final Camera camera = parser.getCamera();

    this.renderedImage = new BufferedImage(camera.getViewportSize().width,
        camera.getViewportSize().height,
        BufferedImage.TYPE_INT_RGB);

    final RayTracerEngine tracer = new RayTracerEngine(rayTracingEventBus, parsed_scene, camera);
    tracer.setSuperSampling(parsed_scene.isSuperSampling());

    this.actualRenderedImage = Toolkit.getDefaultToolkit().createImage(tracer);

    if (this.displayImage) {
      this.rayTracingEventBus.register(new RayTracerDisplayer(tracer));
    }

    final Thread thread = new Thread(tracer);
    thread.start();
  }

  @Subscribe
  public void traceStarted(final RayTraceStarted evt) {
    logger.info("Ray Tracing started at {}", evt.getNanoTime());
  }

  @Subscribe
  public void traceFinished(final RayTraceFinished evt) {
    logger.info("Ray Tracing finished at {}", evt.getNanoTime());

    if (saveImage) {
      renderedImage.getGraphics().drawImage(actualRenderedImage, 0, 0, null);

      logger.info("Writing frame " + frame + " ... ");
      try {
        ImageIO.write(renderedImage, "PNG", new File("trace_" + frame + ".png"));
      } catch (IOException e) {
        logger.error("Problem writing image", e);
      }
      logger.info("Done.");
    }
  }

  @Subscribe
  public void tracedLine(final RayTracedLine event) {
    logger.trace("Traced line {}", event.getLineNumber());
  }
}
