package net.andrewhatch.gfx.raytracer.scene.rays;


import net.andrewhatch.gfx.raytracer.scene.core.Point;
import net.andrewhatch.gfx.raytracer.scene.core.Vector;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;

public class RefractedRay extends Ray {

  protected double refr;

  public RefractedRay(Scene scene, Point origin, Vector direction, int depth, double refr) {
    super(scene, origin, direction, depth);
    this.refr = refr;
  }
}
