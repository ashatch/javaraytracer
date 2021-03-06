package net.andrewhatch.gfx.raytracer.documentreaders;

import net.andrewhatch.gfx.raytracer.lang.SceneBaseListener;
import net.andrewhatch.gfx.raytracer.lang.SceneLexer;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.AmbienceDeclarationContext;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.BrightnessDeclarationContext;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.CameraDefinitionContext;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.CameraLookAtDeclarationContext;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.CameraSizeDefinitionContext;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.ColourDeclarationContext;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.DefinitionContext;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.DiameterDeclarationContext;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.DiffusionDeclarationContext;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.LightAssignmentContext;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.LightDefinitionContext;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.LuminousDeclarationContext;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.MaxDepthDefinitionContext;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.OpticsDefinitionContext;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.PlaneDefinitionContext;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.ReflectionDeclarationContext;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.RefractionDeclarationContext;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.SceneContext;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.SphereDefinitionContext;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.TransparencyDeclarationContext;
import net.andrewhatch.gfx.raytracer.lang.SceneParser.VectorDeclarationContext;
import net.andrewhatch.gfx.raytracer.scene.camera.Camera;
import net.andrewhatch.gfx.raytracer.scene.core.Vector;
import net.andrewhatch.gfx.raytracer.scene.geometry.Plane;
import net.andrewhatch.gfx.raytracer.scene.geometry.Sphere;
import net.andrewhatch.gfx.raytracer.scene.lighting.Light;
import net.andrewhatch.gfx.raytracer.scene.lighting.Lighting;
import net.andrewhatch.gfx.raytracer.scene.optics.Colour;
import net.andrewhatch.gfx.raytracer.scene.optics.OpticalProperties;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AshSceneParser extends SceneBaseListener implements SceneParser {
  private static final Logger log = LoggerFactory.getLogger(AshSceneParser.class);
  private final Map<String, OpticalProperties> optics = new HashMap<>();
  private Scene scene;
  private Camera camera;
  private Vector currentVector;
  private double currentRadius;
  private OpticalProperties currentOptics;
  private OpticalProperties assignedOptics;
  private float currentBrightness;
  private int maxDepth = 1;

  @Override
  public void parse(
    final String document
  ) {
    final SceneLexer lexer = new SceneLexer(new ANTLRInputStream(document));
    final CommonTokenStream tokens = new CommonTokenStream(lexer);
    final net.andrewhatch.gfx.raytracer.lang.SceneParser parser =
      new net.andrewhatch.gfx.raytracer.lang.SceneParser(tokens);

    final SceneContext sceneContext = parser.scene();

    ParseTreeWalker.DEFAULT.walk(this, sceneContext);
  }

  @Override
  public Scene getScene() {
    return scene;
  }

  @Override
  public Camera getCamera() {
    return this.camera;
  }

  @Override
  public void enterScene(
    @NotNull SceneContext ctx
  ) {
    log.info("enter scene");
    this.scene = new Scene(new Lighting());
  }

  @Override
  public void exitScene(
    @NotNull SceneContext ctx
  ) {
    this.scene.setMaxDepth(this.maxDepth);
    this.scene.setSuperSampling(true);
  }

  @Override
  public void exitAmbienceDeclaration(
    @NotNull AmbienceDeclarationContext ctx
  ) {
    this.scene.setAmbientColour(new Colour(
      Double.parseDouble(ctx.floatList().Float(0).getText()),
      Double.parseDouble(ctx.floatList().Float(1).getText()),
      Double.parseDouble(ctx.floatList().Float(2).getText())
    ));
  }

  @Override
  public void exitCameraDefinition(
    @NotNull CameraDefinitionContext ctx
  ) {
    this.camera.setPosition(this.currentVector);
  }

  @Override
  public void exitCameraSizeDefinition(
    @NotNull CameraSizeDefinitionContext ctx
  ) {
    this.camera = new Camera(new Dimension(
      Integer.parseInt(ctx.Integer(0).getText()),
      Integer.parseInt(ctx.Integer(1).getText())
    ));
  }

  @Override
  public void exitMaxDepthDefinition(
    @NotNull MaxDepthDefinitionContext ctx
  ) {
    this.maxDepth = Integer.parseInt(ctx.Integer().getText());
  }

  @Override
  public void enterDefinition(
    @NotNull DefinitionContext ctx
  ) {
    this.currentRadius = 0.0;
    this.currentVector = new Vector();
  }

  @Override
  public void exitColourDeclaration(
    @NotNull final ColourDeclarationContext ctx
  ) {
    this.currentOptics.setColour(new Colour(
      Double.parseDouble(ctx.Float(0).getText()),
      Double.parseDouble(ctx.Float(1).getText()),
      Double.parseDouble(ctx.Float(2).getText())
    ));
  }

  @Override
  public void exitCameraLookAtDeclaration(
    @NotNull CameraLookAtDeclarationContext ctx
  ) {
    getCamera().setLookAt(new Vector(
      Float.parseFloat(ctx.Float(0).getText()),
      Float.parseFloat(ctx.Float(1).getText()),
      Float.parseFloat(ctx.Float(2).getText())
    ));
  }

  @Override
  public void exitDiameterDeclaration(
    @NotNull DiameterDeclarationContext ctx
  ) {
    this.currentRadius = Double.parseDouble(ctx.Float().getText()) / 2.0;
  }

  @Override
  public void exitVectorDeclaration(
    @NotNull VectorDeclarationContext ctx
  ) {
    final float x = Float.parseFloat(ctx.Float(0).getText());
    final float y = Float.parseFloat(ctx.Float(1).getText());
    final float z = Float.parseFloat(ctx.Float(2).getText());
    this.currentVector = new Vector(x, y, z);
  }

  @Override
  public void enterOpticsDefinition(
    @NotNull OpticsDefinitionContext ctx
  ) {
    this.currentOptics = new OpticalProperties();
  }

  @Override
  public void exitOpticsDefinition(
    @NotNull OpticsDefinitionContext ctx
  ) {
    this.optics.put(ctx.Identifier().getText(), this.currentOptics);
    log.info("Optics now: {}", this.optics);
  }

  @Override
  public void exitReflectionDeclaration(
    @NotNull ReflectionDeclarationContext ctx
  ) {
    final float reflection = Float.parseFloat(ctx.Float().getText());
    this.currentOptics.setReflectiveness(reflection);
  }

  @Override
  public void exitRefractionDeclaration(
    @NotNull RefractionDeclarationContext ctx
  ) {
    final float refraction = Float.parseFloat(ctx.Float().getText());
    this.currentOptics.setRefractiveness(refraction);
  }

  @Override
  public void exitTransparencyDeclaration(
    @NotNull TransparencyDeclarationContext ctx
  ) {
    final float transparency = Float.parseFloat(ctx.Float().getText());
    this.currentOptics.setTransparency(transparency);
  }

  @Override
  public void exitDiffusionDeclaration(
    @NotNull DiffusionDeclarationContext ctx
  ) {
    final float diffusion = Float.parseFloat(ctx.Float().getText());
    this.currentOptics.setDiffusion(diffusion);
  }

  @Override
  public void exitLuminousDeclaration(
    @NotNull LuminousDeclarationContext ctx
  ) {
    final boolean luminous = "yes".equals(ctx.BooleanValue().getText());
    this.currentOptics.setLuminous(luminous);
  }

  @Override
  public void exitSphereDefinition(
    @NotNull SphereDefinitionContext ctx
  ) {
    final Sphere sphere = new Sphere(
      this.currentVector.toPoint(),
      this.currentRadius,
      this.assignedOptics);

    log.info("Sphere: radius={} op={}", sphere.getRadius(), sphere.getOpticProperties());

    this.scene.addSceneObject(sphere);
  }

  @Override
  public void exitPlaneDefinition(
    @NotNull PlaneDefinitionContext ctx
  ) {
    final Plane plane = new Plane(
      this.currentVector.toPoint(),
      new Vector(0f, 1f, 0f),
      this.assignedOptics);

    this.scene.addSceneObject(plane);
  }

  @Override
  public void exitLightDefinition(
    @NotNull LightDefinitionContext ctx
  ) {
    final Light light = new Light(this.currentVector.toPoint());
    light.setRadius(this.currentRadius);
    light.setBrightness(this.currentBrightness);
    light.setOpticProperties(this.assignedOptics);

    log.info(
      "Light radius {} brightness {} op={}",
      light.getRadius(),
      light.getBrightness(),
      light.getOpticProperties());

    this.scene.addSceneObject(light);
  }

  @Override
  public void exitLightAssignment(
    @NotNull LightAssignmentContext ctx
  ) {
    this.assignedOptics = this.optics.get(ctx.Identifier().getText());
  }

  @Override
  public void exitBrightnessDeclaration(
    @NotNull BrightnessDeclarationContext ctx
  ) {
    this.currentBrightness = Float.parseFloat(ctx.Float().getText());
  }
}
