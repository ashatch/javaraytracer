package net.andrewhatch.gfx.raytracer.scene.geometry;

import net.andrewhatch.gfx.raytracer.scene.core.Point;
import net.andrewhatch.gfx.raytracer.scene.core.Vector;
import net.andrewhatch.gfx.raytracer.scene.optics.Colour;
import net.andrewhatch.gfx.raytracer.scene.optics.OpticalProperties;

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
    int x = (int) Math.ceil(p.getX() * cos_inv);
    int y = (int) Math.ceil(p.getY());
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
