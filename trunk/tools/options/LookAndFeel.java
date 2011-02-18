/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.options;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author ssoldatos
 */
public class LookAndFeel {

  public static final String SYSTEM = "System";
  public static final String CROSS = "Cross Platform";
  public static final String NIMBUS = "Nimbus";
  public static final String WINDOWS = "Windows";
  public static final String MOTIF = "Motif";
  public static final String[] LOOK_AND_FEELS = new String[]{SYSTEM, CROSS, NIMBUS, WINDOWS, MOTIF};

  public LookAndFeel(String laf) {
    try {
      if (laf.equals(NIMBUS)) {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
      } else if (laf.equals(CROSS)) {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      } else if (laf.equals(SYSTEM)) {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } else if (laf.equals(WINDOWS)) {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      } else if (laf.equals(MOTIF)) {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
      } else {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
    } catch (ClassNotFoundException ex) {
      Logger.getLogger(LookAndFeel.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      Logger.getLogger(LookAndFeel.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      Logger.getLogger(LookAndFeel.class.getName()).log(Level.SEVERE, null, ex);
    } catch (UnsupportedLookAndFeelException ex) {
      Logger.getLogger(LookAndFeel.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  //
  //--
  //--
}
