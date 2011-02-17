/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import exceptions.ErrorMessages;
import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import receipts.Main;

/**
 *
 * @author lordovol
 */
public class Helper {

  public static String[] DELIMETERS = {"\t", ",", ";"};

  public static String createAmount(String amount) {
    amount = amount.replaceAll("\\.", "");
    amount = amount.replaceAll(",", ".");
    return amount;
  }

  public static String getGreekMonth(int intMonth) {
    switch (intMonth) {
      case 1:
        return "Ιανουάριος";
      case 2:
        return "Φεβρουάριος";
      case 3:
        return "Μάρτιος";
      case 4:
        return "Απρίλιος";
      case 5:
        return "Μάϊος";
      case 6:
        return "Ιούνιος";
      case 7:
        return "Ιούλιος";
      case 8:
        return "Αύγουστος";
      case 9:
        return "Σεπτέμβριος";
      case 10:
        return "Οκτώβρις";
      case 11:
        return "Νοέμβριος";
      case 12:
        return "Δεκέμβριος";
    }
    return "";
  }

  public static void message(String mess, String title, int type) {
    Info i = new Info(title, mess, type);
    // JOptionPane.showMessageDialog(null, mess, title, type);
  }

  public static int confirm(String title, String message) {
    return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
  }

  public static String convertDateForSQL(String date) {
    String[] d = date.split("/");
    return d[2] + "-" + d[1] + "-" + d[0];
  }

  public static String convertDateForView(Date date) {
    DateFormat df = new SimpleDateFormat(Options.DATE_FORMAT);
    return df.format(date);
  }

  public static String getThisMonth() {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
    return dateFormat.format(calendar.getTime());
  }

  public static String convertDateForSQL(Date date) {
    DateFormat df = new SimpleDateFormat(Options.DATE_SQL_FORMAT);
    return df.format(date);
  }

  public static Date convertStringToDate(String date) {
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    try {
      return df.parse(date);
    } catch (ParseException e) {
      Main.log(Level.WARNING, ErrorMessages.DATE_FORMAT, e);
      return null;
    }
  }

  public static Date convertStringFromSqlToDate(String date) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    try {
      return df.parse(date);
    } catch (ParseException e) {
      Main.log(Level.WARNING, ErrorMessages.DATE_FORMAT, e);
      return null;
    }
  }

  public static String getCurrentGreekMonth() {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
    return dateFormat.format(calendar.getTime());
  }

  public static boolean isInteger(String val) {
    try {
      Integer.parseInt(val);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static String getDelimeter(String line) {
    for (int i = 0; i < DELIMETERS.length; i++) {
      String del = DELIMETERS[i];
      if (substringCount(del, line) > 1) {
        return del;
      }
    }
    return null;
  }

  public static int substringCount(String needle, String haystack) {
    return haystack.split(needle, -1).length - 1;
  }

  public static boolean browse(URI uri) throws UnsupportedOperationException, IOException{
    if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
      try {
        Desktop.getDesktop().browse(uri);
        return true;
      } catch (IOException ex) {
       throw new IOException("Δεν ήταν δυνατή η περιήγηση στην σελίδα " + uri);
      }
      
    } else {
      throw new UnsupportedOperationException("Η περιήγηση στη σελίδα " + uri + " δεν "
          + "επιτρέπεται από το λειτουργικό σύστημα");
    }

  }

  public static boolean mail(URI email) throws IOException {
    if (Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
      try {
        Desktop.getDesktop().mail(email);
        return true;
      } catch (IOException ex) {
       throw new IOException("Δεν ήταν δυνατή η αποστολή στο " + email);
      }

    } else {
      throw new UnsupportedOperationException("Η αποστολή email δεν "
          + "επιτρέπεται από το λειτουργικό σύστημα");
    }
  }

  public static String stripHTML(String str) {
    return str.replaceAll("\\<.*?>", "");

  }

  

  public static String convertAmountForViewing(double amount) {
    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
    dfs.setGroupingSeparator('.');
    dfs.setDecimalSeparator(',');
    DecimalFormat decFormat = new DecimalFormat(Options._DECIMAL_FORMAT);
    decFormat.setDecimalFormatSymbols(dfs);
    return decFormat.format(amount);
  }

  public static String convertAmountForEditing(double amount) {
    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
    dfs.setDecimalSeparator('.');
    DecimalFormat decFormat = new DecimalFormat(Options._DECIMAL_EDITING_FORMAT);
    decFormat.setDecimalFormatSymbols(dfs);
    return decFormat.format(amount);
  }

  public static PrintWriter createOutputStream(File file, boolean append) throws IOException {
    PrintWriter b = null;
    FileOutputStream fos = new FileOutputStream(file, append);
    OutputStreamWriter out = new OutputStreamWriter(fos, "CP1253");
    b = new PrintWriter(out);

    return b;
  }

  public static void openFile(File exportFile) {
    if (Desktop.isDesktopSupported()) {
      try {
        Desktop.getDesktop().open(exportFile);
      } catch (IOException ex) {
        Main.logger.log(Level.SEVERE, ErrorMessages.FILE_NOT_FOUND, ex);
      }
    }

  }

  public static String ask(String title, String question) {
    Ask a = new Ask(title, question, null);
    return (String) a.selection;
    //return JOptionPane.showInputDialog(null, question, title, JOptionPane.QUESTION_MESSAGE);
  }

  public static String ask(String title, String question, Object[] options) {
    Ask a = new Ask(title, question, options);
    return (String) a.selection;
    //return (String) JOptionPane.showInputDialog(null, question, title, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
  }

  public static boolean copyFile(File source, File target) {
    if (source.isFile()) {
      try {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(source), 4096);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(target), 4096);
        int theChar;
        while ((theChar = bis.read()) != -1) {
          bos.write(theChar);
        }
        bos.close();
        bis.close();
        return true;
      } catch (FileNotFoundException ex) {
        Main.log(Level.SEVERE, ErrorMessages.FILE_NOT_FOUND, ex);
        return false;
      } catch (IOException ex) {
        Main.log(Level.SEVERE, ErrorMessages.IO, ex);
        return false;
      }
    }
    return false;
  }

  public static String parseCsvLine(String line, String delimeter, String replacement) {
    CharSequence curChar;
    StringBuilder newString = new StringBuilder();
    Boolean inStr = false;

    for (int i = 0; i < line.length(); i++) {
      curChar = line.subSequence(i, i + 1);
      if (curChar.equals("\"")) {
        inStr = !inStr;
      }
      if (inStr && curChar.equals(String.valueOf(delimeter))) {
        curChar = replacement;
      }
      newString.append(curChar);
    }
    return newString.toString();
  }

  public static BufferedReader createInputStream(File file) throws FileNotFoundException, UnsupportedEncodingException {
    FileInputStream fis = new FileInputStream(file);
    InputStreamReader in = new InputStreamReader(fis, "ISO-8859-7");
    return new BufferedReader(in);
  }

  public static String join(String[] string, String delimiter) {
    java.util.List<?> s = Arrays.asList(string);
    return join(s, delimiter);
  }

  public static String join(Collection<?> s, String delimiter) {
    StringBuilder buffer = new StringBuilder();
    Iterator<?> iter = s.iterator();
    while (iter.hasNext()) {
      buffer.append(iter.next());
      if (iter.hasNext()) {
        buffer.append(delimiter);
      }

    }
    return buffer.toString();
  }

  public static boolean isInArray(String string, String array[]) {
    for (int i = 0; i
        < array.length; i++) {
      if (string.trim().equals(array[i].trim())) {
        return true;
      }
    }
    return false;
  }

  public static String fixAmount(String am) {
    if (am.equals("")) {
      return "";
    }
    if (am.indexOf(".") == -1) {
      return am + ".00";
    } else if (am.indexOf(".") == am.length() - 1) {
      return am + "00";
    } else if (am.indexOf(".") == am.length() - 2) {
      return am + "0";
    } else if (am.indexOf(".") == am.length() - 3) {
      return am;
    }
    return fixAmount(am.replaceFirst("\\.", ""));

  }

   public static boolean hasInternetConnection(String address) {
    BufferedReader in = null;
    try {
      URL url = new URL(address);
      in = new BufferedReader(new InputStreamReader(url.openStream()));
      return true;
    } catch (IOException ex) {
      return false;
    }
  }

   public static void initInternetConnection() {
    if (Options.toBoolean(Options.USE_PROXY)) {
      Properties props = System.getProperties();
      props.put("http.proxyHost", Options.toString(Options.PROXY_HOST));
      props.put("http.proxyPort", Options.toString(Options.PROXY_PORT));
      System.setProperties(props);
    } else {
      Properties props = System.getProperties();
      props.put("http.proxyHost", "");
      props.put("http.proxyPort", "80");
      System.setProperties(props);
    }
  }

  static boolean isNumeric(String string) {
     try {
      long l = Long.parseLong(string.trim());
    } catch (NumberFormatException ex) {
      return false;
    }
    return true;
  }

  private Helper() {
  }
}
