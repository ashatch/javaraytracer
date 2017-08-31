package net.andrewhatch.gfx.raytracer.events;

public class RayTracedLine {
  private final int lineNumber;

  public RayTracedLine(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  public int getLineNumber() {
    return lineNumber;
  }
}
