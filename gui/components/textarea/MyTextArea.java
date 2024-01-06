package gui.components.textarea;

import javax.swing.*;
import java.awt.*;

public class MyTextArea extends JTextArea {
	public MyTextArea(int size, String text, Color foreground_col, Color background_col) {
		this.setText(text);
		this.setForeground(foreground_col);
		this.setBackground(background_col);
		this.setEditable(false);
		this.setLineWrap(true);
		this.setFont(new Font(Font.DIALOG, Font.BOLD, size));
	}
}
