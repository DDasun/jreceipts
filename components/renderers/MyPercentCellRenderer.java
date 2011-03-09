/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package components.renderers;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author lordovol
 */
public class MyPercentCellRenderer extends DefaultTableCellRenderer {

  private static final long serialVersionUID = 3254668574L;


  @Override
  public Component getTableCellRendererComponent(JTable table,
          Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    double dValue = (Double) (value);
    String val = (int)(dValue*100) + "%";
    setHorizontalAlignment(SwingConstants.RIGHT);
    JLabel l = (JLabel) this;
    l.setText(val);
    return l;
  }
}
