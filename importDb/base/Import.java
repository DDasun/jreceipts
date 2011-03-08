/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package importDb.base;

import components.Database;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import receipts.Main;
import tools.Helper;
import tools.options.Options;

/**
 *
 * @author ssoldatos
 */
public abstract class Import extends AbstractImport implements ImportConstants {
  
  @Override
  public boolean selectFile() {
    JFileChooser fc = new JFileChooser(new File(Options.USER_DIR));
    fc.setDialogTitle("Επιλογή αρχείου");
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fc.setFileFilter(new ImportFileFilter(fileType));
    fc.setMultiSelectionEnabled(false);
    fc.showOpenDialog(null);
    if ((file = fc.getSelectedFile()) != null) {
      return true;
    }
    errorMessage = "Δεν επιλέξατε κάποιο αρχείο";
    return false;
  }

  public boolean start() {
    tmp = new File(Options.USER_DIR + "import.tmp");
    tmp.deleteOnExit();
    if (selectFile()
        && createTmpFile()
        && readTmpFile()
        && assignFields()
        && createDb()
        && importFile()
        && deleteTmpFile()) {
      return true;
    }
    if (errorMessage != null) {
      Helper.message(errorMessage, "Εισαγωγή αρχείου", JOptionPane.ERROR_MESSAGE);
    }
    return false;
  }

  @Override
  public boolean readTmpFile() {
    String del = "";
    try {
      BufferedReader in = Helper.createInputStream(tmp);
      String line;
      boolean firstLine = true;
      while ((line = in.readLine()) != null) {
        if (firstLine) {
          del = Helper.getDelimeter(line);
          if (del == null) {
            errorMessage = "Δεν βρέθηκε διαχωριστής πεδίων στο αρχείο\n"
                + "Ελέξτε αν τα πεδία του αρχείου διαχωρίζονται με tab , ';' ή ',' ";
            return false;
          }
          firstLine = false;
        }
        line = Helper.parseCsvLine(line, del, "~");
        String[] fields = line.split(del, -1);
        for (int i = 0; i < fields.length; i++) {
          String field = fields[i];
          field = field.replaceAll("~", del);
          if (field.startsWith("\"") && field.endsWith("\"")) {
            field = field.substring(1, field.length() - 1);
          }
          fields[i] = field;

        }
        data.add(fields);
      }
      return true;
    } catch (FileNotFoundException ex) {
      Main.log(Level.SEVERE, null, ex);
      errorMessage = "Το αρχείο εισαγωγής δεν βρέθηκε";
      return false;
    } catch (UnsupportedEncodingException ex) {
      Main.log(Level.SEVERE, null, ex);
      errorMessage = "Το αρχείο εισαγωγής έχει χαρακτήρες που δεν υποατηρίζονται από το σύστημα";
      return false;
    } catch (IOException ex) {
      Main.log(Level.SEVERE, null, ex);
      errorMessage = "Σφάλμα ανάγνωσης από το αρχείο εισαγωγής";
      return false;
    }
  }

  @Override
  public boolean assignFields() {
    AssignFields assign = new AssignFields(file, data);
    if (assign.assigned) {
      headers = assign.headers;
      fields = assign.fields;
      database = assign.database;
      thousandDel = assign.thousandsDel;
      decimalDel = assign.decimalDel;
      multiDecimalDel = assign.multiDecimalDel;
      return true;
    }
    errorMessage = "Δεν έγινε η αναγωγή πεδίων";
    return false;
  }

  @Override
  public boolean createDb() {
    try {
      return Database.createDb(database)==Database.OK;
    } catch (ClassNotFoundException ex) {
      errorMessage="Δεν ήταν δυνατή η δημιουργία της βάσης";
      Main.log(Level.SEVERE, null, ex);
      return false;
    }
  }

  @Override
  public boolean importFile() {
    if (headers) {
      data.remove(0);
    }
    Importer imp = new Importer(database, data, fields, thousandDel, decimalDel,multiDecimalDel);
    if (imp.imported) {
      return true;
    }
    errorMessage="Δεν ήταν δυνατή η εισαγωγή στη βάση";
    return false;
  }

  @Override
  public boolean deleteTmpFile() {
    if(tmp.delete()){
      return true;
    }
    //errorMessage="Δεν ήταν δυνατή η διαγραφή του προσωρινού αρχείου";
    return false;
  }
}
