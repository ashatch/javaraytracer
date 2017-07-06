package net.andrewhatch.gfx.lib;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DrawableStack extends DrawableElement {

  protected final List<DrawableElement> stack;
  protected final Map<String, DrawableElement> name_stack;

  public DrawableStack() {
    stack = new ArrayList<DrawableElement>();
    name_stack = new HashMap<String, DrawableElement>();
  }

  public void pushElement(final String name, final DrawableElement el) {
    if (name_stack.containsKey(name)) {
      final Object removed = name_stack.remove(name);
      final int indx = stack.indexOf(removed);
      stack.remove(indx);
    }

    stack.add(el);
    name_stack.put(name, el);
  }

  public DrawableElement getElement(final String name) {
    return name_stack.get(name);
  }

  public void draw(final Graphics2D gfx, final long tick_count) {
    Iterator<DrawableElement> i = stack.iterator();
    DrawableElement de;
    while (i.hasNext()) {
      de = i.next();
      de.draw(gfx, tick_count);
    }
  }

  public String toString() {
    final StringBuffer sb = new StringBuffer();
    final Iterator<DrawableElement> i = stack.iterator();
    DrawableElement de;
    while (i.hasNext()) {
      de = i.next();
      sb.append(de.toString() + ",");
    }
    return sb.toString();
  }

}
