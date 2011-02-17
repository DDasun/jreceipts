/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package importDb.base;

import exceptions.NoSheetSelectedException;
import exceptions.SheetNotFoundException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import tools.Helper;

/**
 *
 * @author ssoldatos
 */
public class OdsReader extends Reader {

    private Sheet activeSheet;
    private SpreadSheet doc;

    public OdsReader(File inputFile, File outputFile) throws IOException {
        super(inputFile, outputFile);
    }

    public boolean readOds() throws SheetNotFoundException, IOException, NoSheetSelectedException {
        doc = SpreadSheet.createFromFile(inputFile);
        if (selectSheet()) {
            int rows = activeSheet.getRowCount();
            int cols = getColsCount();
            for (int i = 0; i < rows; i++) {
                String[] line = new String[cols];
                for (int j = 0; j < cols; j++) {
                    line[j] = parseValue(activeSheet.getValueAt(j, i));
                }
                out.println(Helper.join(line, "\t"));
            }
            out.close();
            return true;
        } else {
            throw new NoSheetSelectedException("Δεν επιλέξατε κάποιο φύλλο");
        }
    }

    private boolean selectSheet() throws SheetNotFoundException {
        int sheets = doc.getSheetCount();
        if (sheets == 0) {
            throw new SheetNotFoundException("Το αρχείο δεν έχει περιεχόμενα");
        }
        if (sheets > 1) {
            String[] sheetNames = new String[sheets];
            for (int i = 0; i < sheets; i++) {
                sheetNames[i] = doc.getSheet(i).getName().trim();
            }
            String name = Helper.ask("Δημιουργία βάσης από ods", "Διαλέξτε το φύλλο απο το οποίο θέλετε να εισάγετε τα δεδομένα", sheetNames);
            if (name != null) {
                activeSheet = doc.getSheet(name);
                return true;
            } else {
                return false;
            }
        } else {
            activeSheet = doc.getSheet(0);
            return true;
        }
    }

    private int getColsCount() {
        int cols = activeSheet.getColumnCount();
        for (int i = 0; i < cols; i++) {
            if (activeSheet.getValueAt(i, 0).equals("")) {
                return i;
            }
        }
        return cols;
    }
}
