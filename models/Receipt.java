/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import components.Database;
import exceptions.ErrorMessages;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import receipts.Main;
import tools.Helper;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class Receipt extends DBRecord {

  private int receipt_id;
  private int type_id;
  private String afm;
  private double amount;
  private Date date;
  private String comments;
  private int valid;
  private String type;
  private String dateForSQL;
  private double multiplier;
  public static final String TABLE = "receipts";
  public static final String COLUMN_RECEIPT_ID = "receipt_id";
  public static final String COLUMN_AFM = "afm";
  public static final String COLUMN_AMOUNT = "amount";
  public static final String COLUMN_BUY_DATE = "buy_date";
  public static final String COLUMN_TYPE_ID = "type_id";
  public static final String COLUMN_COMMENTS = "comments";
  public static final String COLUMN_VALID = "valid";
  public static final String HEADER_RECEIPT_ID = "receipt_id";
  public static final String HEADER_AFM = "afm";
  public static final String HEADER_AMOUNT = "amount";
  public static final String HEADER_BUY_DATE = "date";
  public static final String HEADER_TYPE_ID = "type_id";
  public static final String HEADER_COMMENTS = "comments";
  public static final String HEADER_VALID = "valid";

  /**
   * 
   * @param receipt_id
   * @param afm
   * @param amount
   * @param date
   * @param type_id
   * @param comments
   */
  public Receipt(int receipt_id, String afm, double amount, Date date, int type_id, String comments, boolean validReceipt) {
    super();
    this.type_id = type_id;
    this.comments = comments;
    this.receipt_id = receipt_id;
    this.afm = afm;
    this.amount = amount;
    this.date = date;
    this.valid = validReceipt ? 1 : 0;
    dateForSQL = Helper.convertDateForSQL(date);
  }

  public Receipt(int receipt_id) {
    super();
    this.receipt_id = receipt_id;
  }

  public void save() throws SQLException {
    if (getReceipt_id() == 0) {
      if (exists()) {
        int c = Helper.confirm("Διπλή εγγραφή", "Η εγγραφή υπάρχει ήδη στη βάση.\nΘέλετε να γίνει η εισαγωγή;");
        //Helper.message("Η εγγραφή υπήρχε ήδη στη βάση.\nΔεν έγινε εισαγωγή", "Διπλή εγγραφή", JOptionPane.ERROR_MESSAGE);
        if (c == JOptionPane.YES_OPTION) {
          insert();
        }
      } else {
        insert();
      }
    } else {
      update();
    }
  }

  private void insert() throws SQLException {
    sql = "INSERT INTO " + TABLE + " (" + COLUMN_AFM + ", " + COLUMN_AMOUNT
        + ", " + COLUMN_BUY_DATE + ", " + COLUMN_TYPE_ID + ", " + COLUMN_COMMENTS + ", " + COLUMN_VALID + ") VALUES "
        + "('" + getAfm() + "','" + getAmount() + "','" + dateForSQL + "', " + getType_id() + ", '" + getComments() + "'," + getValid() + ")";
    stmt.executeUpdate(sql);
  }

  private void update() throws SQLException {
    sql = "UPDATE " + TABLE + " SET " + COLUMN_AFM + " = '" + getAfm() + "', " + COLUMN_AMOUNT + " = '" + amount + "',"
        + " " + COLUMN_BUY_DATE + " = '" + dateForSQL + "' , " + COLUMN_TYPE_ID + " = '" + getType_id()
        + "', " + COLUMN_COMMENTS + " = '" + getComments() + "', " + COLUMN_VALID + "=" + valid
        + " WHERE " + COLUMN_RECEIPT_ID + " =" + getReceipt_id();
    stmt.executeUpdate(sql);
  }

  private boolean exists() throws SQLException {
    sql = "SELECT * FROM " + TABLE + " WHERE " + COLUMN_AFM + " = '" + getAfm() + "' AND " + COLUMN_BUY_DATE + " = '"
        + dateForSQL
        + "' AND " + COLUMN_AMOUNT + " = '" + getAmount() + "'";
    rs = stmt.executeQuery(sql);
    if (rs.next()) {
      return true;
    }
    return false;
  }

  public static void deleteById(int id) {
    try {
      sql = "UPDATE " + TABLE + " SET " + COLUMN_VALID + " = 0 WHERE " + COLUMN_RECEIPT_ID + " = " + id;
      stmt.executeUpdate(sql);
    } catch (SQLException ex) {
      Helper.message("Σφάλμα στην βάση δεδομένων.\nΗ διαγραφή δεν έγινε", "SQL σφάλμα", JOptionPane.ERROR_MESSAGE);
      Main.log(Level.SEVERE, "Σφάλμα στην βάση δεδομένων.\nΗ διαγραφή δεν έγινε", ex);
    }
  }

  public static void restoreById(int id) {
    try {
      sql = "UPDATE " + TABLE + " SET " + COLUMN_VALID + " = 1 WHERE " + COLUMN_RECEIPT_ID + " = " + id;
      stmt.executeUpdate(sql);
    } catch (SQLException ex) {
      Helper.message("Σφάλμα στην βάση δεδομένων.\nΗ επαναφορά δεν έγινε", "SQL σφάλμα", JOptionPane.ERROR_MESSAGE);
      Main.log(Level.SEVERE, "Σφάλμα στην βάση δεδομένων.\nΗ επαναφορά δεν έγινε", ex);
    }
  }

  public static Vector<Object> getCollection(boolean addHeader) {
    return getCollection(addHeader, "");
  }

  public static Vector<Object> getCollection(boolean addHeader, String criteria) {
    return getCollection(addHeader, "", true);
  }

  public static Vector<Object> getCollection(boolean addHeader, String criteria, boolean valid) {
    try {
      if (criteria.equals("")) {
        criteria = " WHERE strftime('%Y', " + COLUMN_BUY_DATE + ")= '" + Options.YEAR + "'";
      } else {
        criteria += " AND  strftime('%Y', " + COLUMN_BUY_DATE + ")= '" + Options.YEAR + "'";
      }
      sql = "SELECT r.*,t." + Type.COLUMN_DESCRIPTION + ",t." + Type.COLUMN_MULTIPLIER
          + " FROM " + TABLE + "  r "
          + "INNER JOIN " + Type.TABLE + " t ON r." + COLUMN_TYPE_ID + " = t." + Type.COLUMN_TYPE_ID + " "
          + criteria + " AND r." + COLUMN_VALID + " = " + (valid ? 1 : 0) + " ORDER BY " + COLUMN_BUY_DATE + " DESC";

      rs = Database.stmt.executeQuery(sql);
      collection = new Vector<Object>();
      if (addHeader) {
        //collection.add(new Receipt(0, "Επιλογή Απόδειξης"));
      }
      while (rs.next()) {
        Receipt r = new Receipt(rs.getInt(COLUMN_RECEIPT_ID));
        r.setAfm(rs.getString(COLUMN_AFM));
        r.setAmount(rs.getDouble(COLUMN_AMOUNT));
        r.setDate(Helper.convertStringFromSqlToDate(rs.getString(COLUMN_BUY_DATE)));
        r.setType_id(rs.getInt(COLUMN_TYPE_ID));
        r.setType(rs.getString(Type.COLUMN_DESCRIPTION));
        r.setComments(rs.getString(COLUMN_COMMENTS));
        r.setMultiplier(rs.getDouble(Type.COLUMN_MULTIPLIER));
        r.setValid(rs.getInt(COLUMN_VALID));
        collection.add(r);
      }
      return collection;
    } catch (SQLException ex) {
      Main.log(Level.SEVERE, ErrorMessages.SQL_EXCEPTION, ex);
      return null;
    }
  }

  public static int getCount(String where) {
    if (where.equals("")) {
      sql = "SELECT count(*) AS totals FROM " + TABLE + " r LEFT JOIN " + Type.TABLE + " t "
          + "ON r." + COLUMN_TYPE_ID + " = t." + Type.COLUMN_TYPE_ID + " "
          + "WHERE t." + Type.COLUMN_VALID + " = 1 AND r." + COLUMN_VALID + " = 1"
          + " AND  strftime('%Y', " + COLUMN_BUY_DATE + ")= '" + Options.YEAR + "'";
    } else {
      sql = "SELECT count(*) AS totals FROM " + TABLE + " r LEFT JOIN " + Type.TABLE + " t "
          + "ON r." + COLUMN_TYPE_ID + " = t." + Type.COLUMN_TYPE_ID + " "
          + "WHERE t." + Type.COLUMN_VALID + " = 1 AND r." + COLUMN_VALID + " = 1 AND " + where
          + " AND  strftime('%Y', " + COLUMN_BUY_DATE + ")= '" + Options.YEAR + "'";
    }
    try {
      rs = Database.stmt.executeQuery(sql);
      while (rs.next()) {
        return rs.getInt("totals");
      }
      return 0;
    } catch (SQLException ex) {
      Main.log(Level.SEVERE, ErrorMessages.SQL_EXCEPTION, ex);
      return 0;
    }

  }

  public static float getAmount(String where) {
    if (where.equals("")) {
      sql = "SELECT SUM(" + COLUMN_AMOUNT + " * " + Type.COLUMN_MULTIPLIER + ") AS totals "
          + "FROM " + TABLE + " r LEFT JOIN " + Type.TABLE + " t "
          + "ON r." + COLUMN_TYPE_ID + " = t." + Type.COLUMN_TYPE_ID + " "
          + "WHERE t." + Type.COLUMN_VALID + " = 1 AND r." + COLUMN_VALID + " = 1"
          + " AND  strftime('%Y', " + COLUMN_BUY_DATE + ")= '" + Options.YEAR + "'";
    } else {
      sql = "SELECT SUM(" + COLUMN_AMOUNT + " * " + Type.COLUMN_MULTIPLIER + ") AS totals "
          + "FROM " + TABLE + " r LEFT JOIN " + Type.TABLE + " t "
          + "ON r." + COLUMN_TYPE_ID + " = t." + Type.COLUMN_TYPE_ID + " "
          + "WHERE t." + Type.COLUMN_VALID + " = 1 AND r." + COLUMN_VALID + " = 1 AND " + where
          + " AND  strftime('%Y', " + COLUMN_BUY_DATE + ")= '" + Options.YEAR + "'";
    }
    try {
      rs = Database.stmt.executeQuery(sql);
      while (rs.next()) {
        return rs.getFloat("totals");
      }
      return 0.00F;
    } catch (SQLException ex) {
      Main.log(Level.SEVERE, ErrorMessages.SQL_EXCEPTION, ex);
      return 0.00F;
    }

  }

  public static void updateField(String field, String newValue, String oldValue) throws SQLException {
    sql = "UPDATE " + TABLE + " SET " + field + " = '" + newValue + "' WHERE " + field + " = '" + oldValue + "'";
    Database.stmt.executeUpdate(sql);
  }

  /**
   * @return the receipt_id
   */
  public int getReceipt_id() {
    return receipt_id;
  }

  /**
   * @param receipt_id the receipt_id to set
   */
  public void setReceipt_id(int receipt_id) {
    this.receipt_id = receipt_id;
  }

  /**
   * @return the type_id
   */
  public int getType_id() {
    return type_id;
  }

  /**
   * @param type_id the type_id to set
   */
  public void setType_id(int type_id) {
    this.type_id = type_id;
  }

  /**
   * @return the afm
   */
  public String getAfm() {
    return afm;
  }

  /**
   * @param afm the afm to set
   */
  public void setAfm(String afm) {
    this.afm = afm;
  }

  /**
   * @return the amount
   */
  public double getAmount() {
    return this.amount;
  }

  /**
   * @param amount the amount to set
   */
  public void setAmount(double amount) {
    this.amount = amount;
  }

  /**
   * @return the date
   */
  public Date getDate() {
    return date;
  }

  /**
   * @param date the date to set
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * @return the comments
   */
  public String getComments() {
    return comments;
  }

  /**
   * @param comments the comments to set
   */
  public void setComments(String comments) {
    this.comments = comments;
  }

  /**
   * @return the type
   */
  public String getType() {
    try {
      return Type.getFieldById(Type.COLUMN_TYPE_ID, type_id, Type.COLUMN_DESCRIPTION, Type.TABLE);
    } catch (SQLException ex) {
      Main.log(Level.SEVERE, null, ex);
      return "";
    }
  }

  /**
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * @return the multiplier
   */
  public double getMultiplier() {
    return multiplier;
  }

  /**
   * @param multiplier the multiplier to set
   */
  public void setMultiplier(double multiplier) {
    this.multiplier = multiplier;
  }

  /**
   * @return the valid
   */
  public int getValid() {
    return valid;
  }

  /**
   * @param valid the valid to set
   */
  public void setValid(int valid) {
    this.valid = valid;
  }
}
