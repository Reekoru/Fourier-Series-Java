package gui.application;

public class GUI implements Runnable {

  private Panel panel;
  private Frame frame;
  private Thread thread;

  private final int FPS_SET = 120;

  public GUI() {
    panel = new Panel();
    frame = new Frame(panel);
    panel.requestFocus();

    startLoop();
  }

  private void startLoop() {
    thread = new Thread(this);
    thread.start();
  }

  @Override
  public void run() {

    double timePerFrame = 1000000000.0 / FPS_SET;

    // For update / tick
    long prevTime = System.nanoTime();

    // Check fps
    int frame = 0;
    int updates = 0;
    long lastCheck = System.currentTimeMillis();

    double deltaFrame = 0;

    while (true) {
      long currentTime = System.nanoTime();

      deltaFrame += (currentTime - prevTime) / timePerFrame;
      prevTime = currentTime;

      if (deltaFrame >= 1) {
        panel.repaint();
        frame++;
        deltaFrame--;
      }

      // Check fps
      if (System.currentTimeMillis() - lastCheck >= 1000) {
        lastCheck = System.currentTimeMillis();
        System.out.println("FPS: " + frame + "| UPS: " + updates);
        frame = 0;
        updates = 0;
      }
    }
  }
}
