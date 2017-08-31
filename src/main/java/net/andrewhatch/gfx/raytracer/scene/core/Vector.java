package net.andrewhatch.gfx.raytracer.scene.core;


public class Vector {

  public static final double EPSILON = 0.00001;

  public double x;
  public double y;
  public double z;

  public Vector() {
    set(0.0, 0.0, 0.0);
  }

  public Vector(double x, double y, double z) {
    set(x, y, z);
  }

  public Vector(Vector v) {
    set(v.x, v.y, v.z);
  }

  public Vector(Point p, Point origin) {
    set(p.getX() - origin.getX(), p.getY() - origin.getY(), p.getZ() - origin.getZ());
  }

  public Vector set(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
    return this;
  }

  public double length() {
    return Math.sqrt((x * x) + (y * y) + (z * z));
  }

  public Vector add(Vector v) {
    this.x += v.x;
    this.y += v.y;
    this.z += v.z;
    return this;
  }

  public Vector scale(double a) {
    this.x *= a;
    this.y *= a;
    this.z *= a;
    return this;
  }

  public Vector multiplyNew(double a) {
    return new Vector(x * a, y * a, z * a);
  }

  public Vector addNew(Vector v) {
    return new Vector(x + v.x, y + v.y, z + v.z);
  }

  public Vector subtractNew(Vector v) {
    return new Vector(x - v.x, y - v.y, z - v.z);
  }

  public Vector subtract(Vector v) {
    this.x -= v.x;
    this.y -= v.y;
    this.z -= v.z;
    return this;
  }

  public Vector unaryMinus() {
    this.x = -this.x;
    this.y = -this.y;
    this.z = -this.z;
    return this;
  }

  public double dotproduct(Vector v) {
    return (v.x * x) + (v.y * y) + (v.z * z);
  }

  public Vector crossProduct(Vector v) {
    double xh = y * v.z - v.y * z;
    double yh = z * v.x - v.z * x;
    double zh = x * v.y - v.x * y;
    x = xh;
    y = yh;
    z = zh;
    return this;
  }

  public Vector crossProductNew(Vector v) {
    double xh = y * v.z - v.y * z;
    double yh = z * v.x - v.z * x;
    double zh = x * v.y - v.x * y;
    return new Vector(xh, yh, zh);
  }

  public Vector normalise() {
    double f = 1.0d / length();
    if (f > EPSILON) {
      scale(f);
    } else {
      set(0.0, 0.0, 0.0);
    }
    return this;
  }

  public boolean nonzero() {
    return this.x != 0 || this.y != 0 || this.z != 0;
  }

  public Point toPoint() {
    return new Point(x, y, z);
  }

  public String toString() {
    return toPoint().toString();
  }
}
