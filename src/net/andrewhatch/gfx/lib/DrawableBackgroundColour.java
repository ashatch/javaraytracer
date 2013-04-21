package net.andrewhatch.gfx.lib;

import java.awt.Graphics2D;

public class DrawableBackgroundColour extends DrawableElement {
	
	protected int width;
	protected int height;
	
	public DrawableBackgroundColour(int w, int h) {
		super();
		this.width = w;
		this.height = h;
	}
	
	public void draw(Graphics2D gfx, long tick_count) {
		gfx.setComposite(getAlphaComposite());
		gfx.setColor(colour);
		gfx.fillRect(0, 0, width, height);
	}

}
