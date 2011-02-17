/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package importDb;

import exceptions.ReceiptException;
import exceptions.SheetNotFoundException;
import importDb.base.Import;
import importDb.base.ImportConstants;
import importDb.base.OdsReader;
import java.io.IOException;

/**
 *
 * @author ssoldatos
 */
public class ImportOds extends Import implements ImportConstants {

    public ImportOds() {
        fileType = FILE_TYPE_ODS;
    }

    @Override
    public boolean createTmpFile() {
        OdsReader ods;
        try {
            ods = new OdsReader(file, tmp);
            if (ods.readOds()) {
                return true;
            } else {
                errorMessage = "Κάποιο λάθος συνέβει με το διάβασμα του αρχείου";
                return false;
            }
        } catch (IOException ex) {
            errorMessage = "Κάποιο λάθος συνέβει με το διάβασμα του αρχείου";
            return false;
        } catch (ReceiptException ex){
            errorMessage = ex.getMessage();
            return false;
        }

    }
}
