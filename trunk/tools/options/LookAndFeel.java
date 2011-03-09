/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.options;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author ssoldatos
 */
public class LookAndFeel {




  public LookAndFeel(String laf) {
    try {
      UIManager.setLookAndFeel(getLookAndFeelClass(laf));
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

  private String getLookAndFeelClass(String name) {
    LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
    LookAndFeelInfo[] lafsEx = getAuxiliaryLookAndFeels();
    for (int i = 0; i < lafs.length; i++) {
      LookAndFeelInfo laf = lafs[i];
      if (laf.getName().equals(name)) {
        return laf.getClassName();
      }
    }
    for (int i = 0; i < lafsEx.length; i++) {
      LookAndFeelInfo laf = lafsEx[i];
      if (laf.getName().equals(name)) {
        return laf.getClassName();
      }
    }
    return UIManager.getCrossPlatformLookAndFeelClassName();
  }

   private static LookAndFeelInfo[] getAuxiliaryLookAndFeels() {
    LookAndFeelInfo[] l = new LookAndFeelInfo[2];
    l[0] = new LookAndFeelInfo("Liquid", "com.birosoft.liquid.LiquidLookAndFeel");
    l[1] = new LookAndFeelInfo("Smart", "com.jtattoo.plaf.smart.SmartLookAndFeel");
    return l;
  }

  public static String[] getLookAndFeels() {
    LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
    LookAndFeelInfo[] lafsEx = getAuxiliaryLookAndFeels();
    String[] names = new String[lafs.length + lafsEx.length];
    for (int i = 0; i < lafs.length; i++) {
      LookAndFeelInfo laf = lafs[i];
      names[i] = laf.getName();
    }
    for (int i = 0; i < lafsEx.length; i++) {
      LookAndFeelInfo laf = lafsEx[i];
      names[lafs.length+i] = laf.getName();
    }
    return names;
  }
}
