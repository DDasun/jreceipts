/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package panels;

import components.MyTableModel;
import components.MyTablePanel;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;
import models.Type;
import receipts.Main;
import tools.Helper;

/**
 *
 * @author lordovol
 */
public class TypesTablePanel extends MyTablePanel {

  public static final long serialVersionUID = 1L;
  private final Main m;
  

  /** Creates new form TypoesPanel */
  public TypesTablePanel(Main m) {
    setModel();
    super.init();
     bt_popup.setVisible(false);
    this.m = m;
    _NUMBER_OF_FIELDS = 4;
    setTitle("Λίστα Καταστημάτων");
    setHint("Κάντε κλικ ή διπλό κλικ σε κάποιο πεδίο για να το μετατρέψετε και δεξί κλικ για να το διαγράψετε");
    object = Type.class;
    addColumns();
    addRows();
    tableModel.addTableModelListener(this);
    setVisible(true);

  }

  public void addRows() {
    Vector<Object> col = Type.getCollection(false);
    Iterator<Object> it = col.iterator();
    while (it.hasNext()) {
      Type type = (Type) it.next();
      Object data[] = {type.getType_id(), type.getDescription(), type.isValid(), type.getMultiplier()};
      tableModel.addRow(data);
    }

  }

  public void addColumns() {
    String[] names = {"Α/Α", "Είδος", "Εγκυρο","Πολλαπλασιαστής"};
    int[] pref = {40, 400, 100,100};
    int[] min = {40, 200, 100,100};
    int[] max = {60, 2000, 100,100};
    super.addColumns(names, pref, min, max);
  }

  public void update() {
    tableModel.setRowCount(0);
    addRows();
  }

  @Override
  public void delete(int id) {
    if (Helper.confirm("Διαγραφή καταστήματος", "Θέλετε να διαγραφεί το κατάστημα;") == JOptionPane.YES_OPTION) {
      Type.deleteById(id);
      m.updateTypesPanel();
    }
  }

  private void setModel() {
    tableModel = new MyTableModel() {

      @Override
      public boolean isCellEditable(int row, int col) {
        if (col > 0) {
          return true;
        } else {
          return false;
        }
      }

      @Override
      public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
          return Integer.class;
        }
        if (columnIndex == 2) {
          return Boolean.class;
        }
        if (columnIndex == 3) {
          return Double.class;
        }
        return super.getColumnClass(columnIndex);
      }
    };

  }

  public void tableChanged(TableModelEvent e) {
    String rec[] = new String[_NUMBER_OF_FIELDS];
    if (e.getType() == TableModelEvent.UPDATE) {
      int row = e.getFirstRow();
      TableModel model = (TableModel) e.getSource();

      for (int i = 0; i < _NUMBER_OF_FIELDS; i++) {
        rec[i] = String.valueOf(model.getValueAt(row, i));
      }
      Type type = new Type(Integer.parseInt(rec[0]), rec[1], (Boolean.parseBoolean(rec[2]) == true ? 1 : 0), Double.parseDouble(rec[3]));
      try {
        type.save();
        m.updateTotalsPanel();
      } catch (SQLException ex) {
        Helper.message("Κάποιο λάθος δημιουργήθηκε στη βάση.\n Η εγγραφη δεν αποθηκεύθηκε.", "SQL σφάλμα", JOptionPane.ERROR_MESSAGE);
        Main.log(Level.SEVERE, "Κάποιο λάθος δημιουργήθηκε στη βάση.\n Η εγγραφη δεν αποθηκεύθηκε.", ex);
      }

    }
  }
}

