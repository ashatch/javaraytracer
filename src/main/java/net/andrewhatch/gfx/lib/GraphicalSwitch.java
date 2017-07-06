package net.andrewhatch.gfx.lib;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

public class GraphicalSwitch extends DrawableElement {

	Map<String, DrawableElement> items;
	DrawableElement current_item;
	String current_item_name;
	
	public GraphicalSwitch() {
		items = new HashMap<String, DrawableElement>();
	}
	
	public void add(String name, DrawableElement el) {
		this.items.put(name, el);
	}
	
	public void setCurrent(String name) {
		current_item_name = name;
		current_item = items.get(name);
	}
	
	public void draw(Graphics2D gfx, long tick_count) {
		current_item.draw(gfx, tick_count);
	}

}
