package net.andrewhatch.gfx.lib.anim;

import net.andrewhatch.gfx.lib.DrawableElement;

public class AlphaAdjusterThread implements Runnable {
	protected Thread t;
	protected float duration_millis;
	
	public AlphaAdjusterThread(DrawableElement de, float duration_millis) {
		this.duration_millis = duration_millis;
	}
	
	public void start() {
		t = new Thread(this);
		t.start();
	}

	public void run() {
		
		
	}
}
