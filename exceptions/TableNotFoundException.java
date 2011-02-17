/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package exceptions;

/**
 *
 * @author ssoldatos
 */
public class TableNotFoundException extends ReceiptException{
  private static final long serialVersionUID = 7573476345353L;

  public TableNotFoundException(String string) {
    super(string);
  }

}
