/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.options;

import java.awt.Component;
import java.io.IOException;
import java.text.ParseException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import tools.Helper;

/**
 *
 * @author ssoldatos
 */
public class OptionsParser {
  private JPanel[] panels;

  public OptionsParser(JPanel[] panels) throws IOException, ParseException {
    this.panels = panels;
    saveOptions();
  }



  private void saveOptions() throws IOException, ParseException {
      getOptionsComponents();
      Options.save();
      Options.getOptions();
  }

  /**
   * Parsing all the options panels for option components
   */
  private void getOptionsComponents() {
    for (int i = 0; i < panels.length; i++) {
      JPanel panel = panels[i];
      parse(panel);
    }
  }

  /**
   * Parsing a jpanel and gets components with a name diff than "noname"<br />
   * Then sets the option with the name of the component to the value of the componont
   * @param panel
   */
  private void parse(JPanel panel) {
    Component[] c = panel.getComponents();
    for (int i = 0; i < c.length; i++) {
      if (c[i].getName() == null) {
      } else if (!c[i].getName().equals("noname") && !c[i].getName().equals("null")) {
        Options.setOption(c[i].getName(), getValue(c[i]));
      }
    }
  }

   /**
   * Getting a value of a component <br />
   * Components are JSpinner, JCheckBox, JComboBox
   * @param c The component
   * @return the value of the component
   */
  private Object getValue(Component c) {
    if (c instanceof JSpinner) {
      JSpinner spin = (JSpinner) c;
      return spin.getValue();
    }
    if (c instanceof JCheckBox) {
      JCheckBox check = (JCheckBox) c;
      return check.isSelected();
    }
    if (c instanceof JTextField) {
      JTextField text = (JTextField) c;
      return text.getText();
    }
    if (c instanceof JComboBox) {
      JComboBox combo = (JComboBox) c;
      // In some combos get the item instead of index
      String name = combo.getName();
      if (Helper.isInArray(Options._COMBO_OPTIONS_, name)) {
        return String.valueOf(combo.getSelectedItem());
      }
      return combo.getSelectedIndex();
    }
    // Get color buttons
    if (c instanceof JButton) {
      JButton button = (JButton) c;
      return button.getBackground().getRed() + ", "
          + button.getBackground().getGreen() + ", "
          + button.getBackground().getBlue();
    }
    return "";
  }
}
