/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package importDb;

import exceptions.ReceiptException;
import importDb.base.Import;
import importDb.base.ImportConstants;
import importDb.base.MdbReader;
import java.io.IOException;

/**
 *
 * @author ssoldatos
 */
public class ImportMdb extends Import implements ImportConstants {

  public ImportMdb() {
    fileType = FILE_TYPE_MDB;
  }

  @Override
  public boolean createTmpFile() {
    try {
      MdbReader mdbr = new MdbReader(file, tmp);
      mdbr.readMdb();
      return true;
    } catch (ReceiptException ex) {
      errorMessage = ex.getMessage();
      return false;
    } catch (IOException ex) {
      errorMessage = "Κάποιο λάθος συνέβει με το διάβασμα του αρχείου";
      return false;
    }
  }
}
