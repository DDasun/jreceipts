/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import components.Database;
import exceptions.ErrorMessages;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import receipts.Main;
import tools.Helper;

/**
 *
 * @author lordovol
 */
public class Afm extends DBRecord {

  private String afm;
  private int afm_id;
  private String name;
  public static final String TABLE = "afm";
  public static final String COLUMN_AFM_ID = "afm_id";
  public static final String COLUMN_AFM = "afm";
  public static final String COLUMN_NAME = "name";
  public static final String HEADER_AFM_ID = "Α/Α";
  public static final String HEADER_AFM = "Α.Φ.Μ.";
  public static final String HEADER_NAME = "Εταιρεία";

  public Afm(int afm_id, String afm, String name) {
    super();
    this.afm_id = afm_id;
    this.afm = afm;
    this.name = name;
  }

  public static ComboBoxModel getModel() {
    model = new DefaultComboBoxModel(getCollection(true));
    return model;
  }

  public static int getId(String type) {
    int type_id = 0;
    return type_id;
  }

  @Override
  public String toString() {
    return getName();
  }

  public void save() throws SQLException {
    if (this.getAfm_id() == 0) {
      if (!exists()) {
        insert();
      } else {
        Helper.message("Το είδος " + getName() + " υπάρχει ήδη στη βάση\n Η εισαγωγή δεν έγινε", "Διπλή εγγραφή", JOptionPane.WARNING_MESSAGE);
      }
    } else {
      update();
    }
  }

  private boolean exists() throws SQLException {
    sql = "SELECT * FROM " + TABLE + " WHERE " + COLUMN_AFM + " = '" + getAfm() + "'";
    rs = stmt.executeQuery(sql);
    if (rs.next()) {
      return true;
    }
    return false;
  }

  private void insert() throws SQLException {
    sql = "INSERT INTO " + TABLE + " (" + COLUMN_AFM + ", " + COLUMN_NAME + ") "
        + "VALUES ( '" + getAfm() + "', '" + getName() + "')";
    stmt.executeUpdate(sql);
  }

  private void update() throws SQLException {
    sql = "UPDATE " + TABLE + " set " + COLUMN_AFM + " = '" + getAfm() +
        "', " + COLUMN_NAME + " = '" + getName() + "' WHERE " + COLUMN_AFM_ID + " = " + getAfm_id();
    stmt.executeUpdate(sql);
  }

  public static Vector<Object> getCollection(boolean addHeader) {
    try {
      sql = "SELECT * FROM " + TABLE + " ORDER BY " + COLUMN_NAME;
      rs = Database.stmt.executeQuery(sql);
      collection = new Vector<Object>();
      if (addHeader) {
        collection.add(new Afm(0, "Επιλογή ΑΦΜ", ""));
      }
      while (rs.next()) {
        Afm a = new Afm(rs.getInt(COLUMN_AFM_ID), rs.getString(COLUMN_AFM), rs.getString(COLUMN_NAME));
        collection.add(a);
      }
      return collection;
    } catch (SQLException ex) {
      Main.log(Level.SEVERE, ErrorMessages.SQL_EXCEPTION, ex);
      return null;
    }
  }

  public static void deleteById(int id) {
    try {
      sql = "DELETE FROM " + TABLE + " WHERE " + COLUMN_AFM_ID + " = " + id;
      stmt.executeUpdate(sql);
    } catch (SQLException ex) {
      Helper.message("Σφάλμα στην βάση δεδομένων.\nΗ διαγραφή δεν έγινε", "SQL σφάλμα", JOptionPane.ERROR_MESSAGE);
      Main.log(Level.SEVERE, "Σφάλμα στην βάση δεδομένων.\nΗ διαγραφή δεν έγινε", ex);
    }
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
   * @return the afm_id
   */
  public int getAfm_id() {
    return afm_id;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the kind
   */
  public Type getType() {
    try {
      sql = "SELECT t." + Type.COLUMN_TYPE_ID + ", t." + Type.COLUMN_DESCRIPTION +
          ", t."+Type.COLUMN_VALID+", t."+Type.COLUMN_MULTIPLIER+
          " FROM "+Type.TABLE+" t INNER JOIN "+Receipt.TABLE+" r " + "ON t." +
          Type.COLUMN_TYPE_ID + " = r."+Receipt.COLUMN_TYPE_ID+" WHERE r."+Receipt.COLUMN_AFM+
          " = '" + this.afm + "' LIMIT 1";
      ResultSet result = stmt.executeQuery(sql);
      while (result.next()) {
        int type_id = result.getInt(Type.COLUMN_TYPE_ID);
        String description = result.getString(Type.COLUMN_DESCRIPTION);
        int valid = result.getInt(Type.COLUMN_VALID);
        Double multiplier = result.getDouble(Type.COLUMN_MULTIPLIER);
        return new Type(type_id, description, valid, multiplier);
      }
      return null;
    } catch (SQLException ex) {
      return null;
    }
  }
}
