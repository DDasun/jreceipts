/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.lang.Integer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lordovol
 */
public class MyTableModel extends DefaultTableModel {

    @Override
  public boolean isCellEditable(int row, int col) {
    if (col > 0) {
      return true;
    } else {
      return false;
    }
  }
}
