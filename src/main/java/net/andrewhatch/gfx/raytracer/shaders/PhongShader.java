package net.andrewhatch.gfx.raytracer.shaders;

import net.andrewhatch.gfx.raytracer.scene.Colour;
import net.andrewhatch.gfx.raytracer.scene.Light;
import net.andrewhatch.gfx.raytracer.scene.Point;
import net.andrewhatch.gfx.raytracer.scene.Ray;
import net.andrewhatch.gfx.raytracer.scene.RayHitInfo;
import net.andrewhatch.gfx.raytracer.scene.RefractedRay;
import net.andrewhatch.gfx.raytracer.scene.Scene;
import net.andrewhatch.gfx.raytracer.scene.SceneObject;
import net.andrewhatch.gfx.raytracer.scene.ShadowRay;
import net.andrewhatch.gfx.raytracer.scene.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class PhongShader extends Shader {

  // Secondary rays
  Ray reflected_ray;
  RefractedRay refracted_ray;

  // geometry
  Point intersect;
  Vector normal;
  List<ShadowRay> shadow_rays = new ArrayList<ShadowRay>();
  double cosine;

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


    // Reflection
    if (optical_properties.reflectiveness > 0.0) {
      Vector ref = reflect();
      reflected_ray = new Ray(scene, intersect, ref, depth + 1);
    }

    // Refraction
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

  public void getColour(Colour c) {
    // Diffused light
    Colour diffuse = new Colour();
    Iterator<ShadowRay> i = shadow_rays.iterator();
    ShadowRay ray;
    while (i.hasNext()) {
      ray = i.next();
      Colour raydiff = new Colour();
      ray.fire(raydiff);

      raydiff.attenuate(ray.attenuation);
      raydiff.attenuate(ray.cosine);
      raydiff.attenuate(ray.light_brightness);
      diffuse.combineWith(raydiff);
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