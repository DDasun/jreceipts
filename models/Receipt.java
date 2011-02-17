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
import tools.Options;

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
  private String type;
  private String dateForSQL;
  private double multiplier;

  /**
   * 
   * @param receipt_id
   * @param afm
   * @param amount
   * @param date
   * @param type_id
   * @param comments
   */
  public Receipt(int receipt_id, String afm, double amount, Date date, int type_id, String comments) {
    super();
    this.type_id = type_id;
    this.comments = comments;
    this.receipt_id = receipt_id;
    this.afm = afm;
    this.amount = amount;
    this.date = date;
    dateForSQL = Helper.convertDateForSQL(date);
  }

  public Receipt(int receipt_id) {
    super();
    this.receipt_id = receipt_id;
  }

  public void save() throws SQLException {
    if (getReceipt_id() == 0) {
      if (exists()) {
        Helper.message("Η εγγραφή υπήρχε ήδη στη βάση.\nΔεν έγινε εισαγωγή", "Διπλή εγγραφή", JOptionPane.ERROR_MESSAGE);
      } else {
        insert();
      }
    } else {
      update();
    }
  }

  private void insert() throws SQLException {
    sql = "INSERT INTO receipts (afm, amount, buy_date, type_id, comments) VALUES "
        + "('" + getAfm() + "','" + getAmount() + "','" + dateForSQL + "', " + getType_id() + ", '" + getComments() + "')";
    stmt.executeUpdate(sql);
  }

  private void update() throws SQLException {
    sql = "UPDATE receipts SET afm = '" + getAfm() + "', amount = '" + amount + "',"
        + " buy_date = '" + dateForSQL + "' , type_id = '" + getType_id() + "', comments = '" + getComments() + "'"
        + " WHERE receipt_id =" + getReceipt_id();
    stmt.executeUpdate(sql);
  }

  private boolean exists() throws SQLException {
    sql = "SELECT * FROM receipts WHERE afm = '" + getAfm() + "' AND buy_date = '"
        + dateForSQL
        + "' AND amount = '" + getAmount() + "'";
    rs = stmt.executeQuery(sql);
    if (rs.next()) {
      return true;
    }
    return false;
  }

  public static void deleteById(int id) {
    try {
      sql = "DELETE FROM receipts WHERE receipt_id = " + id;
      stmt.executeUpdate(sql);
    } catch (SQLException ex) {
      Helper.message("Σφάλμα στην βάση δεδομένων.\nΗ διαγραφή δεν έγινε", "SQL σφάλμα", JOptionPane.ERROR_MESSAGE);
      Main.log(Level.SEVERE, "Σφάλμα στην βάση δεδομένων.\nΗ διαγραφή δεν έγινε", ex);
    }
  }

  public static Vector<Object> getCollection(boolean addHeader) {
    return getCollection(addHeader, "");
  }

  public static Vector<Object> getCollection(boolean addHeader, String criteria) {
    try {
      if(criteria.equals("")){
        criteria = " WHERE strftime('%Y', buy_date)= '" + Options.YEAR +"'";
      } else {
        criteria += " AND  strftime('%Y', buy_date)= '" + Options.YEAR +"'";
      }
      sql = "SELECT r.*,t.description,t.multiplier FROM receipts  r "
          + "INNER JOIN types t ON r.type_id = t.type_id "+criteria +" ORDER BY buy_date";
          
      rs = Database.stmt.executeQuery(sql);
      collection = new Vector<Object>();
      if (addHeader) {
        //collection.add(new Receipt(0, "Επιλογή Απόδειξης"));
      }
      while (rs.next()) {
        Receipt r = new Receipt(rs.getInt("receipt_id"));
        r.setAfm(rs.getString("afm"));
        r.setAmount(rs.getDouble("amount"));
        r.setDate(Helper.convertStringFromSqlToDate(rs.getString("buy_date")));
        r.setType_id(rs.getInt("type_id"));
        r.setType(rs.getString("description"));
        r.setComments(rs.getString("comments"));
        r.setMultiplier(rs.getDouble("multiplier"));
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
      sql = "SELECT count(*) AS totals FROM receipts LEFT JOIN types "
          + "ON receipts.type_id = types.type_id WHERE valid = 1"
           + " AND  strftime('%Y', buy_date)= '" + Options.YEAR +"'";
    } else {
      sql = "SELECT count(*) AS totals FROM receipts LEFT JOIN types "
          + "ON receipts.type_id = types.type_id WHERE valid = 1 AND " + where
          + " AND  strftime('%Y', buy_date)= '" + Options.YEAR +"'";
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
      sql = "SELECT SUM(amount*multiplier) AS totals FROM receipts LEFT JOIN types "
          + "ON receipts.type_id = types.type_id WHERE valid = 1"
          + " AND  strftime('%Y', buy_date)= '" + Options.YEAR +"'";
    } else {
      sql = "SELECT SUM(amount*multiplier) AS totals FROM receipts LEFT JOIN types "
          + "ON receipts.type_id = types.type_id WHERE valid = 1 AND " + where
          + " AND  strftime('%Y', buy_date)= '" + Options.YEAR +"'";
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
    sql = "UPDATE receipts SET " + field + " = '" + newValue + "' WHERE " + field + " = '" + oldValue + "'";
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
      return Type.getFieldById("type_id", type_id, "description", "types");
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
}
