package system;

import java.io.*;
import java.util.*;

import java.nio.file.Paths;
import java.nio.file.Files;

import javax.swing.JProgressBar;

import system.controller.CursorController;
import system.comparator.CaptureComparator;

public class AutomaticPlaybackSystem {
	private final String LOG_FILE_NAME = "./log/APS.log";
	private boolean deny = false;
	private int src_number = 0;
	private String src_file_path;
	private JProgressBar progressbar = null;

	private ConfigReader_APS setup;
	private ArrayList<String> src;
	private Parsing parsing;

	public static String done(int numerator, int denominator) {
		final int MAX = 30;
		int ratio = numerator * 100 / (denominator);
		int r = ratio * 30 / 100;
		String done = String.format("\u001b[1A\u001b[KDone %03d: [\u001b[00;32m", ratio);
		for (int i = 0; i < MAX; i++) {
			if (r < i) {
				done += "-";
			} else if (i < r) {
				done += "=";
			} else {
				done += ">\u001b[00m";
			}
		}
		done += "\u001b[00m]";
		return done;
	}

	public void setProgressBar(JProgressBar progressbar) {
		this.progressbar = progressbar;
	}

	public AutomaticPlaybackSystem(String setup_file_path, String src_file_path) {
		this.src_file_path = src_file_path;
		if (Files.notExists(Paths.get("./log/"))) {
			try {
				Files.createDirectory(Paths.get("./log/"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		SourceAnalyzer analyzer = new SourceAnalyzer(LOG_FILE_NAME);
		analyzer.writeAnalyzerInformation(setup_file_path, src_file_path);

		setup = new ConfigReader_APS(setup_file_path);
		src = new ArrayList<>();
		parsing = new Parsing();
		try {
			BufferedReader src_file = new BufferedReader(new FileReader(src_file_path));
			int line_number = 0;
			int tmp = 0;
			String src_line;
			while ((src_line = src_file.readLine()) != null) {
				src_line = src_line.replace(" ", "");
				if ((tmp = analyzer.examinateInstruction_APS(line_number, src_line)) < 0) {
					deny = true;
					break;
				}
				src.add(src_line);
				line_number++;
				src_number += tmp;
				System.out.println(src_line);
			}
			src_file.close();
			if (analyzer.getCount_begin_end() != 0) {
				deny = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			src.add("padding");
		}
		analyzer.closeAnalyzerLog(deny);
	}

	public boolean sourcePlaybackSystem() {
		CaptureComparator cmp = new CaptureComparator();
		CursorController cursor = new CursorController();

		Stack<Integer> loop_idx = new Stack<>();
		double threshold = 0.0;
		double ratio = 0.0;
		int src_count = 0;
		int loop = 0;
		int overall_loop = setup.getLoopValue();
		int interval = setup.getIntervalValue();
		boolean close = setup.getCloseValue();

		int rand_scale = 1;
		int x = 0;
		int y = 0;
		int w = 0;

		if (deny == true) {
			return false;
		}

		System.out.println(done(0, overall_loop * src.size()));

		try {
			Thread.sleep(interval);
		} catch (InterruptedException e) {
			System.out.println("Interrupted code: 10101");
			return false;
		}
		try {
			BufferedWriter log_file = new BufferedWriter(new FileWriter("./log/exec.log", true));
			log_file.write("\n");
			for (int i = 0; i < overall_loop; i++) {
				/* point window */
				for (int line_number = 1; line_number < src.size(); line_number++) {
					src_count++;
					String[] token = (src.get(line_number)).split(":");
					String[] arguments;
					switch (token[0]) {
						case "begin":
							for (int j = 0; j < (loop - 1); j++) {
								loop_idx.push(line_number);
							}
							break;
						case "click":
							arguments = SourceAnalyzer.splitParentheses(token[1], ",");
							int c = 1;
							try {
								String[] v = arguments[1].split(",");
								x = Integer.parseInt(parsing.bottom_up(v[0]));
								y = Integer.parseInt(parsing.bottom_up(v[1]));
								if (v.length == 3) {
									c = Integer.parseInt(parsing.bottom_up(v[2]));
								}
							} catch (NumberFormatException e) {
								System.out.println(e.toString() + " at " + src_file_path + " line " + line_number);
								log_file.write((new Date()).toString() + ", " + src.get(line_number) + ", error\n");
								log_file.flush();
								log_file.close();
								System.exit(1);
							}
							cursor.moveCursor(x, y);
							try {
								Thread.sleep(interval);
							} catch (InterruptedException e) {
								System.out.println("Interrupted code: 20101");
								log_file.write(
										(new Date()).toString() + ", " + src.get(line_number) + ", interrupted\n");
								log_file.flush();
								log_file.close();
								return false;
							}
							if (arguments[0].equals("left")) {
								cursor.click_leftMouse(c);
							} else if (arguments[0].equals("right")) {
								cursor.click_rightMouse(c);
							}
							try {
								Thread.sleep(interval);
							} catch (InterruptedException e) {
								System.out.println("Interrupted code: 20102");
								log_file.write(
										(new Date()).toString() + ", " + src.get(line_number) + ", interrupted\n");
								log_file.flush();
								log_file.close();
								return false;
							}
							break;
						case "end":
							if (!loop_idx.isEmpty()) {
								line_number = loop_idx.pop();
							}
							break;
						case "loop":
							try {
								loop = Integer.parseInt(parsing.bottom_up(token[1]));
							} catch (NumberFormatException e) {
								System.out.println(e.toString() + " at " + src_file_path + " line " + line_number);
								log_file.write(
										(new Date()).toString() + ", " + src.get(line_number) + ", interrupted\n");
								log_file.flush();
								log_file.close();
								System.exit(1);
							}
							break;
						case "source":
							break;
						case "var":
							arguments = token[1].split("=");
							parsing.setVariable(arguments[0], arguments[1]);
							break;
						case "wheel":
							arguments = token[1].split(",");
							try {
								w = Integer.parseInt(parsing.bottom_up(arguments[1]));
							} catch (NumberFormatException e) {
								System.out.println(e.toString() + " at " + src_file_path + " line " + line_number);
								log_file.write((new Date()).toString() + ", " + src.get(line_number) + ", error\n");
								log_file.flush();
								log_file.close();
								System.exit(1);
							}
							if (arguments[0].equals("up")) {
								cursor.moveWheel(Math.abs(w) * (-1));
							} else if (arguments[0].equals("down")) {
								cursor.moveWheel(Math.abs(w));
							}
							try {
								Thread.sleep(interval);
							} catch (InterruptedException e) {
								System.out.println("Interrupted code: 20201");
								log_file.write(
										(new Date()).toString() + ", " + src.get(line_number) + ", interrupted\n");
								log_file.flush();
								log_file.close();
								return false;
							}
							break;
						case "wait":
							arguments = SourceAnalyzer.splitParentheses(token[1], ",");
							switch (arguments[0]) {
								case "cmp_screen_cursor_move":
									try {
										String[] v = arguments[1].split(",");
										x = Integer.parseInt(parsing.bottom_up(v[0]));
										y = Integer.parseInt(parsing.bottom_up(v[1]));
										rand_scale = Integer.parseInt(parsing.bottom_up(v[2]));
										threshold = Double.parseDouble(parsing.bottom_up(arguments[2]));
										while ((ratio = cmp.coincidenceRatio_move_cursor(interval, x, y,
												rand_scale)) < threshold) {
											if (ratio < 0) {
												return false;
											}
										}
									} catch (NumberFormatException e) {
										System.out.println(
												e.toString() + " at " + src_file_path + " line " + line_number);
										log_file.write(
												(new Date()).toString() + ", " + src.get(line_number) + ", error\n");
										log_file.flush();
										log_file.close();
										System.exit(1);
									}
									break;
								case "cmp_screen":
									try {
										threshold = Double.parseDouble(parsing.bottom_up(arguments[2]));
										while ((ratio = cmp.coincidenceRatio(interval)) < threshold) {
											if (ratio < 0) {
												log_file.write((new Date()).toString() + ", " + src.get(line_number)
														+ ", false");
												return false;
											}
										}
									} catch (NumberFormatException e) {
										System.out.println(
												e.toString() + " at " + src_file_path + " line " + line_number);
										log_file.write(
												(new Date()).toString() + ", " + src.get(line_number) + ", error\n");
										log_file.flush();
										log_file.close();
										System.exit(1);
									}
									break;
								case "null":
									try {
										Thread.sleep(Integer.parseInt(parsing.bottom_up(arguments[2])));
									} catch (NumberFormatException e) {
										System.out.println(
												e.toString() + " at " + src_file_path + " line " + line_number);
										log_file.write(
												(new Date()).toString() + ", " + src.get(line_number) + ", error\n");
										log_file.flush();
										log_file.close();
										System.exit(1);
									} catch (InterruptedException e) {
										System.out.println("Interrupted code: 20301");
										log_file.write((new Date()).toString() + ", " + src.get(line_number)
												+ ", interrupted\n");
										log_file.flush();
										log_file.close();
										return false;
									}
									break;
								default:
									System.out.println("Error    : Undefined token in source code\n"
											+ "Problem  - Number of argument of \"wait\" instruction\n"
											+ "arguments: " + token[1]);

									log_file.write((new Date()).toString() + ", " + src.get(line_number) + ", error\n");
									log_file.flush();
									log_file.close();
									System.exit(1);
									break;
							}
							break;
						default:
							System.out.println("Error   : Incorrect source code\n"
									+ "Problem - Undefined instruction token\n"
									+ "token   : " + token[0]);
							System.out.println("Ignore this source code at " + src_file_path + " line " + line_number);
							log_file.write((new Date()).toString() + ", " + src.get(line_number) + ", error\n");
							log_file.flush();
							log_file.close();
							System.exit(1);
							break;
					}
					System.out.println(done(src_count, overall_loop * src_number));
					if (progressbar != null) {
						progressbar.setValue(src_count * 100 / (overall_loop * src_number));
					}
					log_file.write((new Date()).toString() + ", " + src.get(line_number) + ", complete\n");
				}
			}
			log_file.flush();
			log_file.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		if (close) {
			cursor.moveCursor(setup.getCloseXValue(), setup.getCloseYValue());
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				System.out.println("Interrupted code: 30101");
				return false;
			}
			cursor.click_leftMouse(setup.getCloseClickCountValue());
		}
		System.out.println(done(1, 1));
		if (progressbar != null) {
			progressbar.setValue(100);
		}
		return true;
	}
}

class SourceAnalyzer {
	private int count_begin_end;
	private int loop;
	private Stack<Integer> loop_idx = new Stack<>();

	private LogWriter log_file;
	private Parsing parsing;

	public SourceAnalyzer(String log_file_name) {
		count_begin_end = 0;
		loop_idx = new Stack<>();
		loop_idx.push(1);
		log_file = new LogWriter(log_file_name);
		parsing = new Parsing();
	}

	public boolean writeAnalyzerInformation(String config_file_name, String source_file_name) {
		return log_file.writeEnv(config_file_name, source_file_name);
	}

	public boolean closeAnalyzerLog(boolean error) {
		return log_file.closeLog(error);
	}

	public int getCount_begin_end() {
		if (0 < count_begin_end) {
			log_file.writeLog("ERROR", "reached_end_of_file_while_parsing");
		}
		return count_begin_end;
	}

	public int examinateInstruction_APS(int line_number, String source) {
		final int TOKEN_SIZE = 2;
		final int CLICK_ARGUMENT_SIZE = 2;

		String[] token = source.split(":");
		if (source.equals("begin:")) {
			count_begin_end++;
			log_file.writeLog("-", "$" + source);
			loop_idx.push(loop);
			return loop_idx.peek();
		} else if (source.equals("end:")) {
			count_begin_end--;
			if (count_begin_end < 0) {
				log_file.writeLog("ERROR", ("line=" + line_number + ",syntax_$" + source));
				return -1;
			}
			loop_idx.pop();
			log_file.writeLog("-", "$" + source);
			return loop_idx.peek();
		}
		if (token.length != TOKEN_SIZE) {
			log_file.writeLog("ERROR", ("line=" + line_number + ",syntax_$" + source));
			return -1;
		}
		String[] arguments;
		String[] v;
		switch (token[0]) {
			case "begin":
				log_file.writeLog("ERROR", ("line=" + line_number + ",number_of_argument_$" + source));
				return -1;
			case "click":
				arguments = splitParentheses(token[1], ",");
				if (arguments == null) {
					log_file.writeLog("ERROR", ("line=" + line_number + ",parentheses_$" + source));
					return -1;
				}
				if (arguments.length != CLICK_ARGUMENT_SIZE) {
					log_file.writeLog("ERROR", ("line=" + line_number + ",number_of_argument_$" + source));
					return -1;
				}
				if (!arguments[0].equals("left") && !arguments[0].equals("right")) {
					log_file.writeLog("ERROR", ("line=" + line_number + ",argument-1_$" + source));
					return -1;
				}
				v = arguments[1].split(",");
				if (v.length != 2 && v.length != 3) {
					log_file.writeLog("ERROR", ("line=" + line_number + ",number_of_argument-2_$" + source));
					return -1;
				}
				for (int i = 0; i < v.length; i++) {
					try {
						Integer.parseInt(parsing.bottom_up(v[i]));
					} catch (NumberFormatException e) {
						log_file.writeLog("ERROR", ("line=" + line_number + ",argument-2-" + i + "_$" + source + "\n"
								+ e.toString()));
						return -1;
					}
				}
				break;
			case "end":
				log_file.writeLog("ERROR", ("line=" + line_number + ",number_of_argument_$" + source));
				return -1;
			case "loop":
				try {
					loop = Integer.parseInt(parsing.bottom_up(token[1]));
				} catch (NumberFormatException e) {
					log_file.writeLog("ERROR", ("line=" + line_number + ",argument_$" + source + "\n"
							+ e.toString()));
					return -1;
				}
				break;
			case "source":
				break;
			case "var":
				arguments = token[1].split("=");
				parsing.setVariable(arguments[0], arguments[1]);
				break;
			case "wheel":
				arguments = token[1].split(",");
				if (arguments.length != 2) {
					log_file.writeLog("ERROR", ("line=" + line_number + ",number_of_argument_$" + source));
					return -1;
				}
				if (!arguments[0].equals("up") && !arguments[0].equals("down")) {
					log_file.writeLog("ERROR", ("line=" + line_number + ",argument-1_$" + source));
					return -1;
				}
				try {
					Integer.parseInt(parsing.bottom_up(arguments[1]));
				} catch (NumberFormatException e) {
					log_file.writeLog("ERROR", ("line=" + line_number + ",argument-2_$" + source + "\n"
							+ e.toString()));
					return -1;
				}
				break;
			case "wait":
				arguments = splitParentheses(token[1], ",");
				if (arguments == null) {
					log_file.writeLog("ERROR", ("line=" + line_number + ",parentheses_$" + source));
					return -1;
				}
				if (arguments.length != 3) {
					log_file.writeLog("ERROR", ("line=" + line_number + ",number_of_argument_$" + source));
					return -1;
				}
				switch (arguments[0]) {
					case "cmp_screen_cursor_move":
						try {
							v = arguments[1].split(",");
							Integer.parseInt(parsing.bottom_up(v[0]));
							Integer.parseInt(parsing.bottom_up(v[1]));
							Integer.parseInt(parsing.bottom_up(v[2]));
						} catch (NumberFormatException e) {
							log_file.writeLog("ERROR", ("line=" + line_number + ",argument-2_$" + source + "\n"
									+ e.toString()));
							return -1;
						}
						try {
							Double.parseDouble(parsing.bottom_up(arguments[2]));
						} catch (NumberFormatException e) {
							log_file.writeLog("ERROR", ("line=" + line_number + ",argument-3_$" + source + "\n"
									+ e.toString()));
							return -1;
						}
						break;
					case "cmp_screen":
						try {
							Double.parseDouble(parsing.bottom_up(arguments[2]));
						} catch (NumberFormatException e) {
							log_file.writeLog("ERROR", ("line=" + line_number + ",argument-3_$" + source + "\n"
									+ e.toString()));
							return -1;
						}
						break;
					case "null":
						try {
							Integer.parseInt(parsing.bottom_up(arguments[2]));
						} catch (NumberFormatException e) {
							log_file.writeLog("ERROR", ("line=" + line_number + ",argument-3_$" + source + "\n"
									+ e.toString()));
							return -1;
						}
						break;
					default:
						log_file.writeLog("ERROR", ("line=" + line_number + ",argument-1_$" + source));
						return -1;
				}
				break;
			default:
				log_file.writeLog("ERROR", ("line=" + line_number + ",undefined_instruction_$" + source));
				return -1;
		}
		log_file.writeLog("-", "$" + source);
		return loop_idx.peek();
	}

	public static String[] splitParentheses(String array, String regex) {
		String[] tmp_array = array.split(regex);
		ArrayList<String> split_list = new ArrayList<>();

		boolean in_parentheses = false;
		int n = 0;
		String tmp = "";

		for (int i = 0; i < tmp_array.length; i++) {
			for (int j = 0; j < tmp_array[i].length(); j++) {
				if (tmp_array[i].charAt(j) == '(') {
					n++;
				}
			}
			for (int j = 0; j < tmp_array[i].length(); j++) {
				if (tmp_array[i].charAt(j) == ')') {
					n--;
				}
			}

			if (tmp_array[i].charAt(0) == '(' && in_parentheses == false) {
				tmp_array[i] = tmp_array[i].substring(1);
				tmp = tmp_array[i] + regex;
				in_parentheses = true;
			} else if (tmp_array[i].charAt(tmp_array[i].length() - 1) == ')' && n == 0) {
				tmp_array[i] = tmp_array[i].substring(0, tmp_array[i].length() - 1);
				split_list.add(tmp + tmp_array[i]);
				in_parentheses = false;
				tmp = "";
			} else {
				tmp_array[i] = tmp_array[i];
				if (in_parentheses == true) {
					tmp = tmp + tmp_array[i] + regex;
				} else {
					split_list.add(tmp_array[i]);
				}
			}
			if (n < 0) {// difference in the number of parentheses
				return null;
			}
		}
		if (n != 0) {// difference in the number of parentheses
			return null;
		}

		String[] str = new String[split_list.size()];
		split_list.toArray(str);

		return str;
	}
}