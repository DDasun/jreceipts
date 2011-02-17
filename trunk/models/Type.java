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
    sql = "SELECT * FROM types WHERE description = '" + getDescription() + "'";
    rs = stmt.executeQuery(sql);
    if (rs.next()) {
      type_id = rs.getInt("type_id");
      description = rs.getString("description");
      valid = rs.getInt("valid");
      multiplier = rs.getDouble("multiplier");
      return true;
    }
    return false;
  }

  private void insert() throws SQLException {
    sql = "INSERT INTO types (description, valid, multiplier) VALUES ('" + getDescription() + "'," + valid + ","+multiplier+")";
    stmt.executeUpdate(sql);
  }

  private void update() throws SQLException {
    sql = "UPDATE types set description = '" + getDescription() + "', valid = " + valid +", multiplier ="+multiplier+"  WHERE type_id = " + getType_id();
    stmt.executeUpdate(sql);
  }

  public static void deleteById(int id) {
    try {
      sql = "DELETE FROM types WHERE type_id = " + id;
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
      sql = "SELECT * FROM types ORDER BY description";
      rs = Database.stmt.executeQuery(sql);
      collection = new Vector<Object>();
      if (addHeader) {
        collection.add(new Type(0, "Επιλογή Είδους", 1,1.0));
      }
      while (rs.next()) {
        Type t = new Type(rs.getInt("type_id"), rs.getString("description"), rs.getInt("valid"), rs.getDouble("multiplier"));
        collection.add(t);
      }
      return collection;
    } catch (SQLException ex) {
      Main.log(Level.SEVERE, ErrorMessages.SQL_EXCEPTION, ex);
      return null;
    }
  }

  public static String[] getComboBoxModel(){
    Vector<Object> col = getCollection(false);
    Iterator<Object> it = col.iterator();
    String[] mod = new String[col.size()];
    int i=0;
    while (it.hasNext()) {
      Type type = (Type)it.next();
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
