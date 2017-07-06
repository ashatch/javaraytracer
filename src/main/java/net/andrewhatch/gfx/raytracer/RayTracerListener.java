package net.andrewhatch.gfx.raytracer;

public interface RayTracerListener {
  void traceStarted();

  void traceFinished();

  void traceAborted();

  void tracedLine(final int lineCompleted);
}
