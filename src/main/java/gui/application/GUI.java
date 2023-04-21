package gui.application;

import javax.swing.JPanel;

public class GUI implements Runnable {
  private JPanel panel;
  private Frame frames;
  private Thread thread;

  private final int FPS_SET = 120;
  private final int UPS_SET = 200;

  public GUI() {
    /*
     * Comment and uncomment panel for different panels
     */

    ///////// START /////////

    // panel = new Panel();
    // panel = new FourierTransformPanel();
    panel = new PathTransform();

    ////////// END //////////

    frames = new Frame(panel, " Fourier Transform");

    panel.requestFocus();

    startLoop();
  }

  private void startLoop() {
    thread = new Thread(this);
    thread.start();
  }

  public void updates() {
    ((PathTransform) panel).updateApp();
  }

  @Override
  public void run() {

    double timePerFrame = 1000000000.0 / FPS_SET;
    double timePerUpdate = 1000000000.0 / UPS_SET;

    // For updates / tick
    long prevTime = System.nanoTime();

    // Check fps
    int frames = 0;
    int updates = 0;
    long lastCheck = System.currentTimeMillis();

    double deltaFrame = 0;
    double deltaUpdate = 0;

    // Repaint loop
    while (true) {
      long currentTime = System.nanoTime();

      deltaFrame += (currentTime - prevTime) / timePerFrame;
      deltaUpdate += (currentTime - prevTime) / timePerUpdate;

      if (deltaUpdate >= 1) {
        updates();
        updates++;

        // Reset frames with layover subframes for next iteration
        deltaUpdate--;
      }
      prevTime = currentTime;

      if (deltaFrame >= 1) {
        panel.repaint();
        frames++;

        // Reset frames with layover subframes for next iteration
        deltaFrame--;
      }

      // Check fps
      if (System.currentTimeMillis() - lastCheck >= 1000) {
        lastCheck = System.currentTimeMillis();
        System.out.println("FPS: " + frames + "| UPS: " + updates);
        frames = 0;
        updates = 0;
      }
    }
  }
}
