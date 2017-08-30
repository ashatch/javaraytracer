package net.andrewhatch.gfx.raytracer.documentreaders;

import net.andrewhatch.gfx.raytracer.lang.SceneBaseListener;
import net.andrewhatch.gfx.raytracer.lang.SceneLexer;
import net.andrewhatch.gfx.raytracer.lang.SceneParser;
import net.andrewhatch.gfx.raytracer.scene.camera.Camera;
import net.andrewhatch.gfx.raytracer.scene.camera.CameraAnimation;
import net.andrewhatch.gfx.raytracer.scene.optics.Colour;
import net.andrewhatch.gfx.raytracer.scene.lighting.Light;
import net.andrewhatch.gfx.raytracer.scene.lighting.Lighting;
import net.andrewhatch.gfx.raytracer.scene.optics.OpticalProperties;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;
import net.andrewhatch.gfx.raytracer.scene.core.Vector;
import net.andrewhatch.gfx.raytracer.scene.geometry.Plane;
import net.andrewhatch.gfx.raytracer.scene.geometry.Sphere;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AshSceneParser extends SceneBaseListener implements GenericSceneParser {
  private static final Logger log = LoggerFactory.getLogger(AshSceneParser.class);

  private Scene scene;
  private CameraAnimation cameraAnimation;
  private Camera camera;
  private Vector viewpoint;
  private Vector currentVector;
  private double currentRadius;
  //private OpticalProperties optics;
  private final Map<String, OpticalProperties> optics = new HashMap<>();
  private OpticalProperties currentOptics;
  private OpticalProperties assignedOptics;
  private float currentBrightness;

  @Override
  public void parse(String document) {
    final SceneLexer lexer = new SceneLexer(new ANTLRInputStream(document));
    final CommonTokenStream tokens = new CommonTokenStream(lexer);
    final SceneParser parser = new SceneParser(tokens);
    final SceneParser.SceneContext sceneContext = parser.scene();

    ParseTreeWalker.DEFAULT.walk(this, sceneContext);
  }

  @Override
  public Scene getScene() {
    return scene;
  }

  @Override
  public CameraAnimation getCameraAnimation() {
    if (this.cameraAnimation == null) {
      this.cameraAnimation = new CameraAnimation();
    }
    return this.cameraAnimation;
  }

  @Override
  public Camera getCamera() {
    if (this.camera == null) {
      this.camera = new Camera(new Dimension(800,600));
    }
    return this.camera;
  }

  @Override public void enterScene(@NotNull SceneParser.SceneContext ctx) {
    log.info("enter scene");
    this.scene = new Scene(new Lighting());
    this.scene.setMaxDepth(16);
    this.scene.setSuperSampling(true);
  }

  @Override public void exitAmbienceDeclaration(@NotNull SceneParser.AmbienceDeclarationContext ctx) {
    this.scene.setAmbientColour(new Colour(
        Double.parseDouble(ctx.floatList().Float(0).getText()),
        Double.parseDouble(ctx.floatList().Float(1).getText()),
        Double.parseDouble(ctx.floatList().Float(2).getText())
    ));
  }

  @Override public void enterDefinition(@NotNull SceneParser.DefinitionContext ctx) {
    this.currentRadius = 0.0;
    this.currentVector = new Vector();
  }

  @Override public void exitColourDeclaration(@NotNull final SceneParser.ColourDeclarationContext ctx) {
    this.currentOptics.setColour(new Colour(
        Double.parseDouble(ctx.Float(0).getText()),
        Double.parseDouble(ctx.Float(1).getText()),
        Double.parseDouble(ctx.Float(2).getText())
    ));
  }

  @Override public void exitCameraParameterDeclaration(@NotNull final SceneParser.CameraParameterDeclarationContext ctx) {
    if ("viewpoint".equals(ctx.cameraParameterKey().getText())) {
      this.viewpoint = position(ctx);
      log.info("viewpoint: {}", this.viewpoint);
      getCamera().setPosition(this.viewpoint);
      getCameraAnimation().startEyePosition = this.viewpoint;
      getCameraAnimation().frames = 50;
    } else if ("to".equals(ctx.cameraParameterKey().getText())) {
      getCameraAnimation().endEyePosition = position(ctx);
      log.info("to: {}",  getCameraAnimation().endEyePosition);
    } else if ("lookAt".equals(ctx.cameraParameterKey().getText())) {
      getCamera().setLookAt(position(ctx));
      log.info("lookAt: {}",  getCamera().getLookAt());
    }
  }

  @Override public void exitDiameterDeclaration(@NotNull SceneParser.DiameterDeclarationContext ctx) {
    this.currentRadius = Double.parseDouble(ctx.Float().getText())/2.0;
  }

  @Override public void exitVectorDeclaration(@NotNull SceneParser.VectorDeclarationContext ctx) {
    final float x = Float.parseFloat(ctx.Float(0).getText());
    final float y = Float.parseFloat(ctx.Float(1).getText());
    final float z = Float.parseFloat(ctx.Float(2).getText());
    this.currentVector = new Vector(x, y, z);
  }

  @Override public void enterOpticsDefinition(@NotNull SceneParser.OpticsDefinitionContext ctx) {
    this.currentOptics = new OpticalProperties();
  }

  @Override public void exitOpticsDefinition(@NotNull SceneParser.OpticsDefinitionContext ctx) {
    this.optics.put(ctx.Identifier().getText(), this.currentOptics);
    log.info("Optics now: {}", this.optics);
  }

  @Override public void exitReflectionDeclaration(@NotNull SceneParser.ReflectionDeclarationContext ctx) {
    final float reflection = Float.parseFloat(ctx.Float().getText());
    this.currentOptics.setReflectiveness(reflection);
  }

  @Override public void exitRefractionDeclaration(@NotNull SceneParser.RefractionDeclarationContext ctx) {
    final float refraction = Float.parseFloat(ctx.Float().getText());
    this.currentOptics.setRefractiveness(refraction);
  }

  @Override public void exitTransparencyDeclaration(@NotNull SceneParser.TransparencyDeclarationContext ctx) {
    final float transparency = Float.parseFloat(ctx.Float().getText());
    this.currentOptics.setTransparency(transparency);
  }

  @Override public void exitDiffusionDeclaration(@NotNull SceneParser.DiffusionDeclarationContext ctx) {
    final float diffusion = Float.parseFloat(ctx.Float().getText());
    this.currentOptics.setDiffusion(diffusion);
  }

  @Override public void exitLuminousDeclaration(@NotNull SceneParser.LuminousDeclarationContext ctx) {
    final boolean luminous = "yes".equals(ctx.BooleanValue().getText());
    this.currentOptics.setLuminous(luminous);
  }

  @Override public void exitSphereDefinition(@NotNull SceneParser.SphereDefinitionContext ctx) {
    final Sphere sphere = new Sphere(this.currentVector.toPoint(), this.currentRadius, this.assignedOptics);
    log.info("Sphere: radius={} op={}", sphere.getRadius(), sphere.getOpticProperties());
    this.scene.addSceneObject(sphere);
  }

  @Override public void exitPlaneDefinition(@NotNull SceneParser.PlaneDefinitionContext ctx) {
    final Plane plane = new Plane(
        this.currentVector.toPoint(), new Vector(0f, 1f, 0f), this.assignedOptics);
    this.scene.addSceneObject(plane);
  }

  @Override public void exitLightDefinition(@NotNull SceneParser.LightDefinitionContext ctx) {
    final Light light = new Light(this.currentVector.toPoint());
    light.setRadius(this.currentRadius);
    light.setBrightness(this.currentBrightness);
    light.setOpticProperties(this.assignedOptics);
    log.info("Light radius {} brightness {} op={}", light.getRadius(), light.getBrightness(), light.getOpticProperties());
    this.scene.addSceneObject(light);
  }

  @Override public void exitLightAssignment(@NotNull SceneParser.LightAssignmentContext ctx) {
    this.assignedOptics = this.optics.get(ctx.Identifier().getText());
  }

  @Override public void exitBrightnessDeclaration(@NotNull SceneParser.BrightnessDeclarationContext ctx) {
    this.currentBrightness = Float.parseFloat(ctx.Float().getText());
  }

  private Vector position(@NotNull final SceneParser.CameraParameterDeclarationContext ctx) {
    double x = Double.valueOf(ctx.floatList().Float(0).getText());
    double y = Double.valueOf(ctx.floatList().Float(1).getText());
    double z = Double.valueOf(ctx.floatList().Float(2).getText());
    return new Vector(x, y, z);
  }
}
