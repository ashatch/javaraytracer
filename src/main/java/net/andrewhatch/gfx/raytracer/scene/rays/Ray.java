package net.andrewhatch.gfx.raytracer.scene.rays;

import net.andrewhatch.gfx.raytracer.scene.optics.Colour;
import net.andrewhatch.gfx.raytracer.scene.core.Point;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;
import net.andrewhatch.gfx.raytracer.scene.scene.SceneObject;
import net.andrewhatch.gfx.raytracer.scene.core.Vector;
import net.andrewhatch.gfx.raytracer.shaders.Shader;

import java.util.Iterator;


public class Ray {

  protected static final double FAR_AWAY = 1000000.0;
  public Point origin;
  protected Scene scene;
  protected Vector direction;
  protected int depth;
  //protected SceneObject intersected_object;
  //protected double distance = FAR_AWAY;
  protected RayHitInfo closest_hit = new RayHitInfo();

  public Ray(Scene scene, Point origin, Vector direction, int depth) {
    this.scene = scene;
    this.origin = origin;
    this.direction = direction;
    this.direction.normalise();
    this.depth = depth;
    closest_hit.distance = FAR_AWAY;
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

  protected void processHit(RayHitInfo hit) {
    if (hit.distance < closest_hit.distance) {
      this.closest_hit = hit;
    }
  }

  public void fire(Colour c) {
    // Go through all objects and try to intersect with them
    Iterator<SceneObject> i = scene.getSceneObjects().iterator();
    SceneObject obj;
    while (i.hasNext()) {
      obj = i.next();
      RayHitInfo hit = obj.intersect(this);
      if (hit != null) {
        this.processHit(hit);
      }
    }
    shade(c);

  }

  void shade(Colour c) {
    if (closest_hit == null || closest_hit.object == null || depth > scene.getMaxDepth()) {
      c.set(0, 0, 0);
      return;
    }

    if (closest_hit.object.getOpticProperties().luminous) {
      // It's the light source
      c.set(closest_hit.object.getOpticProperties().colour);
      return;
    }

    // Ask the object for its shader
    Shader shader = closest_hit.object.createShader(this);
    shader.getColour(c);
  }

}