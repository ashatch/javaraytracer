package net.andrewhatch.gfx.lib;

import java.awt.Graphics2D;

public class DrawableRectangle extends DrawableElement {
  private int height;
  private int width;
  private int width_half;
  private int height_half;

  public DrawableRectangle(final int width, final int height) {
    this.width = width;
    this.height = height;

    width_half = width / 2;
    height_half = height / 2;
  }

  public void draw(final Graphics2D gfx, final long tick_count) {
    gfx.drawRect(x - width_half, y - height_half, width, height);
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
    height_half = this.height / 2;
  }

  public int getWidth() {
    return width;
  }

  public int getWidth_half() {
    return width_half;
  }
}
