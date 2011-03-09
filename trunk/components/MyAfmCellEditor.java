/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import javax.swing.table.TableCellEditor;
import tools.Helper;

/**
 * The cell editor for the datechooser
 * @author lordovol
 */
public class MyAfmCellEditor extends MyTextFieldEditor implements TableCellEditor {

  private static final long serialVersionUID = 917881575221755609L;
  

  @Override
  protected boolean isValidValue() {
    return Helper.isValidAfm(textfield.getText().trim());
  }
}
