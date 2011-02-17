/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package panels.charts;

import java.io.File;
import java.io.FilenameFilter;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author ssoldatos
 */
public class ImageFileFilter extends FileFilter implements FilenameFilter{
 private int type;
  public static  String[] EXTENSIONS = {"png", "jpg", "gif"};

  public boolean accept(File dir, String name) {
    for (int i = 0; i < EXTENSIONS.length; i++) {
      String string = EXTENSIONS[i];
      if (!string.equals("") && name.endsWith(string)) {
        return true;
      }

    }
    return false;
  }

  @Override
  public boolean accept(File f) {
    if (f.isDirectory()) {
      return true;
    } else {
      for (int i = 0; i < EXTENSIONS.length; i++) {
      String string = EXTENSIONS[i];
      if (!string.equals("") && f.getName().endsWith(string)) {
        return true;
      }
    }
    }
    return false;
  }

  @Override
  public String getDescription() {
    return "Image files (*.png, *.jpg, *.gif)";
  }
}
