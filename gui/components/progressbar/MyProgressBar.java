package gui.components.progressbar;

import javax.swing.*;
import java.awt.*;

public class MyProgressBar extends JProgressBar{
  public static void setModel(int n) {
    String className = "javax.swing.plaf.metal.MetalLookAndFeel";
    switch (n) {
      case 0:
        className = "javax.swing.plaf.metal.MetalLookAndFeel";
        break;
      case 1:
        className = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
        break;
    }
    try {
      UIManager.setLookAndFeel(className);
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
  }

  public MyProgressBar(Color foreground, Color background) {
    this.setForeground(foreground);
    this.setBackground(background);
  }
}
