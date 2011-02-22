/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import components.Database;
import exceptions.ErrorMessages;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import receipts.Main;
import tools.Helper;

/**
 *
 * @author lordovol
 */
public class Type extends DBRecord {

  private int type_id;
  private String description;
  private int valid;
  private double multiplier;
  public static final String TABLE = "types";
  public static final String COLUMN_TYPE_ID = "type_id";
  public static final String COLUMN_DESCRIPTION = "description";
  public static final String COLUMN_VALID = "valid";
  public static final String COLUMN_MULTIPLIER = "multiplier";
  public static final String HEADER_TYPE_ID = "Α/Α";
  public static final String HEADER_DESCRIPTION = "Κατηγορία";
  public static final String HEADER_VALID = "Έγκυρη";
  public static final String HEADER_MULTIPLIER = "Ποσοστό";

  public Type(int type_id, String description, int valid, double multiplier) {
    super();
    this.type_id = type_id;
    this.description = description;
    this.valid = valid;
    this.multiplier = multiplier;
  }

  public static ComboBoxModel getModel() {
    model = new DefaultComboBoxModel(getCollection(true));
    return model;
  }

  @Override
  public String toString() {
    return getDescription();
  }

  public void save() throws SQLException {
    if (this.getType_id() == 0) {
      if (!exists()) {
        insert();
      } else {
        Helper.message("Το είδος " + getDescription() + " υπάρχει ήδη στη βάση\n Η εισαγωγή δεν έγινε", "Διπλή εγγραφή", JOptionPane.WARNING_MESSAGE);
      }
    } else {
      update();
    }
  }

  public boolean exists() throws SQLException {
    sql = "SELECT * FROM " + TABLE + " WHERE " + COLUMN_DESCRIPTION + " = '" + getDescription() + "'";
    rs = stmt.executeQuery(sql);
    if (rs.next()) {
      type_id = rs.getInt(COLUMN_TYPE_ID);
      description = rs.getString(COLUMN_DESCRIPTION);
      valid = rs.getInt(COLUMN_VALID);
      multiplier = rs.getDouble(COLUMN_MULTIPLIER);
      return true;
    }
    return false;
  }

  private void insert() throws SQLException {
    sql = "INSERT INTO " + TABLE + " (" + COLUMN_DESCRIPTION + ", " + COLUMN_VALID
        + ", " + COLUMN_MULTIPLIER + ") VALUES ('" + getDescription() + "'," + valid + "," + multiplier + ")";
    stmt.executeUpdate(sql);
  }

  private void update() throws SQLException {
    sql = "UPDATE " + TABLE + " set " + COLUMN_DESCRIPTION + " = '" + getDescription()
        + "', " + COLUMN_VALID + " = " + valid
        + ", " + COLUMN_MULTIPLIER + " =" + multiplier + "  WHERE " + COLUMN_TYPE_ID + " = " + getType_id();
    stmt.executeUpdate(sql);
  }

  public static void deleteById(int id) {
    try {
      sql = "DELETE FROM " + TABLE + " WHERE " + COLUMN_TYPE_ID + " = " + id;
      stmt.executeUpdate(sql);
    } catch (SQLException ex) {
      Helper.message("Σφάλμα στην βάση δεδομένων.\nΗ διαγραφή δεν έγινε", "SQL σφάλμα", JOptionPane.ERROR_MESSAGE);
      Main.log(Level.SEVERE, ErrorMessages.SQL_EXCEPTION, ex);
    }
  }

  /**
   * @return the type_id
   */
  public int getType_id() {
    return type_id;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @param type_id the type_id to set
   */
  public void setType_id(int type_id) {
    this.type_id = type_id;
  }

  public static Vector<Object> getCollection(boolean addHeader) {
    try {
      sql = "SELECT * FROM " + TABLE + " ORDER BY " + COLUMN_DESCRIPTION;
      rs = Database.stmt.executeQuery(sql);
      collection = new Vector<Object>();
      if (addHeader) {
        collection.add(new Type(0, "Επιλογή Είδους", 1, 1.0));
      }
      while (rs.next()) {
        Type t = new Type(rs.getInt(COLUMN_TYPE_ID),
            rs.getString(COLUMN_DESCRIPTION),
            rs.getInt(COLUMN_VALID),
            rs.getDouble(COLUMN_MULTIPLIER));
        collection.add(t);
      }
      return collection;
    } catch (SQLException ex) {
      Main.log(Level.SEVERE, ErrorMessages.SQL_EXCEPTION, ex);
      return null;
    }
  }

  public static String[] getComboBoxModel() {
    Vector<Object> col = getCollection(false);
    Iterator<Object> it = col.iterator();
    String[] mod = new String[col.size()];
    int i = 0;
    while (it.hasNext()) {
      Type type = (Type) it.next();
      mod[i] = type.toString();
      i++;
    }
    return mod;
  }

  /**
   * @return the valid
   */
  public boolean isValid() {
    return valid == 1 ? true : false;
  }

  /**
   * @param valid the valid to set
   */
  public void setValid(int valid) {
    this.valid = valid;
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
