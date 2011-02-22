/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package panels;

import panels.charts.Graph;
import components.MyAmountCellRenderer;
import components.MyMonthCellRenderer;
import components.MyStaticTablePanel;
import components.MyTableModel;
import components.MyTablePanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;
import models.Statistics;
import models.Statistics.MonthlyStats;
import models.Type;
import receipts.Main;

/**
 *
 * @author lordovol
 */
public class MonthlyTablePanel extends MyStaticTablePanel {
  private static final long serialVersionUID = 4535647567L;

  private final Main m;
  private Vector<Object> collection;
   String[] names = {Statistics.COLUMN_MONTH, Statistics.COLUMN_TOTALS, Statistics.COLUMN_AMOUNT};

  /** Creates new form TypoesPanel */
  public MonthlyTablePanel(Main m) {
    setModel();
    super.init();
    bt_popup.addActionListener(new MonthlyStatsActionListener());
    this.m = m;
    _NUMBER_OF_FIELDS = 3;
    _TABLE_NAME_ = _MONTHLY_STATISTICS_;
    setTitle("Στατιστικά ανά μήνα");
    object = Type.class;
    addColumns();
    addRows();
     //POSO
    table.getColumn(Statistics.COLUMN_AMOUNT).setCellRenderer(new MyAmountCellRenderer());
    table.getColumn(Statistics.COLUMN_MONTH).setCellRenderer(new MyMonthCellRenderer());
    setVisible(true);
  }

  public void addRows() {
    Statistics stats = new Statistics();
   collection = stats.getMonthlyCollection();
    Iterator<Object> it = collection.iterator();
    while (it.hasNext()) {
      MonthlyStats mStats = (MonthlyStats) it.next();
      Object data[] = {mStats.month, mStats.totals, mStats.amount};
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

    private class MonthlyStatsActionListener implements ActionListener{

    public MonthlyStatsActionListener() {
    }

    public void actionPerformed(ActionEvent e) {
      new Graph(names, collection, Statistics.MONTHLY);
    }
  }

}

