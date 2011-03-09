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
public class MyPercentCellEditor extends MyTextFieldEditor implements TableCellEditor {

  private static final long serialVersionUID = 917881575221755609L;
  

  /**
   * Get the cell component
   * @param table The cells table
   * @param value The cells value
   * @param isSelected If the cell is selected
   * @param row The row number
   * @param column The column number
   * @return The date Chooser object
   */
  @Override
  public Component getTableCellEditorComponent(JTable table, Object value,
      boolean isSelected, int row, int column) {
    int percent = (int) (((Double) value) * 100);
    textfield.setText(String.valueOf(percent));
    return textfield;
  }

  /**
   * Get the value of the cell
   * @return The cells value
   */
  public Double getCellEditorValue() {
    return Double.parseDouble(textfield.getText()) / 100;

  }

  @Override
  protected boolean isValidValue() {
    try {
      Double dAmount = Double.parseDouble(textfield.getText()) / 100;
      return true;
    } catch (NumberFormatException ex) {
      return false;
    }
  }
}
