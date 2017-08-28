package net.andrewhatch.gfx.raytracer.scene;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Lighting {

  List<Light> lights;

  public Lighting() {
    lights = new ArrayList();
  }

  public void addLight(Light light) {
    this.lights.add(light);
    updateRelativeBrightness();
  }

  private void updateRelativeBrightness() {
    int num_lights = lights.size();
    double total_brightness = 0.0;
    Light l;
    for (int i = 0; i < num_lights; i++) {
      l = lights.get(i);
      total_brightness += l.getBrightness();
    }

    for (int i = 0; i < num_lights; i++) {
      l = lights.get(i);
      l.setRelativeBrightness(l.getBrightness() / total_brightness);
    }
  }

  public void addLightsToScene(Scene s) {
    Light l;
    for (int i = 0; i < lights.size(); i++) {
      l = lights.get(i);
      s.addSceneObject(l);
    }
  }

  public Iterator<Light> getIterator() {
    return lights.iterator();
  }
}
