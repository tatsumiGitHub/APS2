package system;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigReader_APS {
	private String user_name = null;
	private int window_width = 480;
	private int window_height = 320;
	private int loop = 1;
	private int interval = 1000;
	private String file_name = "./src/default.dat";
	private boolean close = false;
	private int closeX = 0;
	private int closeY = 0;
	private int close_click_count = 1;
	private boolean notification = false;
	private String social_media = null;
	private String access_token = null;

	public ConfigReader_APS(String config_file_name) {
		if (Files.notExists(Paths.get("./setup.config"))) {
			try {
				FileWriter fw = new FileWriter(new File("./setup.config"));

				fw.write("UserName  hoge\n" +
						" width    480\n" +
						" height   320\n" +
						" loop     1\n" +
						" interval 2000\n" +
						" path     ./script/sample1.dat\n" +
						" close    yes\n" +
						"   close-button-position 1530,5,1\n" +
						" Notification    no\n" +
						"   social_media  LINE\n" +
						"   # access_token LINE_NOTIFY_TOKEN");
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			BufferedReader setup_file = new BufferedReader(new FileReader(config_file_name));
			String line;
			while ((line = setup_file.readLine()) != null) {
				line = line.trim();
				if (line.charAt(0) != '#') {
					while (line.replace("  ", " ") != line) {
						line = line.replace("  ", " ");
					}
					String[] token = (line.trim()).split(" ");
					if (token.length == 2) {
						switch (token[0]) {
							case "UserName":
								user_name = token[1];
								break;
							case "width":
								window_width = Integer.parseInt(token[1]);
								if (window_width < 0) {
									System.out.println("Error: Invalid value was obtained\n"
											+ "Int  : window_width\n"
											+ "value: " + window_width);
									System.exit(1);
								}
								break;
							case "height":
								window_height = Integer.parseInt(token[1]);
								if (window_height < 0) {
									System.out.println("Error: Invalid value was obtained\n"
											+ "Int  : window_height\n"
											+ "value: " + window_height);
									System.exit(1);
								}
								break;
							case "loop":
								loop = Integer.parseInt(token[1]);
								if (loop < 0) {
									System.out.println("Error: Invalid value was obtained\n"
											+ "Int  : loop\n"
											+ "value: " + loop);
									System.exit(1);
								}
								break;
							case "interval":
								interval = Integer.parseInt(token[1]);
								if (interval < 0) {
									System.out.println("Error: Invalid value was obtained\n"
											+ "Int  : interval\n"
											+ "value: " + interval);
									System.exit(1);
								}
								break;
							case "path":
								if ((new File(token[1])).exists() == true) {
									file_name = token[1];
								}
								break;
							case "close":
								if (token[1].equals("yes")) {
									close = true;
								}
								break;
							case "close-button-position":
								String[] tmp = token[1].split(",");
								closeX = Integer.parseInt(tmp[0]);
								closeY = Integer.parseInt(tmp[1]);
								close_click_count = Integer.parseInt(tmp[2]);
								if (closeX < 0) {
									System.out.println("Error: Invalid value was obtained\n"
											+ "Int  : closeX\n"
											+ "value: " + closeX);
									System.exit(1);
								}
								if (closeY < 0) {
									System.out.println("Error: Invalid value was obtained\n"
											+ "Int  : closeY\n"
											+ "value: " + closeY);
									System.exit(1);
								}
								if (close_click_count < 0) {
									System.out.println("Error: Invalid value was obtained\n"
											+ "Int  : close_click_count\n"
											+ "value: " + close_click_count);
									System.exit(1);
								}
								break;
							case "Notification":
								if (token[1].equals("yes")) {
									notification = true;
								}
								break;
							case "social_media":
								if (token[1] != null) {
									social_media = token[1];
								}
								break;
							case "access_token":
								if (token[1] != null) {
									access_token = token[1];
								}
								break;
							default:
								System.out.println("Error: undifined token [" + token[0] + "]");
								System.exit(1);
								break;
						}
					}
				}
			}
			setup_file.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public String getUserName() {
		return user_name;
	}

	public int getWidthValue() {
		return window_width;
	}

	public int getHeightValue() {
		return window_height;
	}

	public int getLoopValue() {
		return loop;
	}

	public int getIntervalValue() {
		return interval;
	}

	public String getFileName() {
		return file_name;
	}

	public boolean getCloseValue() {
		return close;
	}

	public int getCloseXValue() {
		return closeX;
	}

	public int getCloseYValue() {
		return closeY;
	}

	public int getCloseClickCountValue() {
		return close_click_count;
	}

	public boolean getNotification() {
		return notification;
	}

	public String getSocialMedia() {
		return social_media;
	}

	public String getAccessToken() {
		return access_token;
	}

	public String toString() {
		return user_name + "," + loop + "," + interval + "," + file_name + "," + close;
	}
}
