package gui.application.Functions;

import java.awt.Graphics2D;

public class Epicycles {
  public Double[] draw(
      Graphics2D g2d, Double x, Double y, Double rotaion, Double[][] fourier, double time) {
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
