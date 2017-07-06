package net.andrewhatch.gfx.lib;

import java.awt.Dimension;

public class Pixels {

  // 0xAARRGGBB
  private int[] pixels;
  private int width;
  private int height;

  public Pixels(final int width, final int height) {
    this(new int[width * height], width, height);
  }

  public Pixels(final int[] pixels, final int w, final int h) {
    this.pixels = pixels;
    this.width = w;
    this.height = h;
  }

  public static int rgbToInt(final int r, final int g, final int b) {
    return (r << 24) | (g << 16) | b;
  }

  public void setRGB(final int x, final int y, final int r, final int g, final int b) {
    final int index = x + (y * width);
    pixels[index] = rgbToInt(r, g, b);
  }

  public int getPixel(final int x, final int y) {
    final int index = x + (y * width);
    return pixels[index];
  }

  public int getHeight() {
    return height;
  }

  public int[] getPixels() {
    return pixels;
  }

  public int getWidth() {
    return width;
  }

  public String toString() {
    return width + "," + height;
  }

  public Dimension getSize() {
    return new Dimension(width, height);
  }

  public void setPixel(final int x, final int y, final int c) {
    pixels[(y * width) + x] = c;
  }

}
