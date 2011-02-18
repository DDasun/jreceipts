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
  public static final String EXPORTS_PATH = "exports/";
  public static final String DOCS_PATH = "docs/";
  public static final String WEBSITE = "http://code.google.com/p/jreceipts/";
  public static final String EMAIL = "lordovol@hotmail.com";
  public static final String DATE_FORMAT = "dd/MM/yyyy";
  public static final String DATE_SQL_FORMAT = "yyyy-MM-dd";
  public static final String _DECIMAL_FORMAT = "###,##0.00 '€'";
  public static final String _DECIMAL_EDITING_FORMAT = "##0.00";
  public static final Color DISABLED_COLOR = Color.BLACK;
  public static final int DISABLED_COLOR_ALPHA = 192;
  public static boolean DEBUG = true;
  public static final String DEFAULT_DATABASE = "DEFAULT_DATABASE";
  public static final String DATABASE = "DATABASE";
  public static final String ASK_FOR_DB = "Ερώτηση στην έναρξη";

  public static String[] _COMBO_OPTIONS_ = {DEFAULT_DATABASE};
  
  
  public static Color COLOR = new Color(255,255,153);
  public static String YEAR = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
  public static Object selectedValue;
  
  
  private static HashMap<String, Object> options;
  public static String USE_PROXY;
  public static String PROXY_HOST;
  public static String PROXY_PORT;


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
    if (options.get(key)==null){
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
   * Writes the default ini file
   * @throws java.io.IOException
   */
  private static void writeDefaultIniFile() throws IOException {
    PrintWriter out = Helper.createOutputStream(new File(Options.USER_DIR + "jreceipts.ini"), false);
    out.println(Options.DEFAULT_DATABASE + "=");
//    out.println(Options.DEBUG_MODE + "=0");
//    out.println(Options.MODAL + "=true");
//    out.println(Options.DATE_FORMAT + "=dd/MM/yyyy");
//    out.println(Options.LOOK_AND_FEEL + "=");
//    out.println(Options.SKIN_COLOR + " =240,240,240");
//    out.println(Options.USE_SKIN + " =true");
//    out.println(Options.USE_PROXY + " =false");
//    out.println(Options.UNIFIED_SERIES + " =true");
//    out.println(Options.PROXY_HOST + " =");
//    out.println(Options.PROXY_PORT + " =");
//    out.println(Options.DIVIDER_LOCATION + " =250");
//    out.println(Options.FEED_DIVIDER_LOCATION + " =250");
//    out.println(Options.FONT_FACE + " =Arial");
//    out.println(Options.FONT_SIZE + " =12");
//    out.println(Options.TABLE_WIDTHS + " =" + getDefaultColumnWidths());
//    out.println(Options.WINDOW_STATE + " =" + JFrame.NORMAL);
//    out.println(Options.WIDTH + " =1000");
//    out.println(Options.HEIGHT + " =600");
//    out.println(Options.CHECK_VERSION + " =true");
//    out.println(Options.PRIMARY_SUB + " =Greek");
//    out.println(Options.SECONDARY_SUB + " =English");
//    out.println(Options.SUBTITLE_SITE + " =" + SubtitleConstants.SUBTITLE_ONLINE_NAME);
//    out.println(Options.AUTO_FILE_UPDATING + " =false");
//    out.println(Options.SEASON_SEPARATOR + " =SE");
//    out.println(Options.TITLE_SEPARATOR + " = - ");
//    out.println(Options.EPISODE_SEPARATOR + " =x");
//    out.println(Options.TOOLBAR_POSITION + " =1");
//    out.println(Options.TOOLBAR_BUTTONS + "=" + getDefaultToolbarButtons());
//    out.println(Options.FEED_COLUMNS + "=" + 1);
//    out.println(Options.VIDEO_APP + "=");

    out.close();
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

  private static void loadDefaultOptions() {
    options.put(Options.DEFAULT_DATABASE, "");
//    options.put(Options.DEBUG_MODE, new Integer(0));
//    options.put(Options.MODAL, true);
//    options.put(Options.DATE_FORMAT, "dd/MM/yyyy");
//    options.put(Options.LOOK_AND_FEEL, "");
//    options.put(Options.SKIN_COLOR, "240,240,240");
//    options.put(Options.USE_SKIN, true);
//    options.put(Options.USE_PROXY, false);
//    options.put(Options.PROXY_HOST, "");
//    options.put(Options.PROXY_PORT, "");
//    options.put(Options.DIVIDER_LOCATION, new Integer(250));
//    options.put(Options.FONT_FACE, "Arial");
//    options.put(Options.FONT_SIZE, new Float(12F));
//    options.put(Options.TABLE_WIDTHS, getDefaultColumnWidths());
//    options.put(Options.WINDOW_STATE, JFrame.NORMAL);
//    options.put(Options.WIDTH, new Integer(1000));
//    options.put(Options.HEIGHT, new Integer(600));
//    options.put(Options.CHECK_VERSION, true);
//    options.put(Options.PRIMARY_SUB, "Greek");
//    options.put(Options.SECONDARY_SUB, "English");
//    options.put(Options.SUBTITLE_SITE, SubtitleConstants.SUBTITLE_ONLINE_NAME);
//    options.put(Options.AUTO_FILE_UPDATING, true);
//    options.put(Options.SEASON_SEPARATOR, " SE");
//    options.put(Options.TITLE_SEPARATOR, "  - ");
//    options.put(Options.EPISODE_SEPARATOR, " x");
//    options.put(Options.TOOLBAR_POSITION, new Integer(0));
//    options.put(Options.TOOLBAR_BUTTONS, getDefaultToolbarButtons());
//    options.put(Options.FEED_DIVIDER_LOCATION, 250);
//    options.put(Options.FEED_COLUMNS, 1);
//    options.put(Options.UNIFIED_SERIES, false);
//    options.put(Options.VIDEO_APP, "");
  }

  private Options() {
  }


}
