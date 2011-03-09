/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package components.editors;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import tools.Helper;

/**
 *
 * @author ssoldatos
 */
public abstract class MyTextFieldEditor extends AbstractCellEditor {
  private static final long serialVersionUID = 23525345345L;
  protected JTextField textfield = new JTextField();
  protected Border valid = BorderFactory.createEmptyBorder(1, 1, 1, 1);
  protected Border invalid = BorderFactory.createLineBorder(Color.RED);

  public MyTextFieldEditor() {
    textfield.setBorder(valid);
    textfield.addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent e) {
        if (isValidValue()) {
          textfield.setBorder(valid);
        } else {
          textfield.setBorder(invalid);
        }
      }
    });
  }

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
    String v = String.valueOf(value);
    textfield.setText(v);
    textfield.setBorder(valid);
    return textfield;
  }

  @Override
  public boolean stopCellEditing() {
    if (isValidValue()) {
    } else {
      return false;
    }
    return super.stopCellEditing();
  }

  protected abstract boolean isValidValue();

  public Object getCellEditorValue() {
    return textfield.getText();
  }
}
