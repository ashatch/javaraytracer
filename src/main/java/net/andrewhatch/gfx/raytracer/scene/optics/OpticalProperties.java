package net.andrewhatch.gfx.raytracer.scene.optics;


public class OpticalProperties {

  public Colour colour;
  public double refractiveness;
  public double transparency;
  public double reflectiveness;
  public double diffusion;
  public boolean luminous;

  public OpticalProperties(
    Colour c,
    double refra,
    double transp,
    double refle,
    double diffu,
    boolean lumin
  ) {
    set(c, refra, transp, refle, diffu, lumin);
  }

  public OpticalProperties() {

  }

  public void setColour(Colour c) {
    this.colour = c;
  }

  public void setValues(double refra, double transp, double refle, double diffu, boolean lumin) {
    this.refractiveness = refra;
    this.transparency = transp;
    this.reflectiveness = refle;
    this.diffusion = diffu;
    this.luminous = lumin;
  }

  public void set(Colour c, double refra, double transp, double refle, double diffu, boolean lumin) {
    this.colour = c;
    this.refractiveness = refra;
    this.transparency = transp;
    this.reflectiveness = refle;
    this.diffusion = diffu;
    this.luminous = lumin;
  }

  public void setRefractiveness(double refractiveness) {
    this.refractiveness = refractiveness;
  }

  public void setTransparency(double transparency) {
    this.transparency = transparency;
  }

  public void setReflectiveness(double reflectiveness) {
    this.reflectiveness = reflectiveness;
  }

  public void setDiffusion(double diffusion) {
    this.diffusion = diffusion;
  }

  public void setLuminous(boolean luminous) {
    this.luminous = luminous;
  }

  public String toString() {
    return "optics[" + colour + "refr:" + refractiveness + ",transp:" + transparency + ",refl:" + reflectiveness + ",dif:" + diffusion + " lumin:" + luminous + "]";
  }
}
