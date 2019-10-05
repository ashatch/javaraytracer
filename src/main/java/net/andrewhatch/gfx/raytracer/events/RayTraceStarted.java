package net.andrewhatch.gfx.raytracer.events;

import net.andrewhatch.gfx.raytracer.scene.camera.Camera;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;

public class RayTraceStarted {
  private final long nanoTime;
  private final Scene scene;
  private final Camera camera;

  public RayTraceStarted(
    final Scene scene,
    final Camera camera,
    long nanoTime
  ) {
    this.scene = scene;
    this.camera = camera;
    this.nanoTime = nanoTime;
  }

  public long getNanoTime() {
    return nanoTime;
  }

  public Scene getScene() {
    return scene;
  }

  public Camera getCamera() {
    return camera;
  }
}
