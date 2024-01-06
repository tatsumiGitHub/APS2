package gui.components.filechooser;

import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.accessibility.AccessibleContext;
import javax.swing.*;
import javax.swing.filechooser.*;

public class MyFileChooser extends JFileChooser {
	private Color foreground = new Color(0, 0, 0);
	private Color background = new Color(255, 255, 255);

	public MyFileChooser(String dialog_title, String directory_path, int type) {
		this.setDialogTitle(dialog_title);
		this.setCurrentDirectory(new File(directory_path));
		this.setDialogType(type);
	}

	public void setDetails(String filter_title, String filter, boolean all_filter) {
		FileNameExtensionFilter extension_filter = new FileNameExtensionFilter(filter_title, filter);
		this.addChoosableFileFilter(extension_filter);
		this.setAcceptAllFileFilterUsed(all_filter);
	}

	private void setGUI(Component c) {
		AccessibleContext ac = c.getAccessibleContext();
		setGUI_Accessible(ac);
	}

	private void setGUI_Accessible(AccessibleContext ac) {
		if (foreground != null) {
			ac.getAccessibleComponent().setForeground(foreground);
		}
		if (background != null) {
			ac.getAccessibleComponent().setBackground(background);
		}
		int n = ac.getAccessibleChildrenCount();
		for (int i = 0; i < n; i++) {
			if ((ac.getAccessibleChild(i) instanceof JPanel) || (ac.getAccessibleChild(i) instanceof JLabel)) {
				setGUI_Accessible(ac.getAccessibleChild(i).getAccessibleContext());
			}
		}
	}

	public void setPanelColor(Color foreground, Color background) {
		this.foreground = foreground;
		this.background = background;
	}

	public void paintFileChooser(JFrame frame, JLabel label) {
		setGUI(this);
		int selected = this.showOpenDialog(frame);
		if (selected == JFileChooser.APPROVE_OPTION) {
			Path path = Paths.get("");
			String current_directory_name = path.toAbsolutePath().toString();
			File file = this.getSelectedFile();
			String select_file_path = file.getPath();
			if (select_file_path.contains(current_directory_name)) {
				select_file_path = "." + select_file_path.substring(current_directory_name.length()).replace("\\", "/");
			}
			if (label != null) {
				label.setText(select_file_path);
			}
		}
		System.out.println(label.getText());
	}
}
