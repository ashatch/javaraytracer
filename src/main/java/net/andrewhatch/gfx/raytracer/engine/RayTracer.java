package net.andrewhatch.gfx.raytracer.engine;

import com.google.common.base.Charsets;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import net.andrewhatch.gfx.raytracer.display.RayTracerDisplay;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;

public class RayTracer {
  private static final Logger logger = LoggerFactory.getLogger(RayTracer.class);

  private final Optional<String> sourceFile;
  private final boolean saveImage;
  private final boolean displayImage;

  private SceneParser parser;

  private EventBus rayTracingEventBus;
  private RayTracerDisplayer rayTracerDisplayer;

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
  }

  public void rayTraceFilePath() throws IOException {
    final RayTracerEngine rayTracerEngine = getTracer();

    final TracedImageProducer imageProducer = new TracedImageProducer(rayTracerEngine);
    rayTracingEventBus.register(imageProducer);

    if (this.displayImage) {
      final RayTracerDisplay display = new RayTracerDisplay(rayTracerEngine, imageProducer);
      this.rayTracerDisplayer = new RayTracerDisplayer(display, saveImage);
      this.rayTracingEventBus.register(rayTracerDisplayer);
    }

    final ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.submit(() -> {
      while (!rayTracerEngine.isFinished()) {
        rayTracerEngine.step();
      }
    });
  }

  public RayTracerEngine getTracer() throws IOException {
    final String filePath = sourceFile.orElseThrow(() ->
        new IllegalArgumentException("Must supply a source file"));

    parser.parse(new String(Files.readAllBytes(Paths.get(filePath)), Charsets.UTF_8));

    final Scene parsed_scene = parser.getScene();
    final Camera camera = parser.getCamera();

    final RayTracerEngine tracer = new RayTracerEngine(rayTracingEventBus, parsed_scene, camera);
    tracer.setSuperSampling(parsed_scene.isSuperSampling());
    return tracer;
  }
}
