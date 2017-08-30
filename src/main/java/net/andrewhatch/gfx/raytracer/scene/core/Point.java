package net.andrewhatch.gfx.raytracer.scene.core;

public class Point {
  private double x;
  private double y;
  private double z;

  public Point(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Point(Point p) {
    this.x = p.x;
    this.y = p.y;
    this.z = p.z;
  }

  public void add(Point p) {
    this.x += p.x;
    this.y += p.y;
    this.z += p.z;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getZ() {
    return z;
  }

  public String toString() {
    return "x:" + x + " y:" + y + " z:" + z;
  }
}
