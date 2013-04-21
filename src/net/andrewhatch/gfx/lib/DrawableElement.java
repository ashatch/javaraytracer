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

	public DrawableElement() {}
	
	public abstract void draw(Graphics2D gfx, long tick_count);
	

	/*** Utility Methods ***/
	protected Composite getAlphaComposite() {
		return AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
	}
	
	/*** GETTERS AND SETTERS ****/
	
	public float getOpacity() {
		return opacity;
	}

	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public Color getC() {
		return colour;
	}

	public void setC(Color colour) {
		this.colour = colour;
	}	
}
