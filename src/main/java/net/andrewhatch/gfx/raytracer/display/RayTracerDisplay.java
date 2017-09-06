package net.andrewhatch.gfx.raytracer.display;

import net.andrewhatch.gfx.raytracer.engine.RayTracerEngine;
import net.andrewhatch.gfx.raytracer.engine.TracedImageProducer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.*;

public class RayTracerDisplay extends JPanel {

  private static final long serialVersionUID = -1875042609484267735L;
  private final TracedImageProducer imageProducer;
  private RayTracerEngine rayTracerEngine;
  private Image rayTracedImage;
  private BufferedImage buf_img;
  private Graphics off_gfx;
  private Font info_font = new Font("Arial", Font.PLAIN, 8);
  private FontMetrics fm;
  private Vector<String> messages = new Vector<>();

  public RayTracerDisplay(final RayTracerEngine rayTracerEngine,
                          final TracedImageProducer imageProducer) {
    super();
    this.rayTracerEngine = rayTracerEngine;
    this.imageProducer = imageProducer;
  }

  public void addMessage(final String msg) {
    this.messages.add(msg);
  }

  @Override
  public void paintComponent(final Graphics g) {
    super.paintComponent(g);
    if (!graphicsAreInitialized()) {
      initializeGraphics(g);
    }

    drawImage(g);
    drawMessages(g);
  }

  private void drawImage(final Graphics g) {
    off_gfx.drawImage(rayTracedImage, 0, 0, this);
    g.drawImage(buf_img, 0, 0, this);
  }

  private boolean graphicsAreInitialized() {
    return off_gfx != null && buf_img != null;
  }

  private void initializeGraphics(final Graphics g) {
    rayTracedImage = createImage(imageProducer);

    buf_img = new BufferedImage(rayTracerEngine.getWidth(),
        rayTracerEngine.getHeight(),
        BufferedImage.TYPE_INT_RGB);

    off_gfx = buf_img.getGraphics();
    fm = g.getFontMetrics(info_font);
  }

  private void drawMessages(final Graphics g) {
    g.setColor(Color.white);
    g.setFont(info_font);
    if (!rayTracerEngine.isFinished()) {
      int y = 10;
      g.drawString("Rendering..." + (int) rayTracerEngine.getPercentComplete() + "%", 10, y);
      final Iterator<String> i = messages.iterator();
      while (i.hasNext()) {
        y += fm.getHeight();
        g.drawString(i.next(), 10, y);
      }
    }
  }

  public BufferedImage getRayTracedImage() {
    return buf_img;
  }
}
