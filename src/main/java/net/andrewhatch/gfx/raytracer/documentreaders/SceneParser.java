package net.andrewhatch.gfx.raytracer.documentreaders;

import net.andrewhatch.gfx.raytracer.scene.camera.Camera;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;

public interface SceneParser {

  void parse(String document);

  Scene getScene();

  Camera getCamera();
}
