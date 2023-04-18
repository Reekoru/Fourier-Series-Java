package gui.application;

import javax.swing.JFrame;

public class Frame extends JFrame {
    Panel panel;

    public Frame(Panel panel){

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Graphics 2D");
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

}
