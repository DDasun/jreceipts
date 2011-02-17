/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools;

/**
 *
 * @author ssoldatos
 */
public interface Taxes {
  /**  Μέγιστο ποσό αποδείξεων για δικαιούχο μόνο : 15000*/
  float MAX_OWNER_TOTAL = 15000;
  /**  Μέγιστο ποσό αποδείξεων για δικαιούχο και σύζυγο : 30000*/
  float MAX_OWNER_MARRIED_TOTAL = 30000;
  /**  Ποσό για το οποίο δεν χρειάζονται αποδείξεις : 6000*/
  double MIN_EARN_THAT_NOT_NEEDS_RECEIPT = 6000;
  /** Μέγιστο Εισόδημα  για το οποίο μετράνε αποδείξεις : 48000*/
  double MAX_EARN = 48000;
  /**  Μέγιστο ποσό αποδείξεων : 12000*/
  double MAX_NEED = 12000;
  /**  Ποσοστό για αποδείξεις κάτω του {@link #MAX_NEED} : 10%*/
  double PER_CENT_FOR_VALID_RECEIPTS = 10;
  /**  Ποσοστό για αποδείξεις πάνω από το {@link #MAX_NEED} : 30%*/
  double PER_CENT_FOR_INVALID_RECEIPTS = 30;
  /** Ποσοστό των αποδείξεων για το οποίο υπάρχει ωφέλεια ή όχι : 10%*/
  double PER_CENT_TAX_BONUS = 10;
}
