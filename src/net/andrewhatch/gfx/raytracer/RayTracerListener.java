package net.andrewhatch.gfx.raytracer;

public interface RayTracerListener {
	public void traceStarted();
	public void traceFinished();
	public void traceAborted();
	public void tracedLine(int line_completed);
}
