/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package export;

/**
 *
 * @author ssoldatos
 */
public interface ExportConstants {

  public final static int EXCEL = 0;
  public final static int CSV = 1;
  public final static int PDF = 2;
  public final String[] EXTENSIONS = {"xls","csv","pdf"};
  public final static int CELL_PADDING = 1;
  public final static int CELL_SPACING = 0;
  public final static int BORDER_WIDTH = 1;
  public final static int TITLE_FONT_SIZE = 13;
  public final static int HEADERS_FONT_SIZE = 10;
  public final static int PLAIN_FONT_SIZE = 9;
  public final static String DEFAULT_FONT = "Verdana";
  public static final String[] HEADERS = {"ΑΑ", "ΗΜΕΡΟΜΗΝΙΑ", "ΕΙΔΟΣ", "Α.Φ.Μ.", "ΠΟΣΟ ΑΠΟΔ.","ΠΟΣ.","ΠΟΣΟ"};
}
