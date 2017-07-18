package net.andrewhatch.gfx.raytracer.documentreaders;

import net.andrewhatch.gfx.raytracer.scene.Camera;
import net.andrewhatch.gfx.raytracer.scene.CameraAnimation;
import net.andrewhatch.gfx.raytracer.scene.Scene;

public interface GenericSceneParser {

  void parse(String document);

  Scene getScene();

  CameraAnimation getCameraAnimation();

  Camera getCamera();
}
