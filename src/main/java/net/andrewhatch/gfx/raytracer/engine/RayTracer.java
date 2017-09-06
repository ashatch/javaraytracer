package net.andrewhatch.gfx.raytracer.engine;

import com.google.common.eventbus.EventBus;

import net.andrewhatch.gfx.raytracer.display.RayTracerDisplayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Named;

public class RayTracer {
  private static final Logger logger = LoggerFactory.getLogger(RayTracer.class);


  private final boolean displayImage;
  private final RayTracerEngine rayTracerEngine;
  private EventBus rayTracingEventBus;
  private RayTracerDisplayer rayTracerDisplayer;

  @Inject
  public RayTracer(final RayTracerEngine rayTracerEngine,
                   final RayTracerDisplayer displayer,
                   final EventBus eventBus,
                   final @Named("displayImage") boolean displayImage) {
    this.rayTracerEngine = rayTracerEngine;
    this.rayTracerDisplayer = displayer;
    this.displayImage = displayImage;
    this.rayTracingEventBus = eventBus;

  }

  public void rayTraceFilePath() throws IOException {
    if (this.displayImage) {
      this.rayTracingEventBus.register(rayTracerDisplayer);
    }

    final ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.submit(() -> {
      while (!rayTracerEngine.isFinished()) {
        rayTracerEngine.step();
      }
    });
  }
}
