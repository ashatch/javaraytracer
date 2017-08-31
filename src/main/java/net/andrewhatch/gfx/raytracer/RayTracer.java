package net.andrewhatch.gfx.raytracer;

import com.google.common.base.Charsets;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;

import net.andrewhatch.gfx.raytracer.documentreaders.SceneParser;
import net.andrewhatch.gfx.raytracer.events.RayTraceFinished;
import net.andrewhatch.gfx.raytracer.events.RayTraceStarted;
import net.andrewhatch.gfx.raytracer.events.RayTracedLine;
import net.andrewhatch.gfx.raytracer.scene.camera.Camera;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.JFrame;


public class RayTracer {
  private static final Logger logger = LoggerFactory.getLogger(RayTracer.class);

  private final Optional<String> sourceFile;
  private final boolean saveImage;

  private SceneParser parser;
  private RayTracerDisplay display;

  int frame = 1;

  private EventBus rayTracingEventBus;

  @Inject
  public RayTracer(final SceneParser parser,
                   final EventBus eventBus,
                   final @Named("sourceFile") Optional<String> sourceFile,
                   final @Named("saveImage") boolean saveImage) {
    this.sourceFile = sourceFile;

    this.parser = parser;
    this.saveImage = saveImage;
    this.rayTracingEventBus = eventBus;
    this.rayTracingEventBus.register(this);
  }

  public void rayTraceFilePath() throws IOException {
    final String filePath = sourceFile.orElseThrow(() ->
        new IllegalArgumentException("Must supply a source file"));

    parser.parse(new String(Files.readAllBytes(Paths.get(filePath)), Charsets.UTF_8));

    final Scene parsed_scene = parser.getScene();
    final Camera camera = parser.getCamera();

    final RayTracerEngine tracer = new RayTracerEngine(rayTracingEventBus, parsed_scene, camera);
    tracer.setSuperSampling(parsed_scene.isSuperSampling());

    display = new RayTracerDisplay(tracer);
    display.setPreferredSize(camera.getViewportSize());

    display.addMessage("Supersampling: " + parsed_scene.isSuperSampling());
    display.addMessage("Scene: " + sourceFile);

    JFrame f = new JFrame();
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    f.add(display);
    f.pack();
    f.setVisible(true);
    tracer.start();
  }

  @Subscribe
  public void traceStarted(final RayTraceStarted evt) {
    logger.info("Ray Tracing started at {}", evt.getNanoTime());
  }

  @Subscribe
  public void traceFinished(final RayTraceFinished evt) {
    logger.info("Ray Tracing finished at {}", evt.getNanoTime());

    if (saveImage) {
      logger.info("Writing frame " + frame + " ... ");
      final BufferedImage img = display.getTracedImage();
      try {
        ImageIO.write(img, "PNG", new File("trace_" + frame + ".png"));
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
