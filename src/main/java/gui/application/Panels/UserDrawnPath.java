package gui.application.Panels;

import gui.application.Functions.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.*;

public class UserDrawnPath extends JPanel implements MouseListener, MouseMotionListener {
  // Set panel dimension
  final int WIDTH = 1280;
  final int HEIGHT = 720;

  // Initialize variables //
  double time = 0;
  int pointStartX = 500;
  Epicycles epicycles;

  Double[][] fourierPointsY;
  Double[][] fourierPointsX;

  int xOffset1 = WIDTH / 7;
  int yOffset1 = HEIGHT / 2;

  // Set position of X component of signal
  Double x1;
  Double y1;

  int xOffset2 = WIDTH / 2;
  int yOffset2 = HEIGHT / 7;

  // Set location of Y component of signal
  Double x2;
  Double y2;

  ArrayList<Double> signalY;
  ArrayList<Double> signalX;

  // Whether in drawing mode or idle mode
  enum State {
    DRAW,
    IDLE
  };

  State state;

  // How many points skipped in drawiwng
  int skip = 1;

  // Holds x y vector
  ArrayList<Double[][]> path = new ArrayList<>();
  ArrayList<ArrayList<Double>> drawnPath = new ArrayList<>();

  public UserDrawnPath() {
    System.out.println("Panel: Path Transfrom \n");
    addMouseListener(this);
    addMouseMotionListener(this);
    // Set panel properties
    this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    this.setBackground(Color.black);

    x1 = 0.0;
    y1 = 0.0;
    x2 = 200.0;
    y2 = 0.0;

    signalX = new ArrayList<>();
    signalY = new ArrayList<>();

    // Initialize size of array
    fourierPointsY = new Double[signalY.size()][];
    fourierPointsX = new Double[signalX.size()][];

    // Calculate fourier transform of signal
    DFT dft = new DFT();
    fourierPointsY = dft.calculateDFT(signalY);
    fourierPointsX = dft.calculateDFT(signalX);

    // To generate epicycles
    epicycles = new Epicycles();

    updateFourier();
    state = State.IDLE;
  }

  public void paint(Graphics g) {
    // Paint background
    super.paint(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

    // Change stroke for circles
    g2d.setStroke(new BasicStroke(2));
    g2d.setPaint(new Color(1, 1, 1, 0.1f));

    // Clear path when drawing is done
    if (path.size() > fourierPointsY.length) {
      path.clear();
    }

    // Set position of epicycle
    y1 = Double.valueOf(this.getHeight() / 10);
    x2 = Double.valueOf(this.getWidth() / 10);

    switch (state) {
      case IDLE:

        // Get X and Y
        Double[] vectorX = epicycles.draw(g2d, x1, y1, 0.0, fourierPointsX, time, true);
        Double[] vectorY = epicycles.draw(g2d, x2, y2, Math.PI / 2, fourierPointsY, time, true);

        Double[][] components = {vectorX, vectorY};

        path.add(0, components);

        // Draw line path of x and y epicycles
        g2d.draw(new Line2D.Double(vectorX[0], vectorX[1], vectorX[0], vectorY[1]));
        g2d.draw(new Line2D.Double(vectorY[0], vectorY[1], vectorX[0], vectorY[1]));

        // Stroke for vertices
        g2d.setPaint(Color.white);

        // Draw point at y
        for (int i = 0; i < path.size() - 1; i += 1) {
          g2d.draw(
              new Line2D.Double(
                  path.get(i)[0][0],
                  path.get(i)[1][1],
                  path.get(i + 1)[0][0],
                  path.get(i + 1)[1][1]));
        }
        break;

      case DRAW:
        g2d.setPaint(Color.white);
        System.out.println(signalX.get(signalX.size() - 1));
        if (isMousePressed) {
          signalX.add(signalX.get(signalX.size() - 1));
          signalY.add(signalY.get(signalY.size() - 1));
        }

        for (int i = 0; i < drawnPath.size() - 1; i++) {
          g2d.draw(
              new Line2D.Double(
                  drawnPath.get(i).get(0),
                  drawnPath.get(i).get(1),
                  drawnPath.get(i).get(0),
                  drawnPath.get(i).get(1)));
        }

        break;
    }
    final double dt = Math.PI * 2 / fourierPointsY.length;
    time += dt;

    if (time >= 2 * Math.PI) {
      time = 0;
    }
  }

  public void updateFourier() {
    path.clear();
    ArrayList<Double> SignalXReduced = new ArrayList<>();
    ArrayList<Double> SignalYReduced = new ArrayList<>();

    for (int i = 0; i < signalX.size(); i += skip) {
      SignalXReduced.add(signalX.get(i));
    }
    for (int i = 0; i < signalY.size(); i += skip) {
      SignalYReduced.add(signalY.get(i));
    }
    // Initialize size of array
    fourierPointsY = new Double[SignalYReduced.size()][];
    fourierPointsX = new Double[SignalXReduced.size()][];

    // Calculate fourier transform of signal
    DFT dft = new DFT();
    fourierPointsY = dft.calculateDFT(SignalYReduced);
    fourierPointsX = dft.calculateDFT(SignalXReduced);
  }

  ///////////////////////////////////////////////////////
  //////////////// MOUSE EVENTS /////////////////////////
  ///////////////////////////////////////////////////////

  boolean isMousePressed = false;

  @Override
  public void mouseClicked(MouseEvent e) {}

  @Override
  public void mousePressed(MouseEvent e) {
    signalX.clear();
    signalY.clear();
    drawnPath.clear();
    isMousePressed = true;
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    isMousePressed = false;
    time = 0;
    state = State.IDLE;

    updateFourier();
  }

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

  @Override
  public void mouseDragged(MouseEvent e) {
    int mouseX = e.getX();
    int mouseY = e.getY();

    state = State.DRAW;
    signalX.add(Double.valueOf(mouseX));
    signalY.add(Double.valueOf(mouseY));

    drawnPath.add(
        new ArrayList<Double>() {
          {
            add(Double.valueOf(mouseX));
            add(Double.valueOf(mouseY));
          }
        });
  }

  @Override
  public void mouseMoved(MouseEvent e) {}
}
