package net.andrewhatch.gfx.raytracer;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.xml.sax.SAXException;

import net.andrewhatch.gfx.raytracer.parser.RayTracerXMLSceneParser;
import net.andrewhatch.gfx.raytracer.scene.Camera;
import net.andrewhatch.gfx.raytracer.scene.CameraAnimation;
import net.andrewhatch.gfx.raytracer.scene.Scene;
import net.andrewhatch.gfx.raytracer.scene.Vector;
import net.andrewhatch.gfx.raytracer.utils.FileUtils;



public class RayTracer implements RayTracerListener {

	RayTracerXMLSceneParser parser;
	RayTracerDisplay display;
	RayTracerEngine tracer;
	CameraAnimation animation;
	
	int frame = 1;
	
	private boolean save = false;
	private boolean animate = false;
	
	public static void main(String[] args) {
		new RayTracer(args[0]);
	}
	
	public RayTracer(String doc) {
		parser = new RayTracerXMLSceneParser();
		String xmlstring = FileUtils.readDocAsString(doc); 
		try {
			parser.parse(xmlstring);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene parsed_scene = parser.getScene();
			
		animation = parser.getCameraAnimation(); 
		Vector cam_pos = animation.getCameraPositionForFrame(frame);		
	
		Dimension image_size = new Dimension(900,500);

		Camera c = parser.getCamera();
		c.setViewportSize(image_size);
		c.setViewpoint(cam_pos);			
		
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
	
	public void setupForFrame() {
		Vector cam_pos = animation.getCameraPositionForFrame(frame);		
		Camera c = parser.getCamera();
		c.setViewpoint(cam_pos);
		tracer.setCamera(c);
		display.reset();
		display.repaint();
	}


	public void traceStarted() {
		System.out.println("Ray Tracing started");
	}

	public void traceFinished() {
		System.out.println("Ray Tracing finished");

		if(save) {
			System.out.print("Writing frame " + frame + " ... "); System.out.flush();
			BufferedImage img = display.getTracedImage();
			try {
				ImageIO.write(img, "PNG", new File("trace_" + frame + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("done.");
		}
		
		if(animate && frame < animation.frames) {
			frame++;
			setupForFrame();
			tracer.start();
		}
	}

	public void traceAborted() {
		System.out.println("Ray Tracing aborted");
	}

	public void tracedLine(int line_completed) {

	}	
}
