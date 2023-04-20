package gui.application;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.*;

public class FourierTransformPanel extends JPanel {

  // Set panel dimension
  int width = 1280;
  int height = 720;
  int xOffset;
  int yOffset;

  // Initialize variables //
  double x = 0;
  double y = 0;
  int pointStartX = 500;
  ArrayList<Double> yValues = new ArrayList<>(); // Stores y values of series
  double time = 0;

  ArrayList<Hashtable<String, Double>> fourierPointsY = new ArrayList<>();

  public FourierTransformPanel() {
    System.out.println("Panel: Fourier Transform \n\n");

    // Set panel properties
    this.setPreferredSize(new Dimension(width, height));
    this.setBackground(Color.black);

    ArrayList<Double> signal = new ArrayList<>();

    int ind = 0;
    /*for (Double i = 0.0; i < 10; i++) {
      for (Double j = 0.0; j < 100; j++) {
        signal.add(ind, 100.0);
        ind++;
      }
      for (Double h = 0.0; h < 100; h++) {
        signal.add(ind, -100.0);
        ind++;
      }
    }*/

    for (Double i = 0.0; i < 100; i++) {
      signal.add(ind, i);
      ind++;
    }

    System.out.println(signal);

    DFT dft = new DFT();
    fourierPointsY = dft.calculateDFT(signal);
  }

  public void paint(Graphics g) {
    // Paint background
    super.paint(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

    // Transfrom origin depending on window size
    xOffset = this.getWidth() / 7;
    yOffset = this.getHeight() / 2;
    g2d.translate(xOffset, yOffset);

    // Reset x and y values for next repaint
    x = 0;
    y = 0;

    // Change stroke for circles
    g2d.setStroke(new BasicStroke(2));
    g2d.setPaint(new Color(1, 1, 1, 0.25f));

    ////////////////////////
    /////// EPICYCLES //////
    ////////////////////////

    for (int i = 0; i < fourierPointsY.size() - 1; i++) {
      double prevX = x;
      double prevY = y;

      double frequency = fourierPointsY.get(i).get("frequency");
      double phase = fourierPointsY.get(i).get("phase");
      double radius = fourierPointsY.get(i).get("amplitude");

      x += radius * Math.cos(frequency * time + phase + Math.PI / 2);
      y += radius * Math.sin(frequency * time + phase + Math.PI / 2);

      // Draw circle
      g2d.drawOval(
          (int) (prevX - radius), (int) (prevY - radius), (int) radius * 2, (int) radius * 2);
      g2d.drawLine((int) prevX, (int) prevY, (int) x, (int) y);
    }

    // System.out.println("Y: " + y);

    // Add points of yValues
    yValues.add(0, y);

    // Stroke for vertices
    g2d.setPaint(Color.white);

    g2d.drawLine((int) x, (int) y, pointStartX, (int) y);

    // Draw point at y
    for (int i = 0; i < yValues.size() - 1; i += 1) {
      g2d.draw(
          new Line2D.Double(
              i + pointStartX, yValues.get(i), i + 1 + pointStartX, yValues.get(i + 1)));
    }

    // Remove out of window points
    if (yValues.size() > this.getWidth()) {
      yValues.remove(yValues.size() - 1);
    }

    final double dt = Math.PI * 2 / fourierPointsY.size();
    time += dt;
  }
}
