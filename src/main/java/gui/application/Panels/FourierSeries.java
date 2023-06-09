package gui.application.Panels;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.*;

public class FourierSeries extends JPanel {

  // Set panel dimension
  int width = 1280;
  int height = 720;
  int xOffset;
  int yOffset;

  // Initialize variables //
  double x = 0;
  double y = 0;
  double r = 100;
  double radius;
  int pointStartX = 500;
  ArrayList<Double> yValues = new ArrayList<>(); // Stores y values of series
  double time = 0;

  public FourierSeries() {
    System.out.println("Panel: Fourier Series \n\n");
    // Set panel properties
    this.setPreferredSize(new Dimension(width, height));
    this.setBackground(Color.black);
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

    for (int i = 0; i < 10; i++) {
      double prevX = x;
      double prevY = y;

      int n = i * 2 + 1;

      // Update x, y position and radius
      radius = r * (4 / (n * Math.PI));
      x += radius * Math.cos(n * time);
      y += radius * Math.sin(n * time);

      // Draw circle
      g2d.drawOval(
          (int) (prevX - radius), (int) (prevY - radius), (int) radius * 2, (int) radius * 2);
      g2d.drawLine((int) prevX, (int) prevY, (int) x, (int) y);
    }

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

    time -= 0.02;

    // Reset time after 1 cycle
    if (Math.abs(time) > Math.PI * 2) {
      time = 0;
    }
  }
}
