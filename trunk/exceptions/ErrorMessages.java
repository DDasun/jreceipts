/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author ssoldatos
 */
public class ErrorMessages {

  public static String SQL_EXCEPTION = "Υπήρξε κάποιο σφάλμα στη βάση δεδομένων";
  public static String AMOUNT_FORMAT = "Το ποσό πρέπει να είναι της μορφής ΧΧΧΧ.ΧΧ";
  public static String DB_FILE_NOT_FOUND = "Δεν βρέθηκε το αρχείο της βάσης";
  public static String DB_IO_ERROR = "Σφάλμα εγγραφής/ανάγνωσης στο αρχείο της βάσης";
  public static String ONLY_ONE_DB ="Δεν υπάρχουν περισσότερες από 1 βάσεις για να διαλέξετε";
  public static String EMPTY_DATABASE = "Δεν υπάρχουν αποδείξεις στη βάση";
  public static String CANCEL_DATABASE = "Ακύρωση δημιουργίας βάσης";
  public static String EMPTY_DATABASE_NAME = "Το όνομα της βάσης δεν μπορεί να είναι κενό";
  public static String CANCEL_RENAME = "Ακύρωση μετονομασίας";
  public static String NO_DB_TO_DELETE = "Δεν υπάρχει κάποια βάση για  να διαγράψετε";
  public static String DB_EXISTS = "Η βάση υπάρχει ήδη";
  public static String DELETE_ACTIVE_DB = "Δεν μπορείτε να διαγράψετε την βάση που χρησιμοποιείτε";
  public static String CREATE_DB_ERROR = "Κάποιο πρόβλημα υπήρξε στην δημιουργία της βάσης";
  public static String CREATE_DB_BACKUP_ERROR = "Το backup της βάσης δεν δημιουργήθηκε";
  public static String DATE_FORMAT = "Wrong date format";
  public static String FILE_NOT_FOUND = "Το αρχείο δεν βρέθηκε";
  public static String IO = "Σφάλμα εγγραφής/ανάγνωσης στο αρχείο";
  public static String GRAPH_SAVE ="Κάποιο σφάλμα συνέβη στην αποθήκευση του γραφήματος";
  public static String NOT_IMAGE_FILE = "Το αρχείο πρέπει να είναι Png, jpg ή gif";

  private ErrorMessages() {
  }
}
