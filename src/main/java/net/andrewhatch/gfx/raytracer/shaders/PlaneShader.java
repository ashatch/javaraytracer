package net.andrewhatch.gfx.raytracer.shaders;

import net.andrewhatch.gfx.raytracer.scene.geometry.Plane;
import net.andrewhatch.gfx.raytracer.scene.optics.Colour;
import net.andrewhatch.gfx.raytracer.scene.rays.Ray;
import net.andrewhatch.gfx.raytracer.scene.rays.RayHitInfo;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;

public class PlaneShader extends PhongShader {

  protected Colour pixel;

  public PlaneShader(Scene scene, Ray incident_ray, RayHitInfo hit, Plane obj) {
    super(scene, incident_ray, hit, obj);
    this.pixel = obj.getPixel(intersect);
  }

  public void writeColour(Colour c) {
    super.writeColour(c);
    c.attenuate(pixel);
  }

}

