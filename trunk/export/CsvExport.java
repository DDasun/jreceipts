/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package export;

import exceptions.ErrorMessages;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.logging.ErrorManager;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import models.Receipt;
import tools.Helper;

/**
 *
 * @author ssoldatos
 */
public class CsvExport extends AbstractExport {

  private PrintWriter out;

  public CsvExport() {
    type = ExportConstants.CSV;
  }

  @Override
  public void export() {
    try {
      records = getRecords();
      out = Helper.createOutputStream(getExportFile(), false);
      String headers="";
      for (int i= 0; i < HEADERS.length; i++) {
        headers+= ";"+ HEADERS[i];
      }
      out.println(headers.substring(1));
      for (Iterator<Object> it = records.iterator(); it.hasNext();) {
        Receipt r = (Receipt) it.next();
        out.println(++receiptId + ";"
            + Helper.convertDateForView(r.getDate()) + ";\""
            + r.getType() + "\";"
            + r.getAfm() + ";"
            + Helper.convertAmountForViewing(r.getAmount())+";"
            + r.getMultiplier() + ";"
            + Helper.convertAmountForViewing(r.getAmount()*r.getMultiplier())+";");
        totalAmount += r.getAmount()*r.getMultiplier();
      }
      out.println(";;;;;;" + Helper.convertAmountForViewing(totalAmount));
      out.close();
      Helper.message("The csv file was exported to " + getExportFile(), "File exported", JOptionPane.INFORMATION_MESSAGE);
      Helper.openFile(getExportFile());

    } catch (IOException ex) {
      receipts.Main.logger.log(Level.SEVERE, ErrorMessages.IO, ex);
    } finally {
      out.close();
    }
  }
}
