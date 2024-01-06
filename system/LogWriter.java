package system;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class LogWriter {
  private BufferedWriter log_file;
  private Calendar calendar = Calendar.getInstance();

  private SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

  public LogWriter(String file_name) {
    try {
      log_file = new BufferedWriter(new FileWriter(file_name, true));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean writeEnv(String setup_file_name, String source_file_name) {
    try {
      log_file.write(System.getProperty("os.name")
          + " version " + System.getProperty("os.version")
          + "." + System.getProperty("os.arch")
          + " (" + System.getProperty("user.name") + ") " + (calendar.getTime()).toString()
          + " setup " + setup_file_name + " source " + source_file_name + "\n");
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean writeLog(String information, String log) {
    try {
      log_file.write(sdf.format(calendar.getTime()).replace(" ", "-") + " " + information + " " + log + "\n");
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean closeLog(boolean error) {
    try {
      if (error == false) {
        log_file.write(sdf.format(calendar.getTime()) + " - finish\n");
      }
      log_file.close();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}