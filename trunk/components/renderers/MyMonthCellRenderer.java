/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package components.renderers;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import tools.Helper;

/**
 *
 * @author lordovol
 */
public class MyMonthCellRenderer extends DefaultTableCellRenderer {

  @Override
  public String getText() {
    int intMonth;

    try {
      intMonth = Integer.parseInt(super.getText());
    } catch (NumberFormatException ex) {
      return "";
    }
    return Helper.getGreekMonth(intMonth);

  }

  @Override
  public Component getTableCellRendererComponent(JTable table,
          Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    setHorizontalAlignment(SwingConstants.RIGHT);
    return this;
  }
}
