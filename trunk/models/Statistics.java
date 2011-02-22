/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import components.Database;
import exceptions.ErrorMessages;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import receipts.Main;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class Statistics extends DBRecord {
  public static int KIND = 0;
  public static int MONTHLY = 1;

  public Vector<Object> getMonthlyCollection() {
    try {
      sql = "SELECT strftime('%m', buy_date) AS month, count(r.receipt_id) AS totals, SUM(amount * t.multiplier) AS amount "
              + " FROM receipts r LEFT JOIN types t"
              + " ON r.type_id = t.type_id  WHERE t.valid = 1 AND r.valid = 1 "
              +  " AND  strftime('%Y', buy_date)= '" + Options.YEAR +"'"
              + " GROUP BY strftime('%m', buy_date)";
      rs = Database.stmt.executeQuery(sql);
      collection = new Vector<Object>();
      while (rs.next()) {
        MonthlyStats s = new MonthlyStats(rs.getString("month"));
        s.month = rs.getInt("month");
        s.totals = rs.getInt("totals");
        s.amount = rs.getDouble("amount");
        collection.add(s);
      }
      return collection;
    } catch (SQLException ex) {
      Main.log(Level.SEVERE, ErrorMessages.SQL_EXCEPTION, ex);
      return null;
    }
  }

  public Vector<Object> getkindCollection() {
    try {
      sql = "SELECT t.type_id AS id, t.description AS description, count(t.type_id) AS totals, "
          + "SUM(amount * t.multiplier) AS amount "
              + " FROM receipts r LEFT JOIN types t"
              + " ON r.type_id = t.type_id  WHERE t.valid = 1 AND r.valid=1 "
              + " AND  strftime('%Y', buy_date)= '" + Options.YEAR +"'"
              + " GROUP BY r.type_id";
      rs = Database.stmt.executeQuery(sql);
      collection = new Vector<Object>();
      while (rs.next()) {
        KindStats s = new KindStats(rs.getString("id"));
        s.description = rs.getString("description");
        s.totals = rs.getInt("totals");
        s.amount = rs.getDouble("amount");
        collection.add(s);
      }
      return collection;
    } catch (SQLException ex) {
      Main.log(Level.SEVERE, ErrorMessages.SQL_EXCEPTION, ex);
      return null;
    }
  }

  public class KindStats implements Comparable<KindStats>{

    public String description;
    public int totals;
    public double amount;

    private KindStats(String kind) {
    }

    public int compareTo(KindStats o) {
      if(this.amount > o.amount){
        return -1;
      }else if(this.amount < o.amount){
        return 1;
      }
      return 0;
    }
  }

  public class MonthlyStats implements Comparable<MonthlyStats>{

    public int month;
    public int totals;
    public double amount;

    private MonthlyStats(String month) {
    }

    public int compareTo(MonthlyStats o) {
      if(this.amount > o.amount){
        return -1;
      }else if(this.amount < o.amount){
        return 1;
      }
      return 0;
    }
  }
}
