package gui.application;

import gui.application.Panels.*;
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

    // panel = new FourierSeries();
    // panel = new FourierTransform();
    // panel = new PathTransform();
    // panel = new UserDrawnPath();
    panel = new ComplexPathTransform();

    ////////// END //////////

    frames = new Frame(panel, " Fourier Transform");

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

    // For updates / tick
    long prevTime = System.nanoTime();

    // Check fps
    int frames = 0;
    long lastCheck = System.currentTimeMillis();

    double deltaFrame = 0;

    // Repaint loop
    while (true) {
      long currentTime = System.nanoTime();

      deltaFrame += (currentTime - prevTime) / timePerFrame;
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
        System.out.println("FPS: " + frames);
        frames = 0;
      }
    }
  }
}
