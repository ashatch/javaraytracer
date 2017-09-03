package net.andrewhatch.gfx.raytracer.display;

import com.google.common.eventbus.Subscribe;

import net.andrewhatch.gfx.raytracer.engine.RayTracerEngine;
import net.andrewhatch.gfx.raytracer.engine.TracedImageProducer;
import net.andrewhatch.gfx.raytracer.events.RayTraceStarted;

import javax.inject.Inject;
import javax.swing.*;

public class RayTracerDisplayer {

  private final RayTracerEngine rayTracerEngine;
  private final TracedImageProducer imageProducer;

  @Inject
  public RayTracerDisplayer(final RayTracerEngine rayTracerEngine,
                            final TracedImageProducer imageProducer) {
    this.rayTracerEngine = rayTracerEngine;
    this.imageProducer = imageProducer;
  }

  @Subscribe
  public void traceStarted(final RayTraceStarted evt) {
    final RayTracerDisplay display = new RayTracerDisplay(rayTracerEngine, imageProducer);
    display.setPreferredSize(evt.getCamera().getViewportSize());

    display.addMessage("Supersampling: " + evt.getScene().isSuperSampling());

    JFrame f = new JFrame();
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    f.add(display);
    f.pack();
    f.setVisible(true);
  }
}
