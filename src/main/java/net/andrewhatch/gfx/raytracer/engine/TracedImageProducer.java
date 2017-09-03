package net.andrewhatch.gfx.raytracer.engine;

import com.google.common.eventbus.Subscribe;

import net.andrewhatch.gfx.raytracer.events.RayTracedLine;

import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;

public class TracedImageProducer implements ImageProducer {
  private final RayTracerEngine engine;

  private int lines_produced;
  private int lines_consumed;
  private ImageConsumer consumer;

  public TracedImageProducer(final RayTracerEngine engine) {
    this.engine = engine;
  }
  @Override
  public void addConsumer(final ImageConsumer ic) {
    consumer = ic;
    consumer.setColorModel(ColorModel.getRGBdefault());
    consumer.setDimensions(engine.getWidth(), engine.getHeight());
    consumer.setHints(ImageConsumer.COMPLETESCANLINES | ImageConsumer.TOPDOWNLEFTRIGHT);
    consumer = ic;
  }

  @Override
  public boolean isConsumer(final ImageConsumer ic) {
    return consumer == ic;
  }

  @Override
  public void removeConsumer(final ImageConsumer ic) {
    consumer = null;
  }

  @Override
  public void startProduction(final ImageConsumer ic) {
    addConsumer(ic);
    if (lines_produced == engine.getHeight()) {
      requestTopDownLeftRightResend(consumer);
    }
  }

  @Override
  public void requestTopDownLeftRightResend(final ImageConsumer ic) {
    ic.setHints(ImageConsumer.TOPDOWNLEFTRIGHT);
    ic.setPixels(0, 0, engine.getWidth(), lines_produced, ColorModel.getRGBdefault(), engine.getPixels().getPixels(), 0, engine.getWidth());
    lines_consumed = lines_produced;
    if (lines_produced == engine.getHeight()) {
      ic.imageComplete(ImageConsumer.STATICIMAGEDONE);
    }
  }

  @Subscribe
  public void lineRayTraced(final RayTracedLine event) {
    lines_produced++;
    if (consumer != null) {
      // give pixels to the consumer

      consumer.setPixels(
          0,                // starting x
          lines_consumed,          // starting y
          engine.getWidth(),              // width
          lines_produced - lines_consumed,// height
          ColorModel.getRGBdefault(),  // color model
          engine.getPixels().getPixels(),            // array of imagePixels
          lines_consumed * engine.getWidth(),    // offset into array
          engine.getWidth());              // line width

      lines_consumed = lines_produced;

      // Tell the consumer to update the display
      consumer.imageComplete(ImageConsumer.SINGLEFRAMEDONE);
    }
  }
}
