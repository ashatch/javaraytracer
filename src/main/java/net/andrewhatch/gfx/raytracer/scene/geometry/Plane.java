package net.andrewhatch.gfx.raytracer.scene.geometry;

import net.andrewhatch.gfx.raytracer.scene.Colour;
import net.andrewhatch.gfx.raytracer.scene.OpticalProperties;
import net.andrewhatch.gfx.raytracer.scene.Point;
import net.andrewhatch.gfx.raytracer.scene.Ray;
import net.andrewhatch.gfx.raytracer.scene.RayHitInfo;
import net.andrewhatch.gfx.raytracer.scene.SceneObject;
import net.andrewhatch.gfx.raytracer.scene.Vector;
import net.andrewhatch.gfx.raytracer.shaders.PlaneShader;
import net.andrewhatch.gfx.raytracer.shaders.Shader;

public class Plane extends SceneObject {
  // geometry
  private Point center;
  private Vector normal;

  private double cos_inv;
  private RayHitInfo hit;

  public Plane() {
    super();
  }

  public Plane(final Point center,
               final Vector perpendicular,
               final OpticalProperties prop) {
    super();
    this.center = center;
    this.optic_properties = prop;
    this.normal = new Vector(perpendicular);
    update();
  }

  public void setCenter(final Point center) {
    this.center = center;
    update();
  }

  public void setPerpendicular(final Vector perp) {
    this.normal = new Vector(perp);
    update();
  }

  private void update() {
    if (this.normal != null) {
      this.normal.normalise();
      this.cos_inv = 1.0 / this.normal.dotproduct(new Vector(0, 0, 1));
    }
  }


  public Colour getPixel(final Point p) {
    return this.optic_properties.colour;
  }

  public Vector getNormal(final Point intersect) {
    return normal;
  }

  public RayHitInfo intersect(final Ray ray) {
    double inc = ray.getDirection().dotproduct(normal);
    if (inc >= Vector.EPSILON) {
      return null;
    }

    Vector oc = new Vector(center, ray.origin);

    double t = oc.dotproduct(normal);
    t /= inc;
    if (t > Vector.EPSILON) {
      hit = new RayHitInfo();
      hit.distance = t;

      final Vector tmp = new Vector(ray.getDirection());
      tmp.scale(hit.distance);
      final Point intersect = new Point(ray.getOrigin());
      intersect.add(tmp.toPoint());
      hit.intersect = intersect;
      hit.normal = this.normal;
      hit.object = this;

      return hit;
    }
    return null;
  }

  public Shader createShader(Ray ray) {
    return new PlaneShader(scene, ray, hit, this);
  }

  public String toString() {
    return "PLANE";
  }

  @Override
  public void setPosition(Vector position) {
    this.center = position.toPoint();
  }
}
