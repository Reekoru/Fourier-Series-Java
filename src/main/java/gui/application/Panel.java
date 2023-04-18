package gui.application;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Panel extends JPanel {

  // Set panel dimension
  int width = 1280;
  int height = 720;

  double radius = 150 * (4 / (1 * Math.PI));
  double pointRadius = 10;
  double period = 0.05;

  // Initialize variables //
  int originX = (int) radius / 4;
  int originY = height / 2 - (int) radius;
  int x = 0;
  int y = 0;

  // Where the points will start drawing
  int pointStartX = (int) (originX + radius * 2 + 100);

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

    time += 0.01;
    x = 0;
    y = 0;

    // Update position of point
    x = (int) (radius * Math.sin(1 * time) + originX + radius - pointRadius / 2);
    y = (int) (radius * Math.cos(1 * time) + originY + radius - pointRadius / 2);

    // Add points of wave
    wave.add(0, (int) (y + pointRadius / 2));

    g2d.setStroke(new BasicStroke(1));
    g2d.setPaint(Color.white);
    g2d.drawOval(originX, originY, (int) radius * 2, (int) radius * 2);

    // Rotating oval
    g2d.fillOval(x, y, (int) pointRadius, (int) pointRadius);

    // Draw point at y
    for (int i = 0; i < wave.size(); i++) {
      g2d.drawLine((int) (i + pointStartX), wave.get(i), (int) (i + pointStartX), wave.get(i));
    }

    g2d.drawLine(
        (int) (x + pointRadius / 2),
        (int) (y + pointRadius / 2),
        pointStartX,
        (int) (y + pointRadius / 2));

    // Remove out of window points
    if (wave.size() > this.getWidth() - radius * 2) {
      wave.remove(wave.size() - 1);
    }
  }
}
