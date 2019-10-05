package net.andrewhatch.gfx.raytracer;

import com.google.common.base.Charsets;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import net.andrewhatch.gfx.raytracer.display.RayTracerDisplay;
import net.andrewhatch.gfx.raytracer.display.RayTracerDisplayer;
import net.andrewhatch.gfx.raytracer.documentreaders.AshSceneParser;
import net.andrewhatch.gfx.raytracer.documentreaders.SceneParser;
import net.andrewhatch.gfx.raytracer.engine.RayTracerEngine;
import net.andrewhatch.gfx.raytracer.engine.TracedImageProducer;
import net.andrewhatch.gfx.raytracer.scene.camera.Camera;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import javax.inject.Named;

public class RayTracerModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(SceneParser.class).to(AshSceneParser.class);
  }

  @Provides
  @Singleton
  public EventBus eventBus() {
    return new EventBus();
  }

  @Provides
  @Singleton
  public RayTracerEngine getTracer(
    final @Named("sourceFile") Optional<String> sourceFile,
    final SceneParser parser,
    final EventBus rayTracingEventBus
  ) throws IOException {
    final String filePath = sourceFile.orElseThrow(() ->
      new IllegalArgumentException("Must supply a source file"));

    parser.parse(new String(Files.readAllBytes(Paths.get(filePath)), Charsets.UTF_8));

    final Scene parsed_scene = parser.getScene();
    final Camera camera = parser.getCamera();

    final RayTracerEngine tracer = new RayTracerEngine(
      rayTracingEventBus,
      parsed_scene,
      camera);

    tracer.setSuperSampling(parsed_scene.isSuperSampling());
    return tracer;
  }

  @Provides
  @Singleton
  public RayTracerDisplayer displayer(
    final RayTracerDisplay display,
    final @Named("saveImage") boolean saveImage
  ) {
    return new RayTracerDisplayer(display, saveImage);
  }

  @Provides
  @Singleton
  public TracedImageProducer rayTracedImageProducer(
    final RayTracerEngine engine,
    final EventBus eventBus
  ) {
    final TracedImageProducer tracedImageProducer = new TracedImageProducer(engine);
    eventBus.register(tracedImageProducer);
    return tracedImageProducer;
  }

  @Provides
  @Singleton
  public RayTracerDisplay rayTracerDisplay(
    final RayTracerEngine rayTracerEngine,
    final TracedImageProducer imageProducer
  ) {
    return new RayTracerDisplay(rayTracerEngine, imageProducer);
  }
}
