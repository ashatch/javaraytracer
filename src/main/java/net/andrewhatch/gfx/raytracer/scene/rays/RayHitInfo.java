package net.andrewhatch.gfx.raytracer.scene.rays;

import net.andrewhatch.gfx.raytracer.scene.core.Point;
import net.andrewhatch.gfx.raytracer.scene.core.Vector;
import net.andrewhatch.gfx.raytracer.scene.scene.SceneObject;

public class RayHitInfo {
  public SceneObject object;
  public Point intersect;
  public Vector normal;
  public double distance;
}
