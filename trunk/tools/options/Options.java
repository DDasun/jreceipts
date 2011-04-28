/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.options;

import exceptions.OptionFormatException;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import receipts.Main;
import tools.Helper;

/**
 *
 * @author lordovol
 */
public class Options {

  public static final String USER_DIR = "./";
  public static final String LOG_PATH = "logs/";
  public static final String DB_PATH = "databases/";
  public static final String BACKUP_PATH = DB_PATH + "backup/";
  public static final String EXPORTS_PATH = "exports/";
  public static final String DOCS_PATH = "docs/";
  public static final String FONTS_PATH = "fonts/";
  public static final String WEBSITE = "http://code.google.com/p/jreceipts/";
  public static final String EMAIL = "lordovol@hotmail.com";
  public static final String DATE_FORMAT = "dd/MM/yyyy";
  public static final String DATE_SQL_FORMAT = "yyyy-MM-dd";
  public static final String _DECIMAL_FORMAT = "###,##0.00 '€'";
  public static final String _DECIMAL_EDITING_FORMAT = "##0.00";
  public static final Color DISABLED_COLOR = Color.GRAY;
  public static final int DISABLED_COLOR_ALPHA = 127;
  

  public static final String DEFAULT_DATABASE = "DEFAULT_DATABASE";
  public static final String DATABASE = "DATABASE";
  public static final String USE_PROXY = "USE_PROXY";
  public static final String PROXY_HOST = "PROXY_HOST";
  public static final String PROXY_PORT = "PROXY_PORT";
  public static final String AUTO_UPDATE = "AUTO_UPDATE";
  public static final String DEBUG = "DEBUG";
  public static final String LOOK_FEEL = "LOOK_FEEL";
  public static final String START_UP_BACKUP = "START_UP_BACKUP";



  public static final String ASK_FOR_DB = "Ερώτηση στην έναρξη";
  public static String[] _COMBO_OPTIONS_ = {DEFAULT_DATABASE, LOOK_FEEL};
  public static Color COLOR = new Color(255, 255, 153);
  public static String YEAR = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
  private static HashMap<String, Object> options;
  
  

  public static void getOptions() throws FileNotFoundException, IOException {
    options = new HashMap<String, Object>();
    Options.loadDefaultOptions();
    if (!new File(Options.USER_DIR + "jreceipts.ini").isFile()) {
      Options.save();
    }
    BufferedReader in = Helper.createInputStream(new File(Options.USER_DIR + "jreceipts.ini"));
    String line = "";
    String[] fields;
    Object value = null;
    while ((line = in.readLine()) != null) {
      if (!line.trim().equals("") && !line.startsWith("/")) {
        fields = line.split("=", -1);
        if (fields[1].trim().equals("true") || fields[1].trim().equals("false")) {
          value = Boolean.parseBoolean(fields[1].trim());
        } else if (Helper.isNumeric(fields[1].trim())) {
          value = Long.parseLong(fields[1].trim());
        } else {
          value = String.valueOf(fields[1]);
        }
        options.put(fields[0].trim(), value);
      }
    }
  }

  public static Integer[] toIntegerArray(String key) {
    String w = Options.toString(key).replaceAll("\\[", "").replaceAll("\\]", "");
    if (w.equals("")) {
      return null;
    }
    String[] arr = w.split(",");
    Integer[] intArr = new Integer[arr.length];
    for (int i = 0; i < arr.length; i++) {
      try {
        intArr[i] = Integer.parseInt(arr[i].trim());
      } catch (NumberFormatException ex) {
        intArr[i] = -1;
      }
    }
    return intArr;
  }

  /**
   * Get an option of an integer type
   * @param key The option to get
   * @return
   */
  public static int toInt(String key) {
    int val = 0;
    String s;
    try {
      s = String.valueOf(options.get(key)).trim();
      if (s != null) {
        val = Integer.parseInt(s);
      } else {
        val = 0;
      }
    } catch (NumberFormatException ex) {
      try {
        throw new OptionFormatException("value " + String.valueOf(options.get(key)).trim() + " of " + key + " is not an integer, setting it to 0");
      } catch (OptionFormatException ex1) {
        Main.log(Level.WARNING, ex1.getMessage(), ex1);
        Options.setOption(key, 0);
        Options.save();
        return 0;
      }
    }
    return val;
  }

  /**
   * Get an option of a float type
   * @param key The option to get
   * @return
   */
  public static float toFloat(String key) {
    float val = 0.0F;
    String s;
    try {
      s = String.valueOf(options.get(key)).trim();
      if (s != null) {
        val = Float.parseFloat(s);
      } else {
        val = 0;
      }
    } catch (NumberFormatException ex) {
      try {
        throw new OptionFormatException("value " + String.valueOf(options.get(key)).trim() + " of " + key + " is not a float, setting it to 0.0");
      } catch (OptionFormatException ex1) {
        Main.log(Level.WARNING, ex1.getMessage(), ex1);
        Options.setOption(key, 0.0F);
        Options.save();
        return 0.0F;
      }
    }
    return val;
  }

  /**
   * Get an option of a boolean type
   * @param key The option to get
   * @return
   */
  public static Boolean toBoolean(String key) {
    Boolean val = false;
    if (options.get(key) == null) {
      return false;
    }
    String value = String.valueOf(options.get(key)).trim();
    if (value.trim().equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
      val = Boolean.parseBoolean(String.valueOf(options.get(key)));
    } else {
      try {
        throw new OptionFormatException(key + " is not a boolean, setting it to false '" + value + "'");
      } catch (OptionFormatException ex1) {
        Main.log(Level.WARNING, ex1.getMessage(), ex1);
        Options.setOption(key, "false");
        Options.save();
        return false;
      }
    }
    return val;
  }

  /**
   * Get an option of a string type
   * @param key The option to get
   * @return
   */
  public static String toString(String key) {
    return toString(key, true);
  }

  /**
   * Get an option of a string type
   * @param key The option to get
   * @param trim Trim value or not
   * @return
   */
  public static String toString(String key, boolean trim) {
    String val = trim ? String.valueOf(options.get(key)).trim() : String.valueOf(options.get(key));
    return val != null && !val.equals("null") ? val : "";
  }

  /**
   * Sets an option
   * @param key The option to set
   * @param value The value to set
   */
  @SuppressWarnings("unchecked")
  public static void setOption(String key, Object value) {
    try {
      options.put(key, value);
    } catch (NullPointerException ex) {
      Main.log(Level.WARNING, "Null pointer exception", ex);
    }

  }

  

  /**
   * Saves the options file
   */
  public static void save() {
    PrintWriter out = null;
    ArrayList<String> arr;

    try {
      out = Helper.createOutputStream(new File(Options.USER_DIR + "jreceipts.ini"), false);
      Iterator<String> it = options.keySet().iterator();
      while (it.hasNext()) {
        String key = String.valueOf(it.next());
        String value = "";
        if (options.get(key) instanceof Object[]) {
          Object[] obj = (Object[]) options.get(key);
          value = Arrays.asList(obj).toString();
        } else {
          value = String.valueOf(options.get(key));
        }
        // Check DB extension
//        if (key.equals(Options.DATABASE)) {
//          if (!value.endsWith(".db")) {
//            value = value + ".db";
//          }
//        }
        out.println(key + "=" + value);
      }
      out.close();
    } catch (IOException ex) {
      Main.log(Level.SEVERE, "Cannot write the ini file", ex);
    } finally {
      out.close();
    }
  }

  /**
   * Writes the default ini file
   * @throws java.io.IOException
   */
  private static void writeDefaultIniFile() throws IOException {
    PrintWriter out = Helper.createOutputStream(new File(Options.USER_DIR + "jreceipts.ini"), false);
    out.println(Options.DEFAULT_DATABASE + "=");
    out.println(Options.USE_PROXY + " =false");
    out.println(Options.PROXY_HOST + " =");
    out.println(Options.PROXY_PORT + " =");
    out.println(Options.AUTO_UPDATE + " =true");
    out.println(Options.DEBUG + "=true");
    out.println(Options.LOOK_FEEL + "=Liquid");
    out.println(Options.START_UP_BACKUP + "=true");
    out.close();
  }

  private static void loadDefaultOptions() {
    options.put(Options.DEFAULT_DATABASE, "");
    options.put(Options.USE_PROXY, false);
    options.put(Options.PROXY_HOST, "");
    options.put(Options.PROXY_PORT, "");
    options.put(Options.AUTO_UPDATE, true);
    options.put(Options.DEBUG, true);
    options.put(Options.LOOK_FEEL, "Liquid");
    options.put(Options.START_UP_BACKUP, true);
  }

  private Options() {
  }
}
