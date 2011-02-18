/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package components.buttons;

import java.net.URL;

/**
 *
 * @author ssoldatos
 */
public class Button extends AbstractButton{
  private static final long serialVersionUID = 2354252345354L;
  public static final String EXIT = "exit";
  public static final String OK = "ok";

  public Button() {
    super();
  }



  public Button(String type) {
    setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/"+type+".png")));
  }

}
