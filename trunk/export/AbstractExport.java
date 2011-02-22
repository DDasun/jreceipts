/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package export;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import models.Receipt;
import models.Type;
import tools.options.Options;

/**
 *
 * @author ssoldatos
 */
public abstract class AbstractExport implements ExportConstants {

  protected int type;
  private File exportFile;
  protected Vector<Object> records;
  protected double totalAmount = 0.0;
  protected int receiptId = 0;

  protected abstract void export();

  protected Vector<Object> getRecords() {
    return Receipt.getCollection(false, "WHERE t."+Type.COLUMN_VALID+" = 1");
  }

  public void setFile() {
    JFileChooser c = new JFileChooser();
    c.setDialogTitle("Εξαγωγή λίστας αποδείξεων");
    try {
      //c.setCurrentDirectory(new File(Options.USER_DIR + Options.EXPORTS_PATH));
      c.setSelectedFile(new File(Options.USER_DIR + Options.EXPORTS_PATH + 
          Options.toString(Options.DATABASE) + "." + ExportConstants.EXTENSIONS[type]).getCanonicalFile());
    } catch (IOException ex) {
      Logger.getLogger(AbstractExport.class.getName()).log(Level.SEVERE, null, ex);
    }
    c.setDialogType(JFileChooser.OPEN_DIALOG);
    c.setFileFilter(new ExportFileFilter(type));
    c.setFileSelectionMode(JFileChooser.FILES_ONLY);
    c.setMultiSelectionEnabled(false);
    int res = c.showDialog(null, "Εξαγωγή");
    if (res == JFileChooser.APPROVE_OPTION) {
      setExportFile(c.getSelectedFile());
      if (getExportFile() != null) {
        export();
      }
    }
  }

  /**
   * @return the exportFile
   */
  public File getExportFile() {
    return exportFile;
  }

  /**
   * @param exportFile the exportFile to set
   */
  public void setExportFile(File exportFile) {
    if (exportFile == null) {
      this.exportFile = null;
    }
    if (!exportFile.getName().endsWith(EXTENSIONS[type])) {
      exportFile = new File(exportFile.getParent() + "/" + exportFile.getName() + "." + EXTENSIONS[type]);
    }
    this.exportFile = exportFile;
  }
}
