/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package exceptions;

/**
 *
 * @author ssoldatos
 */
public class NoSheetSelectedException extends ReceiptException{
  private static final long serialVersionUID = 23549856092380236L;

  public NoSheetSelectedException(String string) {
    super(string);
  }

}
