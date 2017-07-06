package net.andrewhatch.gfx.lib;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

public abstract class DrawableElement {

  protected float opacity = 1.0f;
  protected int x;
  protected int y;
  protected int z;
  protected Color colour;

  public DrawableElement() {
  }

  public abstract void draw(Graphics2D gfx, long tick_count);

  protected Composite getAlphaComposite() {
    return AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
  }

  public float getOpacity() {
    return opacity;
  }

  public void setOpacity(final float opacity) {
    this.opacity = opacity;
  }

  public int getX() {
    return x;
  }

  public void setX(final int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(final int y) {
    this.y = y;
  }

  public int getZ() {
    return z;
  }

  public void setZ(final int z) {
    this.z = z;
  }

  public Color getColour() {
    return colour;
  }

  public void setColour(final Color colour) {
    this.colour = colour;
  }
}
