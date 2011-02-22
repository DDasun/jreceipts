/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import tools.Helper;
import tools.options.Options;

/**
 * The cell editor for the datechooser
 * @author lordovol
 */
public class MyPercentCellEditor extends AbstractCellEditor implements TableCellEditor {

  private static final long serialVersionUID = 917881575221755609L;

  JTextField amount = new JTextField();

  /**
   * Get the cell component
   * @param table The cells table
   * @param value The cells value
   * @param isSelected If the cell is selected
   * @param row The row number
   * @param column The column number
   * @return The date Chooser object
   */
  public Component getTableCellEditorComponent(JTable table, Object value,
      boolean isSelected, int row, int column) {
      int percent = (int) (((Double) value) * 100);
      amount.setText(String.valueOf(percent));
      return amount;
    
  }

  /**
   * Get the value of the cell
   * @return The cells value
   */
  public Double getCellEditorValue() {
    return Double.parseDouble(amount.getText())/100;
    
  }

  


  @Override
  public boolean stopCellEditing() {
    try {
    Double dAmount = Double.parseDouble(amount.getText())/100;
    } catch(NumberFormatException ex){
      Helper.message("Λάθος φορμάτ ποσοστού επι τοις εκατό\nΤο φορμάτ πρέπει να είναι ακέραιος αριθμός ", "Λάθος ποσοστό", JOptionPane.ERROR_MESSAGE);
      amount.setText((String) Options.selectedValue);
      return false;
    }
    return super.stopCellEditing();
  }




  
}
