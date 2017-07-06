package net.andrewhatch.gfx.lib;

import java.awt.Color;
import java.awt.Graphics2D;

public class AnimatedBackground extends DrawableElement {

  protected final int width;
  protected final int height;
  private final int[] ws = {100, 30, 40, 20, 200, 50, 100, 150};
  private final int[] xs = {0, 50, 100, 150, 300, 500, 30, 70};
  private final int[] vs = {1, 5, 2, -4, -3, 6, 2, -1};
  private final Color[] cs = {
      new Color(0.9f, 0.9f, 0.9f),
      new Color(0.7f, 0.7f, 0.7f),
      new Color(0.5f, 0.5f, 0.5f),
      new Color(0.8f, 0.8f, 0.8f),
      new Color(0.9f, 0.9f, 0.9f),
      new Color(0.6f, 0.6f, 0.6f),
      new Color(0.9f, 0.9f, 0.9f),
      new Color(0.5f, 0.5f, 0.5f)
  };

  public AnimatedBackground(final int width, final int height) {
    super();
    this.width = width;
    this.height = height;
  }

  public void draw(final Graphics2D gfx, final long tickCount) {
    gfx.setComposite(getAlphaComposite());
    gfx.setColor(colour);
    gfx.fillRect(0, 0, width, height);


    for (int i = 0; i < xs.length; i++) {
      xs[i] = xs[i] + vs[i];
      if (xs[i] > width + ws[i]) {
        xs[i] = -ws[i];
      }
      if (xs[i] + ws[i] < 0) {
        xs[i] = width + ws[i];
      }
      gfx.setColor(cs[i]);
      gfx.fillRect(xs[i], y, ws[i], height);

    }

  }

}
