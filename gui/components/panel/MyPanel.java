package gui.components.panel;

import java.awt.*;
import javax.swing.*;

public class MyPanel extends JPanel {
	private int option = 0;

	public MyPanel(int option, Color col) {
		this.option = option;
		if (col != null) {
			this.setBackground(col);
		}
		this.setLayout(null);
	}

	public void setChart(int x, int y, int width, int height) {
		this.setBounds(x, y, width, height);
		this.setPreferredSize(new Dimension(width, height));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		switch (option) {
			case 1:
				g.drawLine(0, 0, 640, 0);
				g.fillRect(0, 0, 75, 480);
				break;
			case 2:
				g.drawLine(0, 0, 640, 0);
				break;
		}
	}
}