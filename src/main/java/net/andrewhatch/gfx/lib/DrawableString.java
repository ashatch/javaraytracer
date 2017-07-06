package net.andrewhatch.gfx.lib;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class DrawableString extends DrawableElement {

	protected String string;
	protected Font font;
	protected FontMetrics font_metrics;
	
	public DrawableString(String s) {
		super();
		this.string = s;
	}	
	
	public void setString(String s) {
		this.string = s;
	}
	
	public void draw(Graphics2D gfx, long tick_count) {
		gfx.setColor(colour);
		gfx.setFont(font);
		if(font_metrics == null)
			font_metrics = gfx.getFontMetrics();
		
		gfx.setComposite(getAlphaComposite());
		
		gfx.drawString(string, x, y);
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public String getString() {
		return string;
	}

}
