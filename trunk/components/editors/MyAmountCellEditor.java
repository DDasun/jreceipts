/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package components.editors;

import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;

/**
 * The cell editor for the datechooser
 * @author lordovol
 */
public class MyAmountCellEditor extends MyTextFieldEditor implements TableCellEditor {

  private static final long serialVersionUID = 917881575221755609L;

  public MyAmountCellEditor() {
    super();
    textfield.setHorizontalAlignment(SwingConstants.RIGHT);
  }

  @Override
  public Double getCellEditorValue() {
    return Double.parseDouble(textfield.getText());
  }



   @Override
  protected boolean isValidValue() {
    try {
      Double dAmount = Double.parseDouble(textfield.getText());
      return true;
    } catch (NumberFormatException ex) {
      return false;
    }
    
  }
}
