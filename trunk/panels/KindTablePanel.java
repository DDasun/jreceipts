/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package panels;

import panels.charts.Graph;
import components.MyAmountCellRenderer;
import components.MyStaticTablePanel;
import components.MyTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;
import models.Statistics;
import models.Statistics.KindStats;
import models.Type;
import receipts.Main;

/**
 *
 * @author lordovol
 */
public class KindTablePanel extends MyStaticTablePanel {

  private static final long serialVersionUID = 4535647567L;
  private final Main m;
  private Vector<Object> collection;
  String[] names = {"Είδος", "Αποδείξεις", "Ποσό"};
    

  /** Creates new form TypoesPanel */
  public KindTablePanel(Main m) {
    setModel();
    super.init();
    bt_popup.addActionListener(new KindStatsActionListener());
    this.m = m;
    _NUMBER_OF_FIELDS = 3;
    _TABLE_NAME_ = _KIND_STATISTICS_;
    setTitle("Στατιστικά ανά είδος");
    object = Type.class;
    addColumns();
    addRows();
    //POSO
    table.getColumn("Ποσό").setCellRenderer(new MyAmountCellRenderer());
    setVisible(true);
  }

  public void addRows() {
    Statistics stats = new Statistics();
    collection = stats.getkindCollection();
    Iterator<Object> it = collection.iterator();
    while (it.hasNext()) {
      KindStats mStats = (KindStats) it.next();
      Object data[] = {mStats.description, mStats.totals, mStats.amount};
      tableModel.addRow(data);
    }

  }

  public void addColumns() {
    int[] pref = {40, 300, 400};
    int[] min = {40, 200, 200};
    int[] max = {60, 400, 1000};
    super.addColumns(names, pref, min, max);

  }

  public void update() {
    tableModel.setRowCount(0);
    addRows();
  }

  private void setModel() {
    tableModel = new MyTableModel() {

      @Override
      public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 1) {
          return Integer.class;
        }
        if (columnIndex == 2) {
          return Double.class;
        }
        return super.getColumnClass(columnIndex);
      }

      @Override
      public boolean isCellEditable(int row, int col) {
        return false;
      }
    };

  }

  private class KindStatsActionListener implements ActionListener{

    public KindStatsActionListener() {
    }

    public void actionPerformed(ActionEvent e) {
      new Graph(names, collection, Statistics.KIND);
    }


  }
}
