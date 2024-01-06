package system.comparator;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.util.Random;

import system.controller.CursorController;

public class CaptureComparator {
	private Robot robot;
	private Rectangle screen_size;
	private int width;
	private int height;

	public CaptureComparator() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		screen_size = env.getMaximumWindowBounds();
		width = screen_size.width;
		height = screen_size.height;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public double coincidenceRatio(int interval) {
		double ratio = 0.0;
		double pixel = (width * height) * 3;

		int coincidence = 0;

		Color color1;
		Color color2;

		try {
			BufferedImage img1 = robot.createScreenCapture(screen_size);
			Thread.sleep(interval);
			BufferedImage img2 = robot.createScreenCapture(screen_size);

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					color1 = new Color(img1.getRGB(i, j));
					color2 = new Color(img2.getRGB(i, j));
					if (color1.getRed() == color2.getRed()) {
						coincidence++;
					}
					if (color1.getGreen() == color2.getGreen()) {
						coincidence++;
					}
					if (color1.getBlue() == color2.getBlue()) {
						coincidence++;
					}
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Interrupted code: 40101");
			e.printStackTrace();
		}
		ratio = coincidence / pixel;
		return ratio;
	}

	public double coincidenceRatio_move_cursor(int interval, int cursorX, int cursorY, int rand_scale) {
		Random rand = new Random();
		CursorController cursor = new CursorController();

		double ratio = 0.0;
		double pixel = (width * height) * 3;

		int coincidence = 0;

		Color color1;
		Color color2;

		try {
			cursor.moveCursor(cursorX + (rand_scale / 2 - rand.nextInt(rand_scale) ), cursorY + (rand_scale / 2 - rand.nextInt(rand_scale) ) );
			BufferedImage img1 = robot.createScreenCapture(screen_size);
			Thread.sleep(interval);
			cursor.moveCursor(cursorX + (rand_scale / 2 - rand.nextInt(rand_scale) ), cursorY + (rand_scale / 2 - rand.nextInt(rand_scale) ) );
			BufferedImage img2 = robot.createScreenCapture(screen_size);

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					color1 = new Color(img1.getRGB(i, j));
					color2 = new Color(img2.getRGB(i, j));
					if (color1.getRed() == color2.getRed()) {
						coincidence++;
					}
					if (color1.getGreen() == color2.getGreen()) {
						coincidence++;
					}
					if (color1.getBlue() == color2.getBlue()) {
						coincidence++;
					}
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Interrupted code: 40201");
			return - 1;
		}
		ratio = coincidence / pixel;
		return ratio;
	}
}
