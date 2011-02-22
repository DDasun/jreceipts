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
public class MyAmountCellRenderer extends DefaultTableCellRenderer {

  private static final long serialVersionUID = 3254668574L;

//  @Override
//  public String getText() {
//    String str = super.getText();
//    if (str.equals("")) {
//      return str;
//    }
//    double d = Double.parseDouble(str);
//    return Helper.convertAmountForViewing(d);
//  }
  @Override
  public Component getTableCellRendererComponent(JTable table,
          Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    double dValue = (Double) (value);
    String val = Helper.convertAmountForViewing(dValue);
    setHorizontalAlignment(SwingConstants.RIGHT);
    JLabel l = (JLabel) this;
    MyStaticTablePanel panel = (MyStaticTablePanel) table.getParent().getParent().getParent();
    if ( panel instanceof ReceiptsTablePanel) {
      l.setBorder(new CompoundBorder(new EmptyBorder(new Insets(0, 0, 0, 5)), l.getBorder()));
      String kind = (String) table.getValueAt(row, 4);
      try {
        double multiplier = (Double) models.Type.getFieldByField("types", "multiplier", "description", kind, 1.0);
        
        if (multiplier != 1.0) {
          setForeground(Color.BLUE);
          String MultValue = Helper.convertAmountForViewing(dValue * multiplier);
          setText(val + "  (" + MultValue + ")");
          setText(val);
        } else {
          setForeground(Skin.getColor_5());
          setText(val);
        }
      } catch (SQLException ex) {
        Main.log(Level.SEVERE, null, ex);
      } catch (Exception ex){
        Main.log(Level.SEVERE, null, ex);
      }
    } else {
      setText(val);
    }
    return l;
  }
}
