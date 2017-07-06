package net.andrewhatch.gfx.raytracer.scene.geometry;

import net.andrewhatch.gfx.raytracer.scene.Colour;
import net.andrewhatch.gfx.raytracer.scene.OpticalProperties;
import net.andrewhatch.gfx.raytracer.scene.Point;
import net.andrewhatch.gfx.raytracer.scene.Vector;

public class TexturedPlane extends Plane {

  // the tile
  int pixels[];
  int width;
  int height;

  double cos_inv;

  public TexturedPlane(Point center, Vector perpendicular, OpticalProperties prop, int pixels[], int width, int height) {
    super(center, perpendicular, prop);
    this.width = width;
    this.height = height;
    this.pixels = pixels;
  }

  public Colour getPixel(Point p) {
    int x = (int) Math.ceil(p.x * cos_inv);
    int y = (int) Math.ceil(p.y);
    x %= width;
    y %= height;

    if (x < 0) {
      x += width;
    }
    if (y < 0) {
      y += height;
    }

    int rgb = pixels[y * width + x];
    return new Colour(rgb);
  }

  public String toString() {
    return "TEXTUREDPLANE";
  }
}
