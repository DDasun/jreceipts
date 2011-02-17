
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package importDb.base;

import com.healthmarketscience.jackcess.Column;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import tools.Helper;

/**
 *
 * @author lordovol
 */
public abstract class Reader {

  protected File inputFile;
  protected File outputFile;
  protected PrintWriter out;
  protected String dateFormat = "dd/MM/yyyy";
  protected SimpleDateFormat formatter;
  protected String decimalFormat = "#0.00";
  protected char groupSeperator = '.';
  protected char decimalSeperator = ',';
  protected String[] decimalFields;

  public Reader() {
  }

  protected Reader(File inputFile, File outputFile) throws IOException {
    this.inputFile = inputFile;
    this.outputFile = outputFile;
    out = Helper.createOutputStream(outputFile, false);
  }

  protected String parseValue(Object value) {
    if (value == null) {
      return "";
    } else if(value instanceof String){
      return (String) value;
    } else if (value instanceof Column.AutoNumberGenerator) {
      value = Integer.parseInt((String) value);
    } else if (value instanceof Double || value instanceof BigDecimal) {
      DecimalFormat myFormatter = new DecimalFormat(decimalFormat);
      DecimalFormatSymbols dfs = new DecimalFormatSymbols();
      dfs.setGroupingSeparator('.');
      dfs.setDecimalSeparator(',');
      myFormatter.setDecimalFormatSymbols(dfs);
      value = myFormatter.format(value);
    } else if (value.getClass().toString().equals("class com.healthmarketscience.jackcess.Column$DateExt")) {
      Date date = (Date) value;
      formatter = new SimpleDateFormat(dateFormat);
      value = formatter.format(date);
    } else if (value instanceof Date) {
      formatter = new SimpleDateFormat(dateFormat);
      return formatter.format(value);
    }
    return value.toString();
  }
}
