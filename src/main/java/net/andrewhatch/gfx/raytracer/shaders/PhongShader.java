package net.andrewhatch.gfx.raytracer.shaders;

import net.andrewhatch.gfx.raytracer.scene.core.Point;
import net.andrewhatch.gfx.raytracer.scene.core.Vector;
import net.andrewhatch.gfx.raytracer.scene.lighting.Light;
import net.andrewhatch.gfx.raytracer.scene.optics.Colour;
import net.andrewhatch.gfx.raytracer.scene.rays.Ray;
import net.andrewhatch.gfx.raytracer.scene.rays.RayHitInfo;
import net.andrewhatch.gfx.raytracer.scene.rays.RefractedRay;
import net.andrewhatch.gfx.raytracer.scene.rays.ShadowRay;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;
import net.andrewhatch.gfx.raytracer.scene.scene.SceneObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class PhongShader extends Shader {

  protected Point intersect;
  private Ray reflectedRay;
  private RefractedRay refractedRay;
  private Vector normal;
  private List<ShadowRay> shadowRays = new ArrayList<ShadowRay>();
  private double cosine;

  public PhongShader(
    Scene scene,
    Ray incident_ray,
    RayHitInfo hit,
    SceneObject obj
  ) {
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
      if (opticalProperties.diffusion > 0.0 && light_cos > 0.0) {
        ShadowRay shadow_ray = new ShadowRay(scene, intersect, light_v, depth + 1);
        shadow_ray.cosine = light_cos;
        shadow_ray.light_brightness = light.getRelativeBrightness();
        shadowRays.add(shadow_ray);
      }
    }

    applyReflection(scene);
    applyRefraction(scene);

  }

  private void applyReflection(Scene scene) {
    if (opticalProperties.reflectiveness > 0.0) {
      Vector ref = reflect();
      reflectedRay = new Ray(scene, intersect, ref, depth + 1);
    }
  }

  private void applyRefraction(Scene scene) {
    if (opticalProperties.transparency > 0.0) {
      Vector ref = refract();
      if (ref.nonzero()) {
        refractedRay = new RefractedRay(
          scene,
          intersect,
          ref,
          depth + 1,
          opticalProperties.refractiveness);

      }
    }
  }

  public Vector reflect() {
    Vector result = new Vector(normal);
    result.scale(-2 * cosine);
    result.add(incidentRay.getDirection());
    return result;
  }

  public Vector refract() {
    double refr = 0;
    if (cosine >= 0.0) {
      refr = opticalProperties.refractiveness;
    } else if (opticalProperties.refractiveness > 0.0) {
      refr = 1.0 / opticalProperties.refractiveness;
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
      Vector result = new Vector(incidentRay.getDirection());
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

  public void writeColour(final Colour c) {
    final Colour diffuseLight = new Colour();

    shadowRays.stream()
        .forEach(shadowRay -> {
          final Colour rayDiffuseColour = new Colour();
          shadowRay.fire(rayDiffuseColour);

          rayDiffuseColour.attenuate(shadowRay.getAttenuation())
              .attenuate(shadowRay.cosine)
              .attenuate(shadowRay.light_brightness);

          diffuseLight.combineWith(rayDiffuseColour);
        });

    diffuseLight.attenuate(opticalProperties.diffusion)
        .combineWith(scene.getAmbientColour())
        .attenuate(opticalProperties.colour);

    c.set(diffuseLight);

    // Reflected light
    if (reflectedRay != null) {
      final Colour specular = new Colour();
      reflectedRay.fire(specular);
      specular.attenuate(opticalProperties.reflectiveness);
      c.combineWith(specular);
    }

    // Refracted light
    if (refractedRay != null) {
      Colour refractedColour = new Colour();
      refractedRay.fire(refractedColour);
      refractedColour.attenuate(opticalProperties.transparency);
      c.combineWith(refractedColour);
    }
  }

}
