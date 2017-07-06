package net.andrewhatch.gfx.raytracer.shaders;

import net.andrewhatch.gfx.raytracer.scene.Colour;
import net.andrewhatch.gfx.raytracer.scene.Ray;
import net.andrewhatch.gfx.raytracer.scene.RayHitInfo;
import net.andrewhatch.gfx.raytracer.scene.Scene;
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

