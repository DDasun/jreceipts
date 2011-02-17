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

  public  Afm(int afm_id, String afm, String name) {
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
    sql = "SELECT * FROM afm WHERE afm = '" + getAfm() + "'";
    rs = stmt.executeQuery(sql);
    if (rs.next()) {
      return true;
    }
    return false;
  }

  private void insert() throws SQLException {
    sql = "INSERT INTO afm (afm, name) VALUES ( '" + getAfm() + "', '" + getName() + "')";
    stmt.executeUpdate(sql);
  }

  private void update() throws SQLException {
    sql = "UPDATE afm set afm = '" + getAfm() + "', name = '" + getName() + "' WHERE afm_id = " + getAfm_id();
    stmt.executeUpdate(sql);
  }

  public static Vector<Object> getCollection(boolean addHeader) {
    try {
      sql = "SELECT * FROM afm ORDER BY name";
      rs = Database.stmt.executeQuery(sql);
      collection = new Vector<Object>();
      if (addHeader) {
        collection.add(new Afm(0, "Επιλογή ΑΦΜ",""));
      }
      while (rs.next()) {
        Afm a = new Afm(rs.getInt("afm_id"), rs.getString("afm"), rs.getString("name"));
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
      sql = "DELETE FROM afm WHERE afm_id = " + id;
      stmt.executeUpdate(sql);
    } catch (SQLException ex) {
      Helper.message("Σφάλμα στην βάση δεδομένων.\nΗ διαγραφή δεν έγινε","SQL σφάλμα",JOptionPane.ERROR_MESSAGE);
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
      sql = "SELECT t.type_id, t.description, t.valid, t.multiplier FROM types t INNER JOIN receipts r " + "ON t.type_ID = r.type_ID WHERE r.afm = '" + this.afm + "' LIMIT 1";
      ResultSet result = stmt.executeQuery(sql);
      while(result.next()){
        int type_id = result.getInt("type_id");
        String description = result.getString("description");
        int valid = result.getInt("valid");
        Double multiplier = result.getDouble("multiplier");
        return new Type(type_id, description, valid,multiplier);
      }
      return null;
    } catch (SQLException ex) {
      return null;
    }
  }
}
