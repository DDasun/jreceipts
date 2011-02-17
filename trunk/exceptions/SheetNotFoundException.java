/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package exceptions;

/**
 *
 * @author ssoldatos
 */
public class SheetNotFoundException extends ReceiptException{
  private static final long serialVersionUID = 63466363636L;

  public SheetNotFoundException(String string) {
    super(string);
  }

}
