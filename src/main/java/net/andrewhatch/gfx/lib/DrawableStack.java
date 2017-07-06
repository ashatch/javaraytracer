package net.andrewhatch.gfx.lib;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import java.util.Iterator;

public class DrawableStack extends DrawableElement {

	protected List<DrawableElement> stack;
	protected Map<String, DrawableElement> name_stack;
	
	public DrawableStack() {
		stack = new ArrayList<DrawableElement>();
		name_stack = new HashMap<String, DrawableElement>();
	}
	
	public void pushElement(String name, DrawableElement el) {
		if(name_stack.containsKey(name)) {
			Object removed = name_stack.remove(name);
			int indx = stack.indexOf(removed);
			stack.remove(indx);
		}
		
		stack.add(el);
		name_stack.put(name, el);		
	}
	
	public DrawableElement getElement(String name) {
		return name_stack.get(name);
	}
	
	public void draw(Graphics2D gfx, long tick_count) {
		Iterator<DrawableElement> i = stack.iterator();
		DrawableElement de;
		while(i.hasNext()) {
			de = i.next();
			//System.out.println("Drawing " + de);
			de.draw(gfx, tick_count);
		}
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		Iterator<DrawableElement> i = stack.iterator();
		DrawableElement de;
		while(i.hasNext()) {
			de = i.next();
			sb.append(de.toString() + ",");
		}
		return sb.toString();
	}

}
