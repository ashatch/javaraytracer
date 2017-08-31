package net.andrewhatch.gfx.raytracer.events;

public class RayTraceStarted {
  private final long nanoTime;

  public RayTraceStarted(long nanoTime) {
    this.nanoTime = nanoTime;
  }

  public long getNanoTime() {
    return nanoTime;
  }
}
