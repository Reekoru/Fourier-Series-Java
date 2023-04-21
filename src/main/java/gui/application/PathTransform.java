package gui.application;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.*;

public class PathTransform extends JPanel implements MouseListener, MouseMotionListener {
  // Set panel dimension
  final int WIDTH = 1280;
  final int HEIGHT = 720;

  // Initialize variables //
  double time = 0;
  int pointStartX = 500;

  Double[][] fourierPointsY;
  Double[][] fourierPointsX;

  int xOffset1 = WIDTH / 7;
  int yOffset1 = HEIGHT / 2;

  // Set position of Y component of signal
  Double x1;
  Double y1;

  int xOffset2 = WIDTH / 2;
  int yOffset2 = HEIGHT / 7;

  // Set location of X component of signal
  Double x2;
  Double y2;

  // Holds x y vector
  ArrayList<Double[][]> path = new ArrayList<>();

  public PathTransform() {
    System.out.println("Panel: Path Transfrom \n");
    addMouseListener(this);
    addMouseMotionListener(this);
    // Set panel properties
    this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    this.setBackground(Color.black);

    ArrayList<Double> signalY = new ArrayList<>();
    ArrayList<Double> signalX = new ArrayList<>();

    ReadCSV csv = new ReadCSV();
    ArrayList<ArrayList<Double>> signal =
        csv.getCoordinates("src/main/java/gui/application/drawings/boat.csv");

    signalX = signal.get(0);
    signalY = signal.get(1);

    // Initialize size of array
    fourierPointsY = new Double[signalY.size()][];
    fourierPointsX = new Double[signalX.size()][];

    // Calculate fourier transform of signal
    DFT dft = new DFT();
    fourierPointsY = dft.calculateDFT(signalY);
    fourierPointsX = dft.calculateDFT(signalX);
  }

  public void paint(Graphics g) {
    // Paint background
    super.paint(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

    // Set position of Y component of signal
    x1 = Double.valueOf(xOffset1);
    y1 = Double.valueOf(yOffset1);

    // Set location of X component of signal
    x2 = Double.valueOf(xOffset2);
    y2 = Double.valueOf(yOffset2);

    // Change stroke for circles
    g2d.setStroke(new BasicStroke(2));
    g2d.setPaint(new Color(1, 1, 1, 0.1f));

    // Draw and get vector of epicycle
    Double[] vectorX = drawEpicycle(g2d, x2, y2, 0.0, fourierPointsX);
    Double[] vectorY = drawEpicycle(g2d, x1, y1, Math.PI / 2, fourierPointsY);

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
              path.get(i)[0][0], path.get(i)[1][1], path.get(i + 1)[0][0], path.get(i + 1)[1][1]));
    }

    // Remove out of window points
    if (path.size() > fourierPointsY.length) {
      path.remove(path.size() - 1);
    }

    final double dt = Math.PI * 2 / fourierPointsY.length;
    time += dt;

    if (time >= 2 * Math.PI) {
      time = 0;
    }
  }

  public void updateApp() {}

  public Double[] drawEpicycle(
      Graphics2D g2d, Double x, Double y, Double rotaion, Double[][] fourier) {
    /*
     * This method draws Epicyles of a fourier transform on an x
     */
    for (int i = 0; i < fourier.length - 1; i++) {
      double prevX = x;
      double prevY = y;

      double frequency = fourier[i][2];
      double radius = fourier[i][3];
      double phase = fourier[i][4];

      x += radius * Math.cos(frequency * time + phase + rotaion);
      y += radius * Math.sin(frequency * time + phase + rotaion);

      // Draw circle
      g2d.drawOval(
          (int) (prevX - radius), (int) (prevY - radius), (int) radius * 2, (int) radius * 2);
      g2d.drawLine((int) prevX, (int) prevY, (int) Math.round(x), (int) Math.round(y));
    }

    return new Double[] {x, y};
  }

  boolean isMousePressed = false;

  @Override
  public void mouseClicked(MouseEvent e) {}

  @Override
  public void mousePressed(MouseEvent e) {
    isMousePressed = true;
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    isMousePressed = false;
  }

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

  @Override
  public void mouseDragged(MouseEvent e) {
    int mouseX = e.getX();
    int mouseY = e.getY();
    int hitBoxSize = 50;

    if ((mouseX > xOffset1 - hitBoxSize) && (mouseX < xOffset1 + hitBoxSize)) {
      if (mouseY > yOffset1 - hitBoxSize && mouseY < yOffset1 + hitBoxSize) {
        xOffset1 = mouseX;
        yOffset1 = mouseY;
      }
    }

    if ((mouseX > xOffset2 - hitBoxSize) && (mouseX < xOffset2 + hitBoxSize)) {
      if (mouseY > yOffset2 - hitBoxSize && mouseY < yOffset2 + hitBoxSize) {
        xOffset2 = mouseX;
        yOffset2 = mouseY;
      }
    }
  }

  @Override
  public void mouseMoved(MouseEvent e) {}
}
