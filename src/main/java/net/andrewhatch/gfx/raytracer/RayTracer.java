package net.andrewhatch.gfx.raytracer;

import com.google.common.base.Charsets;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

import net.andrewhatch.gfx.raytracer.documentreaders.AshSceneParser;
import net.andrewhatch.gfx.raytracer.documentreaders.SceneParser;
import net.andrewhatch.gfx.raytracer.events.RayTraceFinished;
import net.andrewhatch.gfx.raytracer.events.RayTraceStarted;
import net.andrewhatch.gfx.raytracer.events.RayTracedLine;
import net.andrewhatch.gfx.raytracer.scene.camera.Camera;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.*;


public class RayTracer {

  SceneParser parser;
  RayTracerDisplay display;
  RayTracerEngine tracer;

  int frame = 1;

  private boolean save = false;
  private EventBus rayTracingEventBus;

  public static void main(String[] args) throws IOException {
    Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {}
    }).getInstance(RayTracer.class).rayTraceFilePath(args[0]);
  }

  public void rayTraceFilePath(String filePathString) throws IOException {
    this.rayTracingEventBus = new EventBus();
    this.rayTracingEventBus.register(this);

    parser = getParser(filePathString);

    parser.parse(new String(Files.readAllBytes(Paths.get(filePathString)), Charsets.UTF_8));
    Scene parsed_scene = parser.getScene();

    Camera c = parser.getCamera();

    tracer = new RayTracerEngine(rayTracingEventBus, parsed_scene, c);
    tracer.setSuperSampling(parsed_scene.isSuperSampling());

    display = new RayTracerDisplay(tracer);
    display.setPreferredSize(c.getViewportSize());

    display.addMessage("Supersampling: " + parsed_scene.isSuperSampling());
    display.addMessage("Scene: " + filePathString);

    JFrame f = new JFrame();
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    f.add(display);
    f.pack();
    f.setVisible(true);
    tracer.start();
  }



  @Subscribe
  public void traceStarted(final RayTraceStarted evt) {
    System.out.println("Ray Tracing started");
  }

  @Subscribe
  public void traceFinished(final RayTraceFinished evt) {
    System.out.println("Ray Tracing finished");

    if (save) {
      System.out.print("Writing frame " + frame + " ... ");
      System.out.flush();
      BufferedImage img = display.getTracedImage();
      try {
        ImageIO.write(img, "PNG", new File("trace_" + frame + ".png"));
      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println("done.");
    }
  }

  @Subscribe
  public void tracedLine(final RayTracedLine evet) {

  }

  private SceneParser getParser(final String doc) {
    return new AshSceneParser();
  }
}
