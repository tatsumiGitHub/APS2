import gui.AutomaticPlaybackSystemFrame_2;

public class Main {
	public static void main(String[] args) {
		AutomaticPlaybackSystemFrame_2 automatic_playback_system_frame = new AutomaticPlaybackSystemFrame_2(
				"AutomaticPlaybackSystem_2_GUI", "./setup.config");

		automatic_playback_system_frame.setVisible(true);
		automatic_playback_system_frame.setResizable(false);
	}
}