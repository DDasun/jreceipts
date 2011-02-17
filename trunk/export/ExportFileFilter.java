/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package export;

import java.io.File;
import java.io.FilenameFilter;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author ssoldatos
 */
public class ExportFileFilter extends FileFilter implements FilenameFilter {

  private int type;
  private String extension;

  public ExportFileFilter(int type) {
    this.type = type;
    getExtensions();
  }

  public boolean accept(File dir, String name) {
     if(name.endsWith(extension)){
        return true;
      }
     return false;
  }

  @Override
  public boolean accept(File f) {
    if (f.isDirectory()) {
      return true;
    } else {
      if(f.getName().endsWith(extension)){
        return true;
      }
    }
    return false;
  }

  @Override
  public String getDescription() {
    if (type == ExportConstants.EXCEL) {
      return "Excel file (*.xls)";
    } else if (type == ExportConstants.CSV) {
      return "Csv file (*.csv)";
    } else if (type == ExportConstants.PDF) {
      return "Adobe pdf file (*.pdf)";
    }
    return "";
  }

  private void getExtensions() {
    if (type == ExportConstants.EXCEL) {
      extension = "xls";
    } else if (type == ExportConstants.CSV) {
      extension = "csv";
    } else if (type == ExportConstants.PDF) {
      extension = "pdf";
    }
  }
}
