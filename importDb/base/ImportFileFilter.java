/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package importDb.base;

import java.io.File;
import java.io.FilenameFilter;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author ssoldatos
 */
public class ImportFileFilter extends FileFilter implements FilenameFilter {

  private int type;
  private String[] extension = {"", "", ""};

  public ImportFileFilter(int type) {
    this.type = type;
    getExtensions();
  }

  public boolean accept(File dir, String name) {
    for (int i = 0; i < extension.length; i++) {
      String string = extension[i];
      if (!string.equals("") && name.endsWith(string)) {
        return true;
      }

    }


    return false;
  }

  @Override
  public boolean accept(File f) {
    if (f.isDirectory()) {
      return true;
    } else {
      for (int i = 0; i < extension.length; i++) {
      String string = extension[i];
      if (!string.equals("") && f.getName().endsWith(string)) {
        return true;
      }
    }
    }
    return false;
  }

  @Override
  public String getDescription() {
    if (type == ImportConstants.FILE_TYPE_XLS) {
      return "Excel file (*.xls, *.xlsx)";
    } else if (type == ImportConstants.FILE_TYPE_ASCII) {
      return "Ascii file (*.csv, *.txt, *.tsv)";
    } else if (type == ImportConstants.FILE_TYPE_MDB) {
      return "Access mdb file (*.mdb, *.accdb)";
    } else if (type == ImportConstants.FILE_TYPE_ODS) {
      return "Open Document spreadsheet (*.ods)";
    }
    return "";
  }

  private void getExtensions() {
    if (type == ImportConstants.FILE_TYPE_XLS) {
      extension[0] = "xls";
      extension[1] = "xlsx";
    } else if (type == ImportConstants.FILE_TYPE_ASCII) {
      extension[0] = "txt";
      extension[1] = "csv";
      extension[2] = "tsv";
    } else if (type == ImportConstants.FILE_TYPE_MDB) {
      extension[0] = "mdb";
      extension[1] = "accdb";
    }else if (type == ImportConstants.FILE_TYPE_ODS) {
      extension[0] = "ods";
    }
  }
}
