package net.andrewhatch.gfx.raytracer.scene.lighting;

import net.andrewhatch.gfx.raytracer.scene.core.Point;
import net.andrewhatch.gfx.raytracer.scene.geometry.Sphere;

public class Light extends Sphere {

  private double brightness = 1.0;
  private double relative_brightness = 1.0;

  public Light() {
    super();
  }

  public Light(Point center) {
    super();
    set(center, radius, optic_properties);
  }

  public double getBrightness() {
    return brightness;
  }

  public void setBrightness(double b) {
    this.brightness = b;
  }

  public double getRelativeBrightness() {
    return relative_brightness;
  }

  public void setRelativeBrightness(double b) {
    this.relative_brightness = b;
  }
}
