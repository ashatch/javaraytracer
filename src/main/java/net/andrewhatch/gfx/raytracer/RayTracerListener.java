package net.andrewhatch.gfx.raytracer;

import java.io.IOException;

public interface RayTracerListener {
  void traceStarted();

  void traceFinished();

  void tracedLine(final int lineCompleted);

  void rayTraceFilePath(final String doc) throws IOException;
}
