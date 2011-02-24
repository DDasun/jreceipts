/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import panels.ReceiptsTablePanel;
import receipts.Main;
import tools.Helper;
import tools.Skin;

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
