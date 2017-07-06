package net.andrewhatch.gfx.lib;

import java.awt.Dimension;

public class Pixels {

	// 0xAARRGGBB
	public int[] pixels;
	public int width;
	public int height;
	
	public Pixels(int width, int height) {
		this(new int[width * height], width, height);
	}	
	
	public Pixels(int[] pixels, int w, int h) {
		this.pixels = pixels;
		this.width = w;
		this.height = h;
	}	
	
	public void setRGB(int x, int y, int r, int g, int b) {
		int index = x + (y * width);
		pixels[index] = rgbToInt(r, g, b);
	}
	
	public static int rgbToInt(int r, int g, int b) {
		int c = (r << 24) | (g << 16) | b;
		return c;
	}
	
	public int getPixel(int x, int y) {
		int index = x + (y * width);
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

	public void setPixel(int x, int y, int c) {
		pixels[(y*width) + x] = c;		
	}

}
