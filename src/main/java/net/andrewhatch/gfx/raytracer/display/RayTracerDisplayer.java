package net.andrewhatch.gfx.raytracer.display;

import com.google.common.eventbus.Subscribe;

import net.andrewhatch.gfx.raytracer.engine.RayTracerEngine;
import net.andrewhatch.gfx.raytracer.engine.TracedImageProducer;
import net.andrewhatch.gfx.raytracer.events.RayTraceFinished;
import net.andrewhatch.gfx.raytracer.events.RayTraceStarted;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.swing.*;

public class RayTracerDisplayer {
  private static final Logger logger = LoggerFactory.getLogger(RayTracerDisplayer.class);

  private final RayTracerEngine rayTracerEngine;
  private final TracedImageProducer imageProducer;
  private final boolean saveImage;
  private RayTracerDisplay display;

  @Inject
  public RayTracerDisplayer(final RayTracerEngine rayTracerEngine,
                            final TracedImageProducer imageProducer,
                            final boolean saveImage) {
    this.rayTracerEngine = rayTracerEngine;
    this.imageProducer = imageProducer;
    this.saveImage = saveImage;
  }

  @Subscribe
  public void traceStarted(final RayTraceStarted evt) {
    EventQueue.invokeLater(() -> {
      display = new RayTracerDisplay(rayTracerEngine, imageProducer);
      display.setPreferredSize(evt.getCamera().getViewportSize());

      display.addMessage("Supersampling: " + evt.getScene().isSuperSampling());

      JFrame f = new JFrame();
      f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      f.add(display);
      f.pack();
      f.setVisible(true);
    });
  }

  @Subscribe
  public void traceFinished(final RayTraceFinished evt) {
    logger.info("Ray Tracing finished at {}", evt.getNanoTime());

    if (saveImage) {
      logger.info("Requesting image save");
      EventQueue.invokeLater(() -> {
        try {
          ImageIO.write(display.getRayTracedImage(), "PNG", new File("trace.png"));
          logger.info("Image saved.");
        } catch (IOException e) {
          logger.error("Problem writing image", e);
        }
      });
    }
  }
}
