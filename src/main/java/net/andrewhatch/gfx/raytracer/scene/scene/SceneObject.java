package net.andrewhatch.gfx.raytracer.scene.scene;

import net.andrewhatch.gfx.raytracer.scene.core.Positionable;
import net.andrewhatch.gfx.raytracer.scene.optics.OpticalProperties;
import net.andrewhatch.gfx.raytracer.scene.rays.Ray;
import net.andrewhatch.gfx.raytracer.scene.rays.RayHitInfo;
import net.andrewhatch.gfx.raytracer.shaders.Shader;

public abstract class SceneObject implements Positionable {

  protected OpticalProperties opticProperties;
  protected Scene scene;

  public SceneObject() {
    super();
  }

  public void setScene(Scene scene) {
    this.scene = scene;
  }

  public abstract RayHitInfo intersect(Ray r);

  public abstract Shader createShader(Ray r);

  public OpticalProperties getOpticProperties() {
    return opticProperties;
  }

  public void setOpticProperties(OpticalProperties optic_properties) {
    this.opticProperties = optic_properties;
  }

  public abstract String toString();
}
