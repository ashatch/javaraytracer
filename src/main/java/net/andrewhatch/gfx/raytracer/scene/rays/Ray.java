package net.andrewhatch.gfx.raytracer.scene.rays;

import net.andrewhatch.gfx.raytracer.scene.core.Point;
import net.andrewhatch.gfx.raytracer.scene.core.Vector;
import net.andrewhatch.gfx.raytracer.scene.optics.Colour;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;


public class Ray {

  protected static final double FAR_AWAY = 1000000.0;
  protected Point origin;
  protected Scene scene;
  protected Vector direction;
  protected int depth;
  protected RayHitInfo closestHit = new RayHitInfo();

  public Ray(final Scene scene,
             final Point origin,
             final Vector direction,
             final int depth) {
    this.scene = scene;
    this.origin = origin;
    this.direction = direction;
    this.direction.normalise();
    this.depth = depth;
    closestHit.distance = FAR_AWAY;
  }

  public int getDepth() {
    return depth;
  }

  public Vector getDirection() {
    return direction;
  }

  public Point getOrigin() {
    return origin;
  }

  protected void processHit(final RayHitInfo hit) {
    if (hit.distance < closestHit.distance) {
      this.closestHit = hit;
    }
  }

  public void fire(final Colour c) {
    scene.getSceneObjects()
        .stream()
        .map(obj -> obj.intersect(this))
        .filter(hit -> hit != null)
        .forEach(hit -> this.processHit(hit));

    shade(c);
  }

  void shade(final Colour c) {
    if (didHitAnObject()) {
      shadeWithSceneObject(c);
    } else {
      c.set(0, 0, 0);
    }
  }

  private void shadeWithSceneObject(final Colour c) {
    if (isALightSource()) {
      c.set(closestHit.object.getOpticProperties().colour);
    } else {
      closestHit.object.createShader(this).writeColour(c);
    }
  }

  private boolean didHitAnObject() {
    return closestHit != null && closestHit.object != null && depth < scene.getMaxDepth();
  }

  private boolean isALightSource() {
    return closestHit.object.getOpticProperties().luminous;
  }
}
