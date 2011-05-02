/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.awt.Font;
import java.util.Enumeration;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 *
 * @author ssoldatos
 */
public class Fonts {

  public Fonts() {
  }

  public void setDefaultFont(Font font) {
    UIDefaults defaults = UIManager.getDefaults();
    Enumeration keys = defaults.keys();
    while (keys.hasMoreElements()) {
      Object key = keys.nextElement();
      Object value = defaults.get(key);
      if (value != null && value instanceof Font) {
        UIManager.put(key, null);
        if (font != null) {
          UIManager.put(key, new FontUIResource(font));
        }
      }
    }
  }
}
