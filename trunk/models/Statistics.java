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
  public static final String COLUMN_MONTH = "month";
  public static final String COLUMN_TOTALS = "totals";
  public static final String COLUMN_AMOUNT = "amount";
  public static final String COLUMN_ID = "id";
  public static final String COLUMN_DESCRIPTION = "description";

  public Vector<Object> getMonthlyCollection() {
    try {
      sql = "SELECT strftime('%m', " + Receipt.COLUMN_BUY_DATE + ") AS "+COLUMN_MONTH+", "
          + "count(r." + Receipt.COLUMN_RECEIPT_ID + ") AS "+COLUMN_TOTALS+", "
          + "SUM(" + Receipt.COLUMN_AMOUNT + " * t." + Type.COLUMN_MULTIPLIER + ") AS "+COLUMN_AMOUNT+" "
          + " FROM " + Receipt.TABLE + " r LEFT JOIN " + Type.TABLE + " t"
          + " ON r." + Receipt.COLUMN_TYPE_ID + " = t." + Type.COLUMN_TYPE_ID
          + "  WHERE t." + Type.COLUMN_VALID + " = 1 AND r." + Receipt.COLUMN_VALID + " = 1 "
          + " AND  strftime('%Y', " + Receipt.COLUMN_BUY_DATE + ")= '" + Options.YEAR + "'"
          + " GROUP BY strftime('%m', " + Receipt.COLUMN_BUY_DATE + ")";
      rs = Database.stmt.executeQuery(sql);
      collection = new Vector<Object>();
      while (rs.next()) {
        MonthlyStats s = new MonthlyStats(rs.getString(COLUMN_MONTH));
        s.month = rs.getInt(COLUMN_MONTH);
        s.totals = rs.getInt(COLUMN_TOTALS);
        s.amount = rs.getDouble(COLUMN_AMOUNT);
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
      sql = "SELECT t." + Type.COLUMN_TYPE_ID + " AS "+COLUMN_ID+","
          + " t." + Type.COLUMN_DESCRIPTION + " AS "+COLUMN_DESCRIPTION+", "
          + "count(t." + Type.COLUMN_TYPE_ID + ") AS "+COLUMN_TOTALS+", "
          + "SUM(" + Receipt.COLUMN_AMOUNT + " * t." + Type.COLUMN_MULTIPLIER + ") AS "+COLUMN_AMOUNT+" "
          + " FROM " + Receipt.TABLE + " r LEFT JOIN " + Type.TABLE + " t"
          + " ON r." + Receipt.COLUMN_TYPE_ID + " = t." + Type.COLUMN_TYPE_ID + "  "
          + "WHERE t." + Type.COLUMN_VALID + " = 1 AND r." + Receipt.COLUMN_VALID + "=1 "
          + " AND  strftime('%Y', " + Receipt.COLUMN_BUY_DATE + ")= '" + Options.YEAR + "'"
          + " GROUP BY r." + Receipt.COLUMN_TYPE_ID;
      rs = Database.stmt.executeQuery(sql);
      collection = new Vector<Object>();
      while (rs.next()) {
        KindStats s = new KindStats(rs.getString(COLUMN_ID));
        s.description = rs.getString(COLUMN_DESCRIPTION);
        s.totals = rs.getInt(COLUMN_TOTALS);
        s.amount = rs.getDouble(COLUMN_AMOUNT);
        collection.add(s);
      }
      return collection;
    } catch (SQLException ex) {
      Main.log(Level.SEVERE, ErrorMessages.SQL_EXCEPTION, ex);
      return null;
    }
  }

  public class KindStats implements Comparable<KindStats> {

    public String description;
    public int totals;
    public double amount;

    private KindStats(String kind) {
    }

    public int compareTo(KindStats o) {
      if (this.amount > o.amount) {
        return -1;
      } else if (this.amount < o.amount) {
        return 1;
      }
      return 0;
    }
  }

  public class MonthlyStats implements Comparable<MonthlyStats> {

    public int month;
    public int totals;
    public double amount;

    private MonthlyStats(String month) {
    }

    public int compareTo(MonthlyStats o) {
      if (this.amount > o.amount) {
        return -1;
      } else if (this.amount < o.amount) {
        return 1;
      }
      return 0;
    }
  }
}
