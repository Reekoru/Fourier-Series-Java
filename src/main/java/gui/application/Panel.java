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
  ArrayList<Integer> xPoints = new ArrayList<Integer>();
  ArrayList<Integer> yPoints = new ArrayList<Integer>();

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
    double radius = 150 * (4 / (1 * Math.PI));

    // Transfrom origin
    xOffset = this.getWidth() / 7;
    yOffset = this.getHeight() / 2;

    // Translate circle to height center
    g2d.translate(xOffset, yOffset);

    x = 0;
    y = 0;
    radius = r * (4 / (1 * Math.PI));

    for (int i = 0; i < 4; i++) {
      int prevRadius = (int) radius;
      int prevX = x;
      int prevY = y;

      xPoints.add(prevX);
      yPoints.add(prevY);

      int n = i * 2 + 1;

      // Update x, y position and radius
      radius = r * (4 / (n * Math.PI));

      x += (int) (radius * Math.cos(n * time) + radius);
      y += (int) (radius * Math.sin(n * time) + radius);

      g2d.setStroke(new BasicStroke(1));
      g2d.setPaint(Color.white);

      // Draw circle
      if (i < 1) {
        g2d.drawOval(prevX - prevRadius, prevY - prevRadius, (int) radius * 2, (int) radius * 2);

        g2d.drawLine((prevX), (prevY), (int) (x - radius), (int) (y - radius));
      }
      // Draw circle
      else {
        g2d.drawOval(
            prevX - prevRadius - (int) (radius),
            prevY - prevRadius - (int) (radius),
            (int) radius * 2,
            (int) radius * 2);

        g2d.drawLine(
            (prevX - prevRadius), (prevY - prevRadius), (int) (x - radius), (int) (y - radius));

        if (i == 2) {
          System.out.print("prevX: " + prevX);
          System.out.print("\t prevRadius: " + prevRadius + "\n");
        }
      }

      if (i > 4) {
        System.out.println("PrevX: " + prevX);
        // System.out.println("CircleCenterX: " + circleCenterX);;
      }
    }

    // Add points of wave
    wave.add(0, (int) (y + pointRadius / 2 - radius));

    g2d.drawLine(
        (int) (x + pointRadius / 2),
        (int) (y + pointRadius / 2),
        pointStartX,
        (int) (y + pointRadius / 2));

    // Draw point at y
    for (int i = 0; i < wave.size(); i++) {
      g2d.drawLine((int) (i + pointStartX), wave.get(i), (int) (i + pointStartX), wave.get(i));
    }

    for (int i = 0; i < xPoints.size(); i++) {
      g2d.drawLine(xPoints.get(i), yPoints.get(i), xPoints.get(i), yPoints.get(i));
    }

    // Remove out of window points
    if (wave.size() > this.getWidth() - radius * 2) {
      wave.remove(wave.size() - 1);
    }

    time -= 0.02;
  }
}
