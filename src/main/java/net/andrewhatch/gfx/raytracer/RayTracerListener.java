package net.andrewhatch.gfx.raytracer;

import java.io.IOException;

public interface RayTracerListener {
  void traceStarted();

  void traceFinished();

  void traceAborted();

  void tracedLine(final int lineCompleted);

  void go(String doc) throws IOException;
}
