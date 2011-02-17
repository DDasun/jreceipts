/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package importDb;

import exceptions.ReceiptException;
import importDb.base.Import;
import importDb.base.ImportConstants;
import importDb.base.XlsReader;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.Options;

/**
 *
 * @author ssoldatos
 */
public class ImportXls extends Import implements ImportConstants {

  public ImportXls() {
    fileType = FILE_TYPE_XLS;
  }

  @Override
  public boolean createTmpFile() {
    try {
      XlsReader xls;
      xls = new XlsReader(file, tmp);
      if (xls.readExcel()) {
        return true;
      }
      errorMessage = "Κάποιο λάθος συνέβει με το διάβασμα του αρχείου";
      return false;
    } catch (IOException ex) {
      errorMessage = "Κάποιο λάθος συνέβει με το διάβασμα του αρχείου";
      return false;
    } catch(ReceiptException ex){
      errorMessage = ex.getMessage();
      return false;
    }

  }
}
