/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.event.TableModelListener;
import tools.Options;

/**
 *
 * @author lordovol
 */
public abstract class MyTablePanel extends MyStaticTablePanel implements MyTableInterface, TableModelListener {

  private javax.swing.JPopupMenu popup;
  private javax.swing.JMenuItem popup_delete;

  @Override
  protected void init() {
    super.init();
    initPopUp();
  }

  private void initPopUp() {
    popup = new javax.swing.JPopupMenu();
    popup_delete = new javax.swing.JMenuItem();
    popup.setInvoker(table);
    popup_delete.setText("Διαγραφή");
    popup_delete.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        popup_deleteActionPerformed(evt);
      }
    });
    popup.add(popup_delete);
    table.addMouseListener(new java.awt.event.MouseAdapter() {

      public void mouseReleased(java.awt.event.MouseEvent evt) {
        tableMouseReleased(evt);
      }
    });
  }

  private void tableMouseReleased(java.awt.event.MouseEvent evt) {
    clickTable(evt);
  }

  protected void clickTable(java.awt.event.MouseEvent evt) {
    Point p = evt.getPoint();
    rowSelected = table.rowAtPoint(p);
    colSelected = table.columnAtPoint(p);
    if (rowSelected > -1) {
      if (evt.getButton() == MouseEvent.BUTTON3) {
        popup.show(evt.getComponent(), evt.getX(), evt.getY());
      } else if (evt.getButton() == MouseEvent.BUTTON1) {
        Options.selectedValue = table.getModel().getValueAt(rowSelected, colSelected);
      }

    }
  }

  private void popup_deleteActionPerformed(java.awt.event.ActionEvent evt) {
    int id = Integer.parseInt(String.valueOf(tableModel.getValueAt(rowSelected, 0)));
    delete(id);
  }

  public abstract void delete(int id);
}
