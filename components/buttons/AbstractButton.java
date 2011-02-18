/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package components.buttons;

import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 *
 * @author ssoldatos
 */
public class AbstractButton extends JButton{
  private static final long serialVersionUID = 2534653363L;

  public AbstractButton() {
    setText("");
    setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exit.png")));
    setPreferredSize(new Dimension(32,32));
    setCursor(new Cursor(Cursor.HAND_CURSOR));
    setBorder(BorderFactory.createEmptyBorder());
    setBorderPainted(false);
    setContentAreaFilled(false);
    
  }


}
