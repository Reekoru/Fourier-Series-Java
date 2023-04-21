package gui.application;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.*;

public class FourierTransformPanel extends JPanel {

  // Set panel dimension
  int width = 1280;
  int height = 720;
  int xOffset;
  int yOffset;

  // Initialize variables //
  double time = 0;
  int pointStartX = 500;
  ArrayList<Double> yValues = new ArrayList<>(); // Stores y values of series

  Double[][] fourierPointsY;

  public FourierTransformPanel() {
    System.out.println("Panel: Fourier Transform \n\n");

    // Set panel properties
    this.setPreferredSize(new Dimension(width, height));
    this.setBackground(Color.black);

    ArrayList<Double> signalY = new ArrayList<>();

    // Generate signal
    /*for (Double i = 0.0; i < 800.204; i++) {
      signal.add(100 * Math.sin(i * 0.05) + 253 * Math.sin(i * 0.02));
    }*/

    for (Double i = 0.0; i < Math.PI * 100; i++) {
      signalY.add(100 * Math.sin(i * 0.1));
    }

    // Initialize size of array
    fourierPointsY = new Double[signalY.size()][];

    DFT dft = new DFT();
    fourierPointsY = dft.calculateDFT(signalY);
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
    // g2d.translate(xOffset, yOffset);

    // Reset x and y values for next repaint
    Double x = 0.0;
    Double y = 0.0;

    // Change stroke for circles
    g2d.setStroke(new BasicStroke(2));
    g2d.setPaint(new Color(1, 1, 1, 0.25f));

    Double[] vectorY =
        drawEpicycle(g2d, 200.0, Double.valueOf(this.getHeight() / 2), Math.PI / 2, fourierPointsY);

    y = vectorY[1];

    yValues.add(0, y);

    // Stroke for vertices
    g2d.setPaint(Color.white);

    // Draw line from last circle to points
    // g2d.drawLine((int) Math.round(x), (int) Math.round(y), pointStartX, (int) Math.round(y));

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

    final double dt = Math.PI * 2 / fourierPointsY.length;
    time += dt;

    if (time >= 2 * Math.PI) {
      time = 0;
    }
  }

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
}
