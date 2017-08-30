package net.andrewhatch.gfx.raytracer.documentreaders;

import net.andrewhatch.gfx.raytracer.scene.camera.Camera;
import net.andrewhatch.gfx.raytracer.scene.camera.CameraAnimation;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;

public interface GenericSceneParser {

  void parse(String document);

  Scene getScene();

  CameraAnimation getCameraAnimation();

  Camera getCamera();
}
