package net.andrewhatch.gfx.raytracer.scene.camera;

import net.andrewhatch.gfx.raytracer.scene.core.Positionable;
import net.andrewhatch.gfx.raytracer.scene.core.Vector;
import net.andrewhatch.gfx.raytracer.scene.rays.Ray;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;

import java.awt.Dimension;


public class Camera implements Positionable {

  private static final Vector UP = new Vector(0, 1, 0);
  private static final Vector FRONT = new Vector(0, 0, 1);
  private static final Vector LEFT = new Vector(-1, 0, 0);

  private Vector up;
  private Vector front;
  private Vector left;
  private Vector eyePosition;
  private double fov;

  private double viewportXSize;
  private double viewportYSize;

  private Vector dir;
  private Vector upWithScale;
  private Vector rightWithScale;
  private Vector lookAtTarget;

  public Camera() {
    this(new Dimension(640, 480));
  }

  public Camera(final Dimension viewportSize) {

    eyePosition = new Vector(0, -5, 1);

    up = new Vector(UP);
    front = new Vector(FRONT);
    left = new Vector(LEFT);

    fov = 40;
    viewportXSize = viewportSize.getWidth();
    viewportYSize = viewportSize.getHeight();

    updateVectors();

  }

  public Dimension getViewportSize() {
    return new Dimension((int)viewportXSize, (int) viewportYSize);
  }

  @Override
  public void setPosition(final Vector eyePosition) {
    this.eyePosition = eyePosition;

    if (lookAtTarget != null) {
      setLookAt(lookAtTarget);
    }
  }

  public void setLookAt(final Vector focusedPosition) {
    this.lookAtTarget = new Vector(focusedPosition);
    front = focusedPosition.subtractNew(eyePosition);
    left = front.crossProductNew(UP);
    updateVectors();
  }

  public void updateVectors() {
    up.normalise();
    left.normalise();
    front.normalise();

    double fovFactor = viewportXSize / viewportYSize;

    dir = front.multiplyNew(0.5);
    upWithScale = up.multiplyNew(Math.tan(Math.toRadians(fov / 2)));
    rightWithScale = left.multiplyNew(-fovFactor * Math.tan(Math.toRadians(fov / 2)));
  }

  public final Ray generateRay(final Scene scene,
                               final double xpos,
                               final double ypos) {
    final double u = (xpos - viewportXSize / 2.0) / viewportXSize;
    final double v = ((viewportYSize - ypos - 1) - viewportYSize / 2.0) / viewportYSize;

    final Vector dv = upWithScale.multiplyNew(v);
    final Vector du = rightWithScale.multiplyNew(u);
    final Vector dir = dv.addNew(du).addNew(this.dir);

    return new Ray(scene, eyePosition.toPoint(), dir, 0);
  }

  public final Ray[][] generateSupersampledRays(final Scene scene, int xpos, int ypos, int side, double f) {
    final Ray[][] rays = new Ray[side * 2 + 1][side * 2 + 1];

    for (int i = -side; i <= side; i++) {
      for (int j = -side; j <= side; j++) {
        int indexX = side + i;
        int indexY = side + j;
        rays[indexX][indexY] = generateRay(scene, xpos + i / f, ypos + j / f);
      }
    }

    return rays;
  }


}
