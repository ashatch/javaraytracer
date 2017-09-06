package net.andrewhatch.gfx.raytracer.engine;

import com.google.common.eventbus.EventBus;

import net.andrewhatch.gfx.raytracer.events.RayTraceFinished;
import net.andrewhatch.gfx.raytracer.events.RayTraceStarted;
import net.andrewhatch.gfx.raytracer.events.RayTracedLine;
import net.andrewhatch.gfx.raytracer.scene.camera.Camera;
import net.andrewhatch.gfx.raytracer.scene.core.Pixels;
import net.andrewhatch.gfx.raytracer.scene.optics.Colour;
import net.andrewhatch.gfx.raytracer.scene.rays.Ray;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RayTracerEngine {

  private static final Logger logger = LoggerFactory.getLogger(RayTracerEngine.class);

  private final EventBus rayTracingEventBus;
  public Camera camera;
  private int width;
  private int height;
  private Pixels pixels;
  private Scene scene;
  private boolean superSampling = false;
  private boolean started = false;
  private boolean finished = true;
  private double percent_complete = 0.0;
  private int currentLine = 0;

  public RayTracerEngine(final EventBus rayTracingEventBus,
                         final Scene scene,
                         final Camera camera) {
    this.rayTracingEventBus = rayTracingEventBus;
    this.scene = scene;
    this.camera = camera;
    this.width = this.camera.getViewportSize().width;
    this.height = this.camera.getViewportSize().height;
    finished = false;
    percent_complete = 0.0;
    final int pix[] = new int[width * height];
    pixels = new Pixels(pix, width, height);
  }

  public void setSuperSampling(final boolean flag) {
    this.superSampling = flag;
  }

  void step() {
    if (finished) {
      return;
    }
    if (!started) {
      startRender();
    }

    scanNextLine();
  }

  private void startRender() {
    logger.info("Render started");
    notifyRayTraceStarted();
    this.started = true;
  }

  private void scanNextLine() {

    if (currentLine == height) {
      finished = true;
      logger.info("Render Finished");
      notifyRayTraceFinished();
      return;
    }

    if (superSampling) {
      traceLineSuperSampling(currentLine);
    } else {
      traceLine(currentLine);
    }

    notifyRayTracedALine(currentLine);

    currentLine++;
  }

  private void notifyRayTraceStarted() {
    rayTracingEventBus.post(new RayTraceStarted(this.scene, this.camera, System.nanoTime()));
  }

  private void notifyRayTracedALine(final int line) {
    rayTracingEventBus.post(new RayTracedLine(line));
  }

  private void notifyRayTraceFinished() {
    rayTracingEventBus.post(new RayTraceFinished(System.nanoTime()));
  }

  public boolean isFinished() {
    return finished;
  }

  public double getPercentComplete() {
    this.percent_complete = (double) this.currentLine / (double) height * 100.0;
    return this.percent_complete;
  }

  private void traceLine(final int y) {
    for (int x = 0; x < width; x++) {
      final Ray r = camera.generateRay(scene, x, y);
      final Colour shade = new Colour();
      r.fire(shade);
      pixels.getPixels()[(y * width) + x] = shade.getRGB();
    }
  }

  private void traceLineSuperSampling(int y) {
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

  Pixels getPixels() {
    return this.pixels;
  }
}
