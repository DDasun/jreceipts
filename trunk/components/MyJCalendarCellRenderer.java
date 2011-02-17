/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.awt.Component;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import tools.Helper;

/**
 *
 * @author lordovol
 */
public class MyJCalendarCellRenderer extends DefaultTableCellRenderer {

  @Override
  public Component getTableCellRendererComponent(JTable table,
          Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    if (value instanceof Date) {
        String strDate = Helper.convertDateForView((Date) value);
        this.setText(strDate);
    }

    return this;
  }

  @Override
  public String getText() {

    String dStr =  super.getText();
    return dStr;
  }




}
