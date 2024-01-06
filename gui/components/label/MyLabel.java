package gui.components.label;

import javax.swing.*;
import java.awt.*;

public class MyLabel extends JLabel{
  public MyLabel(int size, String text, Color col) {
    this.setForeground(col);
    this.setText(text);
    this.setFont(new Font(Font.DIALOG, Font.BOLD, size));
  }
}
