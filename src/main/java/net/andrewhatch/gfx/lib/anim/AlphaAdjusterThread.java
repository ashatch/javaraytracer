package net.andrewhatch.gfx.lib.anim;

public class AlphaAdjusterThread implements Runnable {
  protected Thread thread;
  protected float durationMillis;

  public AlphaAdjusterThread(final float durationMillis) {
    this.durationMillis = durationMillis;
  }

  public void start() {
    thread = new Thread(this);
    thread.start();
  }

  public void run() {


  }
}
