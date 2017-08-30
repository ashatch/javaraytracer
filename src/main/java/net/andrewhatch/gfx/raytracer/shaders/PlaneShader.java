package net.andrewhatch.gfx.raytracer.shaders;

import net.andrewhatch.gfx.raytracer.scene.optics.Colour;
import net.andrewhatch.gfx.raytracer.scene.rays.Ray;
import net.andrewhatch.gfx.raytracer.scene.rays.RayHitInfo;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;
import net.andrewhatch.gfx.raytracer.scene.geometry.Plane;

public class PlaneShader extends PhongShader {

  protected Colour pixel;

  public PlaneShader(Scene scene, Ray incident_ray, RayHitInfo hit, Plane obj) {
    super(scene, incident_ray, hit, obj);
    this.pixel = obj.getPixel(intersect);
  }

  public void getColour(Colour c) {
    super.getColour(c);
    c.attenuate(pixel);
  }

}

