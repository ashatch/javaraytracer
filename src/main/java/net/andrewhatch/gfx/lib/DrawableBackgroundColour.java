package net.andrewhatch.gfx.lib;

import java.awt.Graphics2D;

public class DrawableBackgroundColour extends DrawableElement {

  final protected int width;
  final protected int height;

  public DrawableBackgroundColour(final int w, final int h) {
    super();
    this.width = w;
    this.height = h;
  }

  public void draw(final Graphics2D gfx, final long tick_count) {
    gfx.setComposite(getAlphaComposite());
    gfx.setColor(colour);
    gfx.fillRect(0, 0, width, height);
  }

}
