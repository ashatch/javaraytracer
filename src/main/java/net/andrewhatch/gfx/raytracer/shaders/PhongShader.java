package net.andrewhatch.gfx.raytracer.shaders;

import net.andrewhatch.gfx.raytracer.scene.optics.Colour;
import net.andrewhatch.gfx.raytracer.scene.lighting.Light;
import net.andrewhatch.gfx.raytracer.scene.core.Point;
import net.andrewhatch.gfx.raytracer.scene.rays.Ray;
import net.andrewhatch.gfx.raytracer.scene.rays.RayHitInfo;
import net.andrewhatch.gfx.raytracer.scene.rays.RefractedRay;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;
import net.andrewhatch.gfx.raytracer.scene.scene.SceneObject;
import net.andrewhatch.gfx.raytracer.scene.rays.ShadowRay;
import net.andrewhatch.gfx.raytracer.scene.core.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class PhongShader extends Shader {

  // Secondary rays
  private Ray reflected_ray;
  private RefractedRay refracted_ray;

  // geometry
  private Vector normal;
  private List<ShadowRay> shadow_rays = new ArrayList<ShadowRay>();
  private double cosine;

  protected Point intersect;

  public PhongShader(Scene scene, Ray incident_ray, RayHitInfo hit, SceneObject obj) {
    super(scene, incident_ray, obj);
    this.intersect = hit.intersect;
    this.normal = hit.normal;
    this.cosine = normal.dotproduct(incident_ray.getDirection());

    Iterator<Light> i = scene.getLighting().getIterator();
    Light light;
    while (i.hasNext()) {
      // vector pointing towards light
      light = i.next();
      Vector light_v = new Vector(light.getCenter(), intersect);
      light_v.normalise();

      double light_cos = light_v.dotproduct(normal);

      // Shadow ray towards the source of light
      if (optical_properties.diffusion > 0.0 && light_cos > 0.0) {
        ShadowRay shadow_ray = new ShadowRay(scene, intersect, light_v, depth + 1);
        shadow_ray.cosine = light_cos;
        shadow_ray.light_brightness = light.getRelativeBrightness();
        shadow_rays.add(shadow_ray);
      }
    }

    applyReflection(scene);
    applyRefraction(scene);

  }

  private void applyReflection(Scene scene) {
    if (optical_properties.reflectiveness > 0.0) {
      Vector ref = reflect();
      reflected_ray = new Ray(scene, intersect, ref, depth + 1);
    }
  }

  private void applyRefraction(Scene scene) {
    if (optical_properties.transparency > 0.0) {
      Vector ref = refract();
      if (ref.nonzero()) {
        refracted_ray = new RefractedRay(scene, intersect, ref, depth + 1, optical_properties.refractiveness);
      }
    }
  }

  public Vector reflect() {
    Vector result = new Vector(normal);
    result.scale(-2 * cosine);
    result.add(incident_ray.getDirection());
    return result;
  }

  public Vector refract() {
    double refr = 0;
    if (cosine >= 0.0) {
      refr = optical_properties.refractiveness;
    } else if (optical_properties.refractiveness > 0.0) {
      refr = 1.0 / optical_properties.refractiveness;
    }

    double disc_2 = refr * refr * (cosine * cosine - 1) + 1;

    if (disc_2 > 0.0) {
      double discr = Math.sqrt(disc_2);
      // a = - b cos - discr
      double alpha = -refr * cosine;
      if (cosine < 0.0) {
        alpha -= discr;
      } else {
        alpha += discr;
      }

      // r = a n + b inc
      Vector result = new Vector(incident_ray.getDirection());
      result.scale(refr);

      Vector n = new Vector(normal);
      n.scale(alpha);
      result.add(n);
      return result;
    } else // total internal reflection (fail)
    {
      return new Vector(0, 0, 0);
    }
  }

  public void writeColour(Colour c) {
    // Diffused light
    final Colour diffuse = new Colour();
    final Iterator<ShadowRay> i = shadow_rays.iterator();
    ShadowRay ray;
    while (i.hasNext()) {
      ray = i.next();
      final Colour rayDiffuseColour = new Colour();
      ray.fire(rayDiffuseColour);

      rayDiffuseColour.attenuate(ray.attenuation);
      rayDiffuseColour.attenuate(ray.cosine);
      rayDiffuseColour.attenuate(ray.light_brightness);
      diffuse.combineWith(rayDiffuseColour);
    }

    diffuse.attenuate(optical_properties.diffusion);
    diffuse.combineWith(scene.getAmbientColour());
    diffuse.attenuate(optical_properties.colour);
    c.set(diffuse);

    // Reflected light
    if (reflected_ray != null) {
      Colour specular = new Colour();
      reflected_ray.fire(specular);
      specular.attenuate(optical_properties.reflectiveness);
      c.combineWith(specular);
    }

    // Refracted light
    if (refracted_ray != null) {
      Colour refr = new Colour();
      refracted_ray.fire(refr);
      refr.attenuate(optical_properties.transparency);
      c.combineWith(refr);
    }
  }

}