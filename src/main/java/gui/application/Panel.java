package gui.application;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Panel extends JPanel {

  // Set panel dimension
  int width = 1280;
  int height = 720;
  int xOffset;
  int yOffset;

  int r = 70;
  double radius = r * (4 / (1 * Math.PI));
  double pointRadius = 10;
  double period = 0.05;

  // Initialize variables //
  int x = 0;
  int y = 0;

  // Where the points will start drawing
  int pointStartX = (int) (radius * 2 + 100);

  // Stores y value for wave
  ArrayList<Integer> wave = new ArrayList<Integer>();

  // This variable will count how many frames we have gone through
  double time = 0;

  public Panel() {
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

    // Transfrom origin
    xOffset = this.getWidth() / 7;
    yOffset = this.getHeight() / 2;

    // Translate circle to height center
    g2d.translate(xOffset, yOffset);

    x = 0;
    y = 0;

    for (int i = 0; i < 10; i++) {
      int prevX = x;
      int prevY = y;

      int n = i * 2 + 1;

      // Update x, y position and radius
      radius = r * (4 / (n * Math.PI));

      x += (int) (radius * Math.cos(n * time));
      y += (int) (radius * Math.sin(n * time));

      g2d.setStroke(new BasicStroke(1));
      g2d.setPaint(Color.white);

      // Draw circle
      g2d.drawOval(prevX - (int) radius, prevY - (int) radius, (int) radius * 2, (int) radius * 2);
      g2d.drawLine((prevX), (prevY), (int) (x), (int) (y));
    }

    // Add points of wave
    wave.add(0, y);

    g2d.drawLine((int) (x), (int) (y), pointStartX, (int) (y));

    // Draw point at y
    for (int i = 0; i < wave.size() - 1; i++) {
      g2d.drawLine(
          (int) (i + pointStartX), wave.get(i), (int) (i + 1 + pointStartX), wave.get(i + 1));
    }

    // Remove out of window points
    if (wave.size() > this.getWidth()) {
      wave.remove(wave.size() - 1);
    }

    time -= 0.02;
  }
}
