package system.controller;

import java.awt.*;
import java.awt.event.*;

public class CursorController {
	private Robot robot;
	private int width;
	private int height;

	public CursorController() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle desktop_size = env.getMaximumWindowBounds();
		width = desktop_size.width;
		height = desktop_size.height;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public boolean moveCursor(int cursorX, int cursorY) {
		if (cursorX < 0) {
			cursorX = 0;
		} else if (width < cursorX) {
			cursorX = width;
		}
		if (cursorY < 0) {
			cursorY = 0;
		} else if (height < cursorY) {
			cursorY = height;
		}
		robot.mouseMove(cursorX, cursorY);
		return true;
	}

	public boolean moveWheel(int wheel) {
		robot.mouseWheel(wheel);
		return true;
	}

	public boolean click_leftMouse(int c) {
		for (int i = 0; i < c; i++) {
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		}
		return true;
	}

	public boolean click_rightMouse(int c) {
		for (int i = 0; i < c; i++) {
			robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
		}
		return true;
	}

	public String toString() {
		return ("screen size| width : " + width
				+ "             height: " + height);
	}
}
