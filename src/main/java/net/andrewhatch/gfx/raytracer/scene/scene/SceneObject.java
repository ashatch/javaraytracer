package net.andrewhatch.gfx.raytracer.scene.scene;

import net.andrewhatch.gfx.raytracer.scene.optics.OpticalProperties;
import net.andrewhatch.gfx.raytracer.scene.core.Point;
import net.andrewhatch.gfx.raytracer.scene.core.Positionable;
import net.andrewhatch.gfx.raytracer.scene.core.Vector;
import net.andrewhatch.gfx.raytracer.scene.rays.Ray;
import net.andrewhatch.gfx.raytracer.scene.rays.RayHitInfo;
import net.andrewhatch.gfx.raytracer.shaders.Shader;

public abstract class SceneObject implements Positionable {

  protected OpticalProperties optic_properties;
  protected Scene scene;

  public SceneObject() {
    super();
  }

  public void setScene(Scene scene) {
    this.scene = scene;
  }

  public abstract RayHitInfo intersect(Ray r);

  public abstract Vector getNormal(Point intersect);

  public abstract Shader createShader(Ray r);

  public OpticalProperties getOpticProperties() {
    return optic_properties;
  }

  public void setOpticProperties(OpticalProperties optic_properties) {
    this.optic_properties = optic_properties;
  }

  public abstract String toString();
}
