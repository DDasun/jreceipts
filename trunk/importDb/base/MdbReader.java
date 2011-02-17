/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package importDb.base;

import com.healthmarketscience.jackcess.Column;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Table;
import exceptions.TableNotFoundException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import tools.Helper;

/**
 *
 * @author ssoldatos
 */
public class MdbReader extends Reader{

  
  private Database db;
  private String tableName;
  private Table table;
 

  public MdbReader(File inputFile, File outputFile) throws IOException {
    super(inputFile, outputFile);
  }

  public boolean readMdb() throws TableNotFoundException, IOException {
    db = Database.open(inputFile);
    PrintWriter out = Helper.createOutputStream(outputFile, false);
    Set<String> tables = db.getTableNames();
    if (tables.isEmpty()) {
      throw new TableNotFoundException("Δεν υπάρχει πίνακας στην mdb");
    } else if (tables.size() == 1) {
      for (String t : tables) {
        tableName = t;
      }
    } else {
      tableName = Helper.ask("Δημιουργία βάσης από mdb", "Βρέθηκαν περισσότεροι από ένας πίνακες\n"
          + "Επιλέξτε τον πίνακα που θέλετε να εισάγετε στην εφαρμογή", tables.toArray());
    }
    table = db.getTable(tableName);
    for (Map<String, Object> row : table) {
      Collection<Object> val = row.values();
      String[] valueArray = new String[val.size()];
      int i = -1;
      for (Iterator<Object> it = val.iterator(); it.hasNext();) {
        Object value = it.next();
        valueArray[++i] = parseValue(value);
      }
      String line = Helper.join(valueArray, "\t");
      out.println(line);
    }
    out.close();
    return true;
  }

  
}
