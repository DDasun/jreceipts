/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;


/**
 *
 * @author lordovol
 */
public class DBRecord {
  protected static Statement stmt;
  protected static String sql = "";
  protected static ResultSet rs;
  protected static Vector<Object> collection = new Vector<Object>();
  protected static ComboBoxModel model;

 
  public DBRecord() {
    DBRecord.stmt = Database.stmt;
  }

  public static ResultSet executeQuery(String query){
    try {
      return stmt.executeQuery(sql);
    } catch (SQLException ex) {
      receipts.Main.log(Level.SEVERE, null, ex);
      return null;
    }
  }

  public static int getIdByField(String table , String field, String idField, String value) throws SQLException {
    sql = "SELECT " + idField + " FROM " + table + " WHERE " +field  + " = '" + value + "'";
    rs = stmt.executeQuery(sql);
    if(rs.next()){
      return rs.getInt(idField);
    }
    return 0;
  }

  public static String getFieldById(String idField,int value,String field,String table) throws SQLException{
sql = "SELECT " + field + " FROM " + table + " WHERE " + idField + " = " + value ;
    rs = stmt.executeQuery(sql);
    if(rs.next()){
      return rs.getString(field);
    }

    return null;
  }

  public static Object getFieldByField(String table, String fieldToGet,String field, String value, Object defaultValue) throws SQLException{
    sql = "SELECT " + fieldToGet + " FROM " + table + " WHERE " + field + " = '" + value +"'";
    rs = stmt.executeQuery(sql);
    if(rs.next()){
      return rs.getObject(fieldToGet);
    }

    return defaultValue;
  }


}
