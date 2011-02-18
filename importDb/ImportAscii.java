/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package importDb;

import importDb.base.ImportConstants;
import importDb.base.Import;
import java.io.File;
import tools.Helper;
import tools.options.Options;

/**
 *
 * @author ssoldatos
 */
public class ImportAscii extends Import implements ImportConstants {

  public ImportAscii() {
    fileType = FILE_TYPE_ASCII;
  }

  @Override
  public boolean createTmpFile() {
    if(Helper.copyFile(file,tmp)){
      return true;
    }
    errorMessage = "Κάποιο λάθος συνέβει με το άνοιγμα του αρχείου";
    return false;
  }


}
