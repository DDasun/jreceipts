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
public class MyValidCellRenderer extends DefaultTableCellRenderer {

  private static final long serialVersionUID = 3254668574L;


  @Override
  public Component getTableCellRendererComponent(JTable table,
          Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    boolean v = (Boolean) value;
    setHorizontalAlignment(SwingConstants.CENTER);
    JLabel l = (JLabel) this;
    l.setText("");
    if(v){
      l.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/valid.png")));
    } else {
      l.setIcon(null);
    }
    return l;
  }
}
