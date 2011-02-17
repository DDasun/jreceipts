/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools;

import java.awt.Color;
import java.util.Calendar;

/**
 *
 * @author lordovol
 */
public class Options {


  //public static final String USER_DIR = System.getProperties().getProperty("user.dir");
  public static final String USER_DIR = "./";
  public static boolean DEBUG = true;
  public static String DATABASE = "";
  public static Color COLOR = new Color(255,255,153);
  public static String DATE_FORMAT = "dd/MM/yyyy";
  public static String DATE_SQL_FORMAT = "yyyy-MM-dd";
  public static String _DECIMAL_FORMAT = "###,##0.00 '€'";
  public static String _DECIMAL_EDITING_FORMAT = "##0.00";
  public static String LOG_PATH = "logs/";
  public static String DB_PATH = "databases/";
  public static String EXPORTS_PATH = "exports/";
  public static String DOCS_PATH = "docs/";
  public static String YEAR = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
  public static Color DISABLED_COLOR = Color.BLACK;
  public static int DISABLED_COLOR_ALPHA = 192;


  private Options() {
  }

}
