package net.andrewhatch.gfx.raytracer.scene.scene;

import net.andrewhatch.gfx.raytracer.scene.optics.Colour;
import net.andrewhatch.gfx.raytracer.scene.core.Point;
import net.andrewhatch.gfx.raytracer.scene.lighting.Light;
import net.andrewhatch.gfx.raytracer.scene.lighting.Lighting;

import java.util.ArrayList;
import java.util.List;


public class Scene {

  private final Lighting lighting;
  private final List<SceneObject> objs = new ArrayList();

  private int max_depth = 4;
  private boolean superSampling = false;
  private Colour ambient_colour;

  public Scene(final Lighting lighting) {
    this.lighting = lighting;
  }

  public void addSceneObject(SceneObject obj) {
    if (obj instanceof Light) {
      this.lighting.addLight((Light)obj);
    }
    obj.setScene(this);
    objs.add(obj);
  }

  public List<SceneObject> getSceneObjects() {
    return objs;
  }

  public int getMaxDepth() {
    return max_depth;
  }

  public void setMaxDepth(int max_depth) {
    this.max_depth = max_depth;
  }

  public Colour getAmbientColour() {
    return ambient_colour;
  }

  public void setAmbientColour(Colour ambient_colour) {
    this.ambient_colour = ambient_colour;
  }

  public String toString() {
    return "scene[" + max_depth + ", " + ambient_colour + "," + objs + ", " + lighting + "]";
  }

  public Lighting getLighting() {
    return lighting;
  }

  public boolean isSuperSampling() {
    return superSampling;
  }

  public void setSuperSampling(boolean superSampling) {
    this.superSampling = superSampling;
  }
}
