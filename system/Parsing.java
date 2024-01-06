package system;

import java.util.*;

public class Parsing {
  private int id = 0;
  private static int id_count = 0;
  private Map<String, String> map = new HashMap<>();

  public static String calc(boolean integer, String s1, String s2, String token) {
    try {
      if (integer == true) {
        if (token.equals("!")) {
          int n1 = Integer.parseInt(s1);
          return String.valueOf(n1 * (-1));
        } else {
          int n1 = Integer.parseInt(s1);
          int n2 = Integer.parseInt(s2);
          switch (token) {
            case "+":
              return String.valueOf(n1 + n2);
            case "-":
              return String.valueOf(n2 - n1);
            case "*":
              return String.valueOf(n1 * n2);
            case "/":
              return String.valueOf(n2 / n1);
            case "%":
              return String.valueOf(n2 % n1);
            default:
              return null;
          }
        }
      } else {
        if (token.equals("!")) {
          double n1 = Double.parseDouble(s1);
          return String.valueOf(n1 * (-1));
        } else {
          double n1 = Double.parseDouble(s1);
          double n2 = Double.parseDouble(s2);
          switch (token) {
            case "+":
              return String.valueOf(n1 + n2);
            case "-":
              return String.valueOf(n2 - n1);
            case "*":
              return String.valueOf(n1 * n2);
            case "/":
              return String.valueOf(n2 / n1);
            case "%":
              return String.valueOf(n2 % n1);
            default:
              return null;
          }
        }
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
      System.out.println("formula: " + s2 + token + s1);
      return null;
    }
  }

  public static String[] split_symbol(String src) {
    String tmp = "";
    String source = src.replace(" ", "");
    LinkedList<String> split_string = new LinkedList<>();
    for (int i = 0; i < source.length(); i++) {
      char c = source.charAt(i);
      if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '!' || c == '(' || c == ')') {
        if (!tmp.equals("")) {
          split_string.add(tmp);
        }
        if (c != '-') {
          split_string.add(String.valueOf(c));
        } else {
          if (split_string.size() == 0) {
            split_string.add("!");
          } else if (split_string.getLast().equals("(")) {
            split_string.add("!");
          } else {
            split_string.add("-");
          }
        }
        tmp = "";
      } else {
        tmp += String.valueOf(c);
      }
    }
    if (!tmp.equals("")) {
      split_string.add(tmp);
    }
    split_string.add("$");

    String[] array = split_string.toArray(new String[split_string.size()]);
    return array;
  }

  public static int f(String token) {
    switch (token) {
      case "+":
        return 2;
      case "-":
        return 2;
      case "*":
        return 4;
      case "/":
        return 4;
      case "%":
        return 4;
      case "!":
        return 6;
      case "(":
        return 0;
      case ")":
        return 11;
      case "i":
        return 11;
      case "$":
        return 0;
      default:
        return 100;
    }
  }

  public static int g(String token) {
    switch (token) {
      case "+":
        return 1;
      case "-":
        return 1;
      case "*":
        return 3;
      case "/":
        return 3;
      case "%":
        return 3;
      case "!":
        return 15;
      case "(":
        return 10;
      case ")":
        return 0;
      case "i":
        return 10;
      case "$":
        return 0;
      default:
        return 0;
    }
  }

  public Parsing() {
    id = id_count;
    id_count++;
  }

  public int setID() {
    return id;
  }

  public void setVariable(String variable, String src) {
    map.put(variable, bottom_up(src));
  }

  public boolean exploreOperand(String c) {
    String tmp = c.replaceAll("[0-9]", "");
    if (tmp.equals("") || tmp.equals(".")) {
      return true;
    } else {
      if (map.get(c) != null) {
        return true;
      } else {
        return false;
      }
    }
  }

  public String bottom_up(String src) {
    int idx = 0;
    String a_i = "", a_j = "";
    String[] symbol = split_symbol(src);
    Deque<String> token = new ArrayDeque<String>();
    Deque<String> operator = new ArrayDeque<String>();
    operator.push("$");
    while (true) {
      a_i = operator.peek();
      a_j = symbol[idx];
      if (exploreOperand(a_j) == true) {
        token.push(a_j);
        idx++;
      } else {
        if (f(a_i) > g(a_j)) {// a_i >> a_j
          boolean integer = true;
          String tmp;
          if (a_i.equals("!")) {// if operator is '!'
            /// operand ///
            String tmp1 = token.pop();
            tmp = tmp1.replaceAll("[0-9]", "").replace("-", "");
            if (tmp.equals(".")) {// if operand is double
              integer = false;
            } else if (!tmp.equals("")) {// if operand is variable
              tmp = tmp1;
              if ((tmp1 = map.get(tmp1)) == null) {// operand does not exist
                return null;
              }
              tmp = tmp1.replaceAll("[0-9]", "").replace("-", "");
              if (tmp.equals(".")) {// if variable is double
                integer = false;
              }
            }

            /// operator ///
            token.pop();
            operator.pop();

            /// calc ///
            token.push(calc(integer, tmp1, null, a_i));
          } else {// calc process
            /// operand_1 ///
            String tmp1 = token.pop();
            tmp = tmp1.replaceAll("[0-9]", "").replace("-", "");
            if (tmp.equals(".")) {// if operand_1 is double
              integer = false;
            } else if (!tmp.equals("")) {// if operand_1 is variable
              tmp = tmp1;
              if ((tmp1 = map.get(tmp1)) == null) {// operand_1 does not exist
                return null;
              }
              tmp = tmp1.replaceAll("[0-9]", "").replace("-", "");
              if (tmp.equals(".")) {// if variable is double
                integer = false;
              }
            }

            /// operator ///
            token.pop();
            operator.pop();

            /// operand_2 ///
            String tmp2 = token.pop();
            tmp = tmp2.replaceAll("[0-9]", "").replace("-", "");
            if (tmp.equals(".")) {// if operand_2 is double
              integer = false;
            } else if (!tmp.equals("")) {// if operand_2 is variable
              tmp = tmp2;
              if ((tmp2 = map.get(tmp2)) == null) {// operand_2 does not exist
                return null;
              }
              tmp = tmp2.replaceAll("[0-9]", "").replace("-", "");
              if (tmp.equals(".")) {// if variable is double
                integer = false;
              }
            }

            /// calc ///
            token.push(calc(integer, tmp1, tmp2, a_i));
          }
        } else if (f(a_i) < g(a_j)) {// a_i << a_j
          /// push operator ///
          token.push(a_j);
          operator.push(a_j);
          idx++;
        } else {// a_i = a_j
          if (a_i.equals("(")) {
            String tmp = token.pop();/// get operand
            /// pop operator ///
            token.pop();
            operator.pop();
            token.push(tmp);
            idx++;
          } else if (a_i.equals("$") && a_j.equals("$")) {
            break;
          } else {
            return null;
          }
        }
      }
    }

    String result = token.pop();
    if (map.get(result) != null) {
      result = map.get(result);
    }
    return result;
  }
}