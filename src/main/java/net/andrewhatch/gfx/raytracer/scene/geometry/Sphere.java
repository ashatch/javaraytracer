package net.andrewhatch.gfx.raytracer.scene.geometry;

import net.andrewhatch.gfx.raytracer.scene.OpticalProperties;
import net.andrewhatch.gfx.raytracer.scene.Point;
import net.andrewhatch.gfx.raytracer.scene.Ray;
import net.andrewhatch.gfx.raytracer.scene.RayHitInfo;
import net.andrewhatch.gfx.raytracer.scene.SceneObject;
import net.andrewhatch.gfx.raytracer.scene.Vector;
import net.andrewhatch.gfx.raytracer.shaders.PhongShader;
import net.andrewhatch.gfx.raytracer.shaders.Shader;

public class Sphere extends SceneObject {

  protected Point center;
  protected double radius;
  protected double radius_squared;
  protected Vector normal;
  protected RayHitInfo hit;

  public Sphere() {
    super();
  }

  public Sphere(Point center, double radius, OpticalProperties optics) {
    super();
    set(center, radius, optics);
  }

  public void setRadius(double radius) {
    this.radius = radius;
    this.radius_squared = radius * radius;
  }

  public void set(Point center, double radius, OpticalProperties optics) {
    this.center = center;
    setRadius(radius);
    this.optic_properties = optics;
    this.radius_squared = radius * radius;
  }

  public Point getCenter() {
    return center;
  }

  public void setCenter(Point center) {
    this.center = center;
  }

  public RayHitInfo intersect(Ray r) {
    Vector oc = new Vector(center, r.origin);
    double oc2 = oc.dotproduct(oc);
    double dist2 = oc2 - radius_squared;

    if (dist2 <= Vector.EPSILON && dist2 >= -Vector.EPSILON) {
      return null;
    }

    double tca = oc.dotproduct(r.getDirection());

    if (dist2 > Vector.EPSILON && tca < 0.0) {
      return null;
    }

    double hc2 = radius_squared - oc2 + tca * tca;

    if (hc2 < 0.0) {
      return null;
    }

    hit = new RayHitInfo();
    hit.object = this;


    if (dist2 > Vector.EPSILON) {
      hit.distance = tca - Math.sqrt(hc2);
    } else {
      hit.distance = tca + Math.sqrt(hc2);
    }

    Vector tmp = new Vector(r.getDirection());
    tmp.scale(hit.distance);
    Point intersect = new Point(r.getOrigin());
    intersect.add(tmp.toPoint());
    hit.intersect = intersect;

    Vector norm = new Vector(intersect, center);
    norm.normalise();

    hit.normal = norm;
    return hit;
  }

  public Vector getNormal(Point intersect) {
    return hit.normal;
  }

  public Shader createShader(Ray r) {
    return new PhongShader(scene, r, hit, this);
  }

  public String toString() {
    return this.getClass().getName() + radius + "@" + center + this.optic_properties;
  }

}
