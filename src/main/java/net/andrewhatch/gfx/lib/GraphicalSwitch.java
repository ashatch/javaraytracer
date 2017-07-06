package net.andrewhatch.gfx.lib;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

public class GraphicalSwitch extends DrawableElement {

  private Map<String, DrawableElement> items;
  private DrawableElement current_item;
  private String current_item_name;

  public GraphicalSwitch() {
    items = new HashMap<String, DrawableElement>();
  }

  public void add(final String name, final DrawableElement el) {
    this.items.put(name, el);
  }

  public void setCurrent(final String name) {
    current_item_name = name;
    current_item = items.get(name);
  }

  public void draw(final Graphics2D gfx, final long tick_count) {
    current_item.draw(gfx, tick_count);
  }

}
