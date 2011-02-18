/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.options;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import tools.Skin;

/**
 *
 * @author ΔΙΟΝΥΣΗΣ
 */
public class OptionsTreeCellRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer {

  private static final long serialVersionUID = 534646765786987L;

  public OptionsTreeCellRenderer() {
    setOpaque(true);
    setLeafIcon(new ImageIcon(getClass().getResource("/images/settings.png")));
    setClosedIcon(new ImageIcon(getClass().getResource("/images/settings_plus.png")));
    setOpenIcon(new ImageIcon(getClass().getResource("/images/settings_minus.png")));

  }

   @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
    if (selected) {
      setForeground(Color.BLACK);
      setBackground(Color.LIGHT_GRAY);
      setBorder(BorderFactory.createLineBorder(Skin.getColor_5(), 1));
    } else {
      setForeground(Color.BLACK);
      setBackground(null);
      setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    }
    
    //setPreferredSize(new Dimension(tree.getWidth()-20, 16));
    return this;
  }

}
