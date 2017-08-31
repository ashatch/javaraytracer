package net.andrewhatch.gfx.raytracer.shaders;

import net.andrewhatch.gfx.raytracer.scene.optics.Colour;
import net.andrewhatch.gfx.raytracer.scene.optics.OpticalProperties;
import net.andrewhatch.gfx.raytracer.scene.rays.Ray;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;
import net.andrewhatch.gfx.raytracer.scene.scene.SceneObject;

public abstract class Shader {

  protected OpticalProperties opticalProperties;
  protected Scene scene;
  protected Ray incidentRay;
  protected int depth;

  public Shader(Scene scene, Ray incidentRay, SceneObject obj) {
    this.scene = scene;
    this.opticalProperties = obj.getOpticProperties();
    this.incidentRay = incidentRay;
    this.depth = incidentRay.getDepth();
  }

  public abstract void writeColour(Colour c);

  public int getDepth() {
    return depth;
  }

  public void setDepth(int depth) {
    this.depth = depth;
  }

  public Ray getIncidentRay() {
    return incidentRay;
  }

  public void setIncidentRay(Ray incidentRay) {
    this.incidentRay = incidentRay;
  }

  public OpticalProperties getOpticalProperties() {
    return opticalProperties;
  }

  public void setOpticalProperties(OpticalProperties opticalProperties) {
    this.opticalProperties = opticalProperties;
  }
}
