package net.andrewhatch.gfx.raytracer.display;

import com.google.common.eventbus.Subscribe;

import net.andrewhatch.gfx.raytracer.events.RayTraceFinished;
import net.andrewhatch.gfx.raytracer.events.RayTraceStarted;
import net.andrewhatch.gfx.raytracer.events.RayTracedLine;

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

  private final boolean saveImage;
  private RayTracerDisplay display;

  @Inject
  public RayTracerDisplayer(final RayTracerDisplay rayTracerDisplay,
                            final boolean saveImage) {
    this.saveImage = saveImage;
    this.display = rayTracerDisplay;
  }

  @Subscribe
  public void traceStarted(final RayTraceStarted evt) {
    logger.trace("Ray trace started at {}", evt.getNanoTime());
    EventQueue.invokeLater(() -> {

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
  public void traceLine(final RayTracedLine event) {
    logger.trace("Line {}", event.getLineNumber());
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
