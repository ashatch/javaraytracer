package net.andrewhatch.gfx.raytracer.events;

public class RayTraceFinished {
  private final long nanoTime;

  public RayTraceFinished(long nanoTime) {
    this.nanoTime = nanoTime;
  }

  public long getNanoTime() {
    return nanoTime;
  }
}
