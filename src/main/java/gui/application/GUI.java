package gui.application;

public class GUI implements Runnable {

  /*
   * Uncomment either panel and the panel in the GUI constructor
   */

  ///////// START /////////

  // private Panel panel;
  private FourierTransformPanel panel;

  ////////// END //////////

  private Frame frame;
  private Thread thread;

  private final int FPS_SET = 60;

  public GUI() {
    /*
     * Comment and uncomment panel for different panels
     */

    ///////// START /////////

    // panel = new Panel();
    panel = new FourierTransformPanel();

    ////////// END //////////

    frame = new Frame(panel, " Fourier Transform");

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
    long lastCheck = System.currentTimeMillis();

    double deltaFrame = 0;

    // Repaint loop
    while (true) {
      long currentTime = System.nanoTime();

      deltaFrame += (currentTime - prevTime) / timePerFrame;
      prevTime = currentTime;

      if (deltaFrame >= 1) {
        panel.repaint();
        frame++;

        // Reset frame with layover subframes for next iteration
        deltaFrame--;
      }

      // Check fps
      if (System.currentTimeMillis() - lastCheck >= 1000) {
        lastCheck = System.currentTimeMillis();
        // System.out.println("FPS: " + frame);
        frame = 0;
      }
    }
  }
}
