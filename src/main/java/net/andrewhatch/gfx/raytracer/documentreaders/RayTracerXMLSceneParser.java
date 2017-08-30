package net.andrewhatch.gfx.raytracer.documentreaders;

import net.andrewhatch.gfx.raytracer.scene.camera.Camera;
import net.andrewhatch.gfx.raytracer.scene.camera.CameraAnimation;
import net.andrewhatch.gfx.raytracer.scene.optics.Colour;
import net.andrewhatch.gfx.raytracer.scene.lighting.Light;
import net.andrewhatch.gfx.raytracer.scene.lighting.Lighting;
import net.andrewhatch.gfx.raytracer.scene.optics.OpticalProperties;
import net.andrewhatch.gfx.raytracer.scene.core.Point;
import net.andrewhatch.gfx.raytracer.scene.scene.Scene;
import net.andrewhatch.gfx.raytracer.scene.scene.SceneObject;
import net.andrewhatch.gfx.raytracer.scene.core.Vector;
import net.andrewhatch.gfx.raytracer.scene.geometry.Plane;
import net.andrewhatch.gfx.raytracer.scene.geometry.Sphere;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.StringReader;


public class RayTracerXMLSceneParser extends DefaultHandler implements GenericSceneParser {

  public static final String SCENE = "scene";
  public static final String AMBIENCE = "ambience";
  public static final String COLOUR = "colour";
  public static final String MAXDEPTH = "maxdepth";
  public static final String POSITION = "position";
  public static final String VIEWPOINT = "viewpoint";
  public static final String LOOKAT = "lookat";
  public static final String LIGHT = "light";

  public static final String OPTICS = "optics";
  public static final String OBJECTS = "objects";
  public static final String ANIMATION = "animation";
  public static final String TO = "to";

  public static final String SPHERE = "sphere";
  public static final String PLANE = "plane";
  public static final String NORMAL = "normal";
  public static final String SUPERSAMPLING = "supers";

  protected XMLReader reader;
  protected CameraAnimation animation = new CameraAnimation();
  protected Scene scene;
  protected Camera camera = new Camera();
  protected Lighting lighting = new Lighting();

  protected String old_name = "";
  protected String current_name = "";
  protected OpticalProperties current_optics;
  protected SceneObject current_object;
  boolean object_list = false;


  public RayTracerXMLSceneParser() {
    try {
      reader = XMLReaderFactory.createXMLReader();
      reader.setContentHandler(this);
    } catch (SAXException e) {
      e.printStackTrace();
    }
  }

  public void parse(String xmlstring) {
    this.scene = new Scene(new Lighting());
    try {
      reader.parse(new InputSource(new StringReader(xmlstring)));
    } catch (final SAXException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Scene getScene() {
    return this.scene;
  }

  public CameraAnimation getCameraAnimation() {
    return this.animation;
  }

  public void startDocument() throws SAXException {
  }

  public void endDocument() throws SAXException {
  }

  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    current_name = qName;

    if (object_list) {
      if (current_name.equals(SPHERE)) {
        Sphere s = new Sphere();
        s.setRadius(Double.parseDouble(atts.getValue("radius")));
        current_object = s;
        current_optics = new OpticalProperties();
      } else if (current_name.equals(PLANE)) {
        Plane p = new Plane();
        current_object = p;
        current_optics = new OpticalProperties();
      } else if (current_name.equals(LIGHT)) {
        Light l = new Light();
        l.setRadius(Double.parseDouble(atts.getValue("radius")));
        l.setBrightness(Double.parseDouble(atts.getValue("brightness")));
        current_object = l;
        current_optics = new OpticalProperties();
      } else if (current_name.equals(POSITION)) {
        if (current_object instanceof Sphere) {
          ((Sphere) current_object).setCenter(getPoint(atts));
        } else if (current_object instanceof Plane) {
          ((Plane) current_object).setCenter(getPoint(atts));
        } else if (current_object instanceof Light) {
          ((Light) current_object).setCenter(getPoint(atts));
        }
      } else if (current_name.equals(NORMAL)) {
        if (current_object instanceof Plane) {
          ((Plane) current_object).setPerpendicular(getVector(atts));
        }
      } else if (current_name.equals(COLOUR)) {
        current_optics.setColour(getColour(atts));
      } else if (current_name.equals(OPTICS)) {
        double refra = Double.parseDouble(atts.getValue("refraction"));
        double transp = Double.parseDouble(atts.getValue("transparency"));
        double refle = Double.parseDouble(atts.getValue("reflection"));
        double diffu = Double.parseDouble(atts.getValue("diffusion"));
        boolean lumin = Boolean.parseBoolean(atts.getValue("luminous"));
        current_optics.setValues(refra, transp, refle, diffu, lumin);
      }

    } else {
      if (current_name.equals(OBJECTS)) {
        object_list = true;
      } else if (current_name.equals(SCENE)) {
        // do nothing
      } else if (current_name.equals(COLOUR)) {
        if (old_name.equals(AMBIENCE)) {
          scene.setAmbientColour(getColour(atts));
        }
      } else if (current_name.equals(POSITION)) {
        if (old_name.equals(VIEWPOINT)) {
          camera.setPosition(getVector(atts));
          animation.startEyePosition = getVector(atts);
        } else if (old_name.equals(LOOKAT)) {
          camera.setLookAt(getVector(atts));
        } else if (old_name.equals(TO)) {
          animation.endEyePosition = getVector(atts);
        }
      } else if (current_name.equals(ANIMATION)) {
        animation.frames = Integer.parseInt(atts.getValue("frames"));
      }
    }
    old_name = current_name;

  }


  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    if (qName.equals(SPHERE)) {
      current_object.setOpticProperties(current_optics);
      scene.addSceneObject(current_object);
    } else if (qName.equals(PLANE)) {
      current_object.setOpticProperties(current_optics);
      scene.addSceneObject(current_object);
    } else if (qName.equals(LIGHT)) {
      current_object.setOpticProperties(current_optics);
      lighting.addLight((Light) current_object);
    } else if (qName.equals(OBJECTS)) {
      this.object_list = false;
    } else if (qName.equals(SCENE)) {
//      scene.setLighting(lighting);
//      lighting.addLightsToScene(scene);
    }
  }


  public void characters(char[] ch, int start, int length) throws SAXException {
    String s = new String(ch, start, length);
    s = s.trim();
    if (!s.equals("")) {
      if (old_name.equals(MAXDEPTH)) {
        scene.setMaxDepth(Integer.parseInt(s));
      } else if (old_name.equals(SUPERSAMPLING)) {
        scene.setSuperSampling(Boolean.parseBoolean(s));
      }
    }
  }

  private Colour getColour(Attributes atts) {
    double r = Double.parseDouble(atts.getValue("r"));
    double g = Double.parseDouble(atts.getValue("g"));
    double b = Double.parseDouble(atts.getValue("b"));
    Colour c = new Colour(r, g, b);
    return c;
  }

  private Vector getVector(Attributes atts) {
    double x = Double.parseDouble(atts.getValue("x"));
    double y = Double.parseDouble(atts.getValue("y"));
    double z = Double.parseDouble(atts.getValue("z"));
    Vector v = new Vector(x, y, z);
    return v;
  }

  private Point getPoint(Attributes atts) {
    double x = Double.parseDouble(atts.getValue("x"));
    double y = Double.parseDouble(atts.getValue("y"));
    double z = Double.parseDouble(atts.getValue("z"));
    Point p = new Point(x, y, z);
    return p;
  }

  public Camera getCamera() {
    return camera;
  }
}
