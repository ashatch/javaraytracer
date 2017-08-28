package net.andrewhatch.gfx.raytracer;

import com.google.common.base.Charsets;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

import net.andrewhatch.gfx.raytracer.documentreaders.AshSceneParser;
import net.andrewhatch.gfx.raytracer.documentreaders.GenericSceneParser;
import net.andrewhatch.gfx.raytracer.documentreaders.RayTracerXMLSceneParser;
import net.andrewhatch.gfx.raytracer.scene.Camera;
import net.andrewhatch.gfx.raytracer.scene.CameraAnimation;
import net.andrewhatch.gfx.raytracer.scene.Scene;
import net.andrewhatch.gfx.raytracer.scene.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.*;


public class RayTracer implements RayTracerListener {

  GenericSceneParser parser;
  RayTracerDisplay display;
  RayTracerEngine tracer;
  CameraAnimation animation;

  int frame = 1;

  private boolean save = false;
  private boolean animate = false;

  public static void main(String[] args) throws IOException {
    Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {}
    }).getInstance(RayTracer.class).go(args[0]);
  }

  public void go(String doc) throws IOException {
    parser = getParser(doc);

    parser.parse(new String(Files.readAllBytes(Paths.get(doc)), Charsets.UTF_8));
    Scene parsed_scene = parser.getScene();

    animation = parser.getCameraAnimation();
    Vector cam_pos = animation.getCameraPositionForFrame(frame);

    Dimension image_size = new Dimension(900, 500);

    Camera c = parser.getCamera();
    c.setViewportSize(image_size);
    c.setPosition(cam_pos);

    tracer = new RayTracerEngine(image_size, parsed_scene, c);
    tracer.setSuperSampling(parsed_scene.isSuperSampling());
    tracer.setRayTracerListener(this);

    display = new RayTracerDisplay(tracer);
    display.setPreferredSize(image_size);

    display.addMessage("Supersampling: " + parsed_scene.isSuperSampling());
    display.addMessage("Scene: " + doc);

    JFrame f = new JFrame();
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    f.add(display);
    f.pack();
    f.setVisible(true);
    tracer.start();
  }

  private GenericSceneParser getParser(String doc) {
    return doc.endsWith(".xml") ?
        new RayTracerXMLSceneParser() :
        new AshSceneParser();
  }

  public void setupForFrame() {
    Vector cam_pos = animation.getCameraPositionForFrame(frame);
    Camera c = parser.getCamera();
    c.setPosition(cam_pos);
    tracer.setCamera(c);
    display.reset();
    display.repaint();
  }


  public void traceStarted() {
    System.out.println("Ray Tracing started");
  }

  public void traceFinished() {
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

    if (animate && frame < animation.frames) {
      frame++;
      setupForFrame();
      tracer.start();
    }
  }

  public void traceAborted() {
    System.out.println("Ray Tracing aborted");
  }

  public void tracedLine(int lineCompleted) {

  }
}