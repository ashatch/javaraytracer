package net.andrewhatch.gfx.raytracer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JPanel;

public class RayTracerDisplay extends JPanel {

  private static final long serialVersionUID = -1875042609484267735L;
  private RayTracerEngine tracer;
  private Image raytrace;
  private BufferedImage buf_img;
  private Graphics off_gfx;
  private Font info_font = new Font("Arial", Font.PLAIN, 8);
  private FontMetrics fm;
  private Vector<String> messages = new Vector<String>();

  public RayTracerDisplay(RayTracerEngine tracer) {
    super();
    this.tracer = tracer;
    raytrace = createImage(this.tracer);
  }

  public void reset() {
    raytrace = createImage(this.tracer);
    clearMessages();
  }

  public void addMessage(String msg) {
    this.messages.add(msg);
  }

  public void clearMessages() {
    this.messages.removeAllElements();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (off_gfx == null || buf_img == null) {
      buf_img = new BufferedImage(tracer.getWidth(), tracer.getHeight(), BufferedImage.TYPE_INT_RGB);
      off_gfx = buf_img.getGraphics();
      fm = g.getFontMetrics(info_font);
    }
    off_gfx.drawImage(raytrace, 0, 0, this);
    g.drawImage(buf_img, 0, 0, this);
    g.setColor(Color.white);
    g.setFont(info_font);
    if (!tracer.isFinished()) {
      int y = 10;
      g.drawString("Rendering..." + (int) tracer.getPercentComplete() + "%", 10, y);
      Iterator<String> i = messages.iterator();
      while (i.hasNext()) {
        y += fm.getHeight();
        g.drawString((String) i.next(), 10, y);
      }
    }
  }

  public BufferedImage getTracedImage() {
    return buf_img;
  }

  public RayTracerEngine getRayTracerEngine() {
    return tracer;
  }
}
