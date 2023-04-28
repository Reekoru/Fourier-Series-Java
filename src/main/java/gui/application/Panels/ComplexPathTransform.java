package gui.application.Panels;

import gui.application.Functions.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.*;

public class ComplexPathTransform extends JPanel implements MouseListener, MouseMotionListener {
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

  public ComplexPathTransform() {
    System.out.println("Panel: Complex Path Transform \n");
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

    // Store signal X and signal Y
    signalX = signal.get(0);
    signalY = signal.get(1);

    // Initialize size of array
    fourierPointsY = new Double[signalY.size()][];
    fourierPointsX = new Double[signalX.size()][];

    // Calculate fourier transform of signal
    DFT dft = new DFT();
    fourierPointsY = dft.calculateDFT(signalY);
    fourierPointsX = dft.calculateDFT(signalX);

    // To generate epicycles
    epicycles = new Epicycles();
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

    // Clear path when drawing is done
    if (path.size() >= fourierPointsY.length) {
      path.clear();
    }

    // Change stroke for circles
    g2d.setStroke(new BasicStroke(2));
    g2d.setPaint(new Color(1, 1, 1, 0.1f));

    Double[] vectorX = epicycles.draw(g2d, x2, y2, 0.0, fourierPointsX, time);
    Double[] vectorY = epicycles.draw(g2d, x1, y1, Math.PI / 2, fourierPointsY, time);
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

    final double dt = Math.PI * 2 / fourierPointsY.length;
    time += dt;

    if (time >= 2 * Math.PI) {
      time = 0;
    }
  }

  ///////////////////////////////////////////////////////
  //////////////// MOUSE EVENTS /////////////////////////
  ///////////////////////////////////////////////////////

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
