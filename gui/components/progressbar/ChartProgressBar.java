package gui.components.progressbar;

import java.awt.*;

public class ChartProgressBar extends MyProgressBar {
    public ChartProgressBar(Color foreground, Color background,
            int x, int y, int width, int height) {
        super(foreground, background);
        this.setBounds(x, y, width, height);
    }
}
