package net.andrewhatch.gfx.lib;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class DrawableString extends DrawableElement {

  protected String string;
  protected Font font;
  protected FontMetrics font_metrics;

  public DrawableString(final String s) {
    super();
    this.string = s;
  }

  public void draw(final Graphics2D gfx, final long tick_count) {
    gfx.setColor(colour);
    gfx.setFont(font);
    if (font_metrics == null) {
      font_metrics = gfx.getFontMetrics();
    }

    gfx.setComposite(getAlphaComposite());

    gfx.drawString(string, x, y);
  }

  public Font getFont() {
    return font;
  }

  public void setFont(final Font font) {
    this.font = font;
  }

  public String getString() {
    return string;
  }

  public void setString(final String s) {
    this.string = s;
  }

}
