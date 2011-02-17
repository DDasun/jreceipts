/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package importDb.base;

/**
 *
 * @author ssoldatos
 */
public interface ImportConstants {
  String DEFAULT_TYPE = "Διάφορα";
  int FILE_TYPE_ASCII = 0;
  int FILE_TYPE_XLS = 1;
  int FILE_TYPE_MDB = 2;
  int FILE_TYPE_ODS = 3;
  String NONE = "Δεν υπάρχει";
  int NOT_EXIST = -1;
  int AFM = 0;
  int AMOUNT = 1;
  int TYPE = 2;
  int DATE = 3;
  int MULTIPLIER = 4;
  String[] THOUSAND_DEL = {"",".",","};
  int THOUSAND_DEL_NONE = 0;
  int THOUSAND_DEL_STOP = 1;
  int THOUSAND_DEL_COMMA = 2;
  String[] DECIMAL_DEL = {",","."};
  int DECIMAL_DEL_COMMA = 0;
  int DECIMAL_DEL_STOP = 1;
   String[] MULTI_DEC_DEL = {",",".","%"};
  int MULTI_DEC_COMMA = 0;
  int MULTI_DEC_STOP = 1;
  int MULTI_DEC_PERC = 2;
}
