package gui.components.label;

import java.awt.*;

public class ChartLabel extends MyLabel {
    public ChartLabel(int size, String text, Color col,
            int x, int y, int width, int height) {
        super(size, text, col);
        this.setBounds(x, y, width, height);
    }
}
