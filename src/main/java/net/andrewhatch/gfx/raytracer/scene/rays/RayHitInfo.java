package net.andrewhatch.gfx.raytracer.scene.rays;

import net.andrewhatch.gfx.raytracer.scene.core.Point;
import net.andrewhatch.gfx.raytracer.scene.scene.SceneObject;
import net.andrewhatch.gfx.raytracer.scene.core.Vector;

public class RayHitInfo {
  public SceneObject object;
  public Point intersect;
  //public Point point;
  public Vector normal;
  public double distance;
}
