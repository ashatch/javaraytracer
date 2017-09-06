package net.andrewhatch.gfx.raytracer.scene.rays;


import net.andrewhatch.gfx.raytracer.scene.core.Point;
import net.andrewhatch.gfx.raytracer.scene.core.Vector;
import net.andrewhatch.gfx.raytracer.scene.optics.Colour;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;

public class ShadowRay extends Ray {
  public double cosine;
  public double light_brightness;
  private double attenuation;

  public ShadowRay(Scene scene, Point origin, Vector dir, int depth) {
    super(scene, origin, dir, depth);
    this.attenuation = 1.0;
  }

  protected void processHit(final RayHitInfo hit) {
    if (hit.distance < closestHit.distance && hit.distance > Vector.EPSILON) {
      if (hit.object.getOpticProperties().transparency != 0.0) {
        // pass through but record attenuation
        attenuation *= hit.object.getOpticProperties().transparency;
      } else {
        if (hit.distance < closestHit.distance) {
          this.closestHit = hit;
        }
      }
    }
  }

  public void shade(final Colour c) {
    if (closestHit.object != null && closestHit.object.getOpticProperties().luminous) {
      c.set(closestHit.object.getOpticProperties().colour);
    } else {
      c.set(0, 0, 0);
    }
  }

  public double getAttenuation() {
    return attenuation;
  }
}