package gui.components.button;

import java.awt.*;

public class ChartButton extends MyButton {
	public ChartButton(int size, String text, String cmd, Color col,
			int x, int y, int width, int height) {// Basis Button
		super(size, text, cmd, col);
		this.setBounds(x, y, width, height);
	}
}
