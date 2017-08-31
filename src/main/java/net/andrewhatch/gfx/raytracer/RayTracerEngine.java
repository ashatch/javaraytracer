package net.andrewhatch.gfx.raytracer;

import com.google.common.eventbus.EventBus;

import net.andrewhatch.gfx.raytracer.events.RayTraceFinished;
import net.andrewhatch.gfx.raytracer.events.RayTraceStarted;
import net.andrewhatch.gfx.raytracer.events.RayTracedLine;
import net.andrewhatch.gfx.raytracer.scene.core.Pixels;
import net.andrewhatch.gfx.raytracer.scene.camera.Camera;
import net.andrewhatch.gfx.raytracer.scene.optics.Colour;
import net.andrewhatch.gfx.raytracer.scene.rays.Ray;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;

import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;


public class RayTracerEngine implements ImageProducer, Runnable {

  private final EventBus rayTracingEventBus;
  public Camera camera;
  private int width;
  private int height;
  private Pixels pixels;
  private int lines_produced;
  private int lines_consumed;
  private Thread thread;
  private ImageConsumer consumer;
  private Scene scene;
  private boolean superSampling = false;
  private boolean finished = true;
  private double percent_complete = 0.0;

  public RayTracerEngine(final EventBus rayTracingEventBus,
                         final Scene scene,
                         final Camera camera) {
    this.rayTracingEventBus = rayTracingEventBus;
    this.scene = scene;
    this.camera = camera;
    this.width = this.camera.getViewportSize().width;
    this.height = this.camera.getViewportSize().height;
  }

  public void setSuperSampling(final boolean flag) {
    this.superSampling = flag;
  }

  public void start() {
    lines_produced = 0;
    lines_consumed = 0;
    finished = false;
    percent_complete = 0.0;
    final int pix[] = new int[width * height];
    pixels = new Pixels(pix, width, height);
    thread = new Thread(this);
    thread.start();
  }

  @Override
  public void addConsumer(final ImageConsumer ic) {
    consumer = ic;
    consumer.setColorModel(ColorModel.getRGBdefault());
    consumer.setDimensions(width, height);
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
    if (lines_produced == height) {
      requestTopDownLeftRightResend(consumer);
    }
  }

  @Override
  public void requestTopDownLeftRightResend(final ImageConsumer ic) {
    ic.setHints(ImageConsumer.TOPDOWNLEFTRIGHT);
    ic.setPixels(0, 0, width, lines_produced, ColorModel.getRGBdefault(), pixels.getPixels(), 0, width);
    lines_consumed = lines_produced;
    if (lines_produced == height) {
      ic.imageComplete(ImageConsumer.STATICIMAGEDONE);
    }
  }


  @Override
  public void run() {
    rayTracingEventBus.post(new RayTraceStarted());

    for (int y = 0; y < height; y++) {
      if (superSampling) {
        traceLineSuperSampling(y);
      } else {
        traceLine(y);
      }

      rayTracingEventBus.post(new RayTracedLine(y));

      synchronized (this) {
        lines_produced++;
        if (consumer != null) {
          // give pixels to the consumer

          consumer.setPixels(
              0,                // starting x
              lines_consumed,          // starting y
              width,              // width
              lines_produced - lines_consumed,// height
              ColorModel.getRGBdefault(),  // color model
              pixels.getPixels(),            // array of imagePixels
              lines_consumed * width,    // offset into array
              width);              // line width

          lines_consumed = lines_produced;

          // Tell the consumer to update the display
          consumer.imageComplete(ImageConsumer.SINGLEFRAMEDONE);
        }
      }


    }
    synchronized (this) {
      if (consumer != null) {
        consumer.imageComplete(ImageConsumer.STATICIMAGEDONE);
      }
    }

    rayTracingEventBus.post(new RayTraceFinished());

    finished = true;
  }

  public boolean isFinished() {
    return finished;
  }

  public double getPercentComplete() {
    this.percent_complete = (double) lines_produced / (double) height * 100.0;
    return this.percent_complete;
  }

  public void traceLine(final int y) {
    for (int x = 0; x < width; x++) {
      final Ray r = camera.generateRay(scene, x, y);
      final Colour shade = new Colour();
      r.fire(shade);
      pixels.getPixels()[(y * width) + x] = shade.getRGB();

    }
  }

  public void traceLineSuperSampling(int y) {
    final int side = 2;
    for (int x = 0; x < width; x++) {
      final Ray[][] r = camera.generateSupersampledRays(scene, x, y, 2, 4.0);
      final Colour[] pixel_c = new Colour[(side * 2 + 1) * (side * 2 + 1)];
      int index = 0;
      for (int i = 0; i < side * 2 + 1; i++) {
        for (int j = 0; j < side * 2 + 1; j++) {
          pixel_c[index] = new Colour();
          r[i][j].fire(pixel_c[index]);
          index++;
        }
      }

      final Colour shade = Colour.getAverageColour(pixel_c);
      pixels.getPixels()[(y * width) + x] = shade.getRGB();
    }
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public void setScene(Scene s) {
    this.scene = s;
  }
}
