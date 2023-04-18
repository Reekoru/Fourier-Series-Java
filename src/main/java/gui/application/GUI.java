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
    long prevFrame = System.nanoTime();
    long currentFrame = System.nanoTime();

    // Check fps
    int frame = 0;
    long lastCheck = System.currentTimeMillis();

    while (true) {
      currentFrame = System.nanoTime();

      // Check time passed and repaint
      if (currentFrame - prevFrame >= timePerFrame) {
        panel.repaint();
        prevFrame = currentFrame;

        frame++;
      }

      // Check fps
      if (System.currentTimeMillis() - lastCheck >= 1000) {
        lastCheck = System.currentTimeMillis();
        System.out.println("FPS: " + frame);
        frame = 0;
      }
    }
  }
}
