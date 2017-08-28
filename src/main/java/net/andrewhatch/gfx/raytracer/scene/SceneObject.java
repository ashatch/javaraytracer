package net.andrewhatch.gfx.raytracer.scene;

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
