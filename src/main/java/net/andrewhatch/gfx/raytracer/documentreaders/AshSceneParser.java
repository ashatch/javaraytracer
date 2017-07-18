package net.andrewhatch.gfx.raytracer.documentreaders;

import net.andrewhatch.gfx.raytracer.lang.SceneBaseListener;
import net.andrewhatch.gfx.raytracer.lang.SceneLexer;
import net.andrewhatch.gfx.raytracer.lang.SceneParser;
import net.andrewhatch.gfx.raytracer.scene.Camera;
import net.andrewhatch.gfx.raytracer.scene.CameraAnimation;
import net.andrewhatch.gfx.raytracer.scene.Colour;
import net.andrewhatch.gfx.raytracer.scene.Scene;
import net.andrewhatch.gfx.raytracer.scene.Vector;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Dimension;

public class AshSceneParser extends SceneBaseListener implements GenericSceneParser {
  private static final Logger log = LoggerFactory.getLogger(AshSceneParser.class);

  private Scene scene;
  private CameraAnimation cameraAnimation;
  private Camera camera;
  private Vector viewpoint;
  private Colour ambient;

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
    this.scene = new Scene();
  }

  @Override public void exitScene(@NotNull SceneParser.SceneContext ctx) {
    log.info("exit scene");
  }

  @Override public void exitColourDeclaration(@NotNull final SceneParser.ColourDeclarationContext ctx) {
    if ("ambient".equals(ctx.colourKey().getText())) {
      float r = Float.valueOf(ctx.floatList().Float(0).getText());
      float g = Float.valueOf(ctx.floatList().Float(1).getText());
      float b = Float.valueOf(ctx.floatList().Float(2).getText());
      this.ambient = new Colour(r, g, b);
    }
  }

  @Override public void exitPositionDeclaration(@NotNull final SceneParser.PositionDeclarationContext ctx) {
    if ("viewpoint".equals(ctx.positionKey().getText())) {
      this.viewpoint = position(ctx);
      getCamera().setViewpoint(this.viewpoint);
      getCameraAnimation().startEyePosition = this.viewpoint;
    } else if ("to".equals(ctx.positionKey().getText())) {
      getCameraAnimation().endEyePosition = position(ctx);
    } else if ("lookAt".equals(ctx.positionKey().getText())) {
      getCamera().setLookAt(position(ctx));
    }
  }

  private Vector position(@NotNull final SceneParser.PositionDeclarationContext ctx) {
    double x = Double.valueOf(ctx.floatList().Float(0).getText());
    double y = Double.valueOf(ctx.floatList().Float(1).getText());
    double z = Double.valueOf(ctx.floatList().Float(2).getText());
    return new Vector(x, y, z);
  }
}
