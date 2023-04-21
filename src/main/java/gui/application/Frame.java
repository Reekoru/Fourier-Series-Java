package gui.application;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JFrame {
  JPanel panel;

  public Frame(JPanel panel, String title) {

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle(title);
    this.add(panel);
    this.pack();
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }
}
