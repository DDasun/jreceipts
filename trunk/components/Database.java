/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import exceptions.ErrorMessages;
import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import models.Afm;
import models.Receipt;
import models.Type;
import receipts.Main;
import tools.Helper;
import tools.options.Options;

/**
 *
 * @author lordovol
 */
public class Database {

  public static Connection conn;
  public static Statement stmt;
  public static String db;

  public static void createConnection(boolean ask) {
    String[] databases = getDatabases();
    try {
      if (databases.length == 0) {
        createDb();
        return;
      } else if (databases.length == 1) {
        db = databases[0];
        db = db.replaceAll(".db$", "");
      } else {
        if (loadDefaultDb() && !ask) {
          db = Options.toString(Options.DEFAULT_DATABASE);
        } else {
          db = Helper.ask("Επιλογή Βάσης", "Επιλέξτε τη βάση που θέλετε να χρησιμοποιήσετε", databases);
        }
        if (!db.equals("")) {
          db = db.replaceAll(".db$", "");
        } else {
          if (Options.toString(Options.DATABASE).equals("")) {
            System.exit(0);
          } else {
            db = Options.toString(Options.DATABASE);
          }
        }
      }
      Class.forName("org.sqlite.JDBC");
      conn = DriverManager.getConnection("jdbc:sqlite:" + Options.USER_DIR + "/" + Options.DB_PATH + "/" + db + ".db");
      stmt = conn.createStatement();
      Options.setOption(Options.DATABASE, db);
    } catch (SQLException ex) {
      Main.log(Level.SEVERE, "Could not connect to the SQLite database", ex);
    } catch (ClassNotFoundException ex) {
      Main.log(Level.SEVERE, "Could not find SQLite class", ex);
    }
  }

  public static boolean createDb() throws ClassNotFoundException {
    db = Helper.ask("Δημιουργία Βάσης", "Πληκτρολογήστε το όνομα της βάσης που θέλετε να δημιουργήσετε");
    if (!db.equals("")) {
      if (new File(Options.USER_DIR + "/" + Options.DB_PATH + "/" + db + ".db").exists()) {
        Helper.message("Η βάση " + db + " υπάρχει ήδη", "Δημιουργία βάσης", JOptionPane.ERROR_MESSAGE);
        createDb();
      }
      db = db.replaceAll(".db$", "");
      return createDb(db);
    }
    return false;

  }

  public static boolean createDb(String db) throws ClassNotFoundException {
    try {
      if (new File(Options.USER_DIR + "/" + Options.DB_PATH + "/" + db + ".db").exists()) {
        Helper.message("Η βάση " + db + " υπάρχει ήδη", "Δημιουργία βάσης", JOptionPane.ERROR_MESSAGE);
        createDb();
      }
      Class.forName("org.sqlite.JDBC");
      conn = DriverManager.getConnection("jdbc:sqlite:" + Options.USER_DIR + "/" + Options.DB_PATH + "/" + db + ".db");
      stmt = conn.createStatement();
      String afm = "CREATE TABLE `" + Afm.TABLE + "` "
          + "("
          + " `" + Afm.COLUMN_AFM_ID + "` INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,"
          + " `" + Afm.COLUMN_AFM + "` VARCHAR DEFAULT 0, "
          + " `" + Afm.COLUMN_NAME + "` VARCHAR NOT NULL "
          + ")";
      stmt.executeUpdate(afm);
      String types = "CREATE  TABLE `" + Type.TABLE + "` "
          + "("
          + " `" + Type.COLUMN_TYPE_ID + "` INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "
          + " `" + Type.COLUMN_DESCRIPTION + "` VARCHAR NOT NULL , "
          + " `" + Type.COLUMN_VALID + "` BOOL DEFAULT 1, "
          + " `" + Type.COLUMN_MULTIPLIER + "` DOUBLE DEFAULT 1.0"
          + ")";
      stmt.executeUpdate(types);
      String receipts = "CREATE TABLE `" + Receipt.TABLE + "`"
          + "("
          + " `" + Receipt.COLUMN_RECEIPT_ID + "` INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,"
          + " `" + Receipt.COLUMN_AFM + "` VARCHAR DEFAULT 0, `amount` DOUBLE NOT NULL , "
          + " `" + Receipt.COLUMN_BUY_DATE + "` DATETIME NOT NULL , "
          + " `" + Receipt.COLUMN_TYPE_ID + "` INTEGER NOT NULL , "
          + " `" + Receipt.COLUMN_COMMENTS + "` TEXT, "
          + " `" + Receipt.COLUMN_VALID + "` INTEGER NOT NULL DEFAULT 1"
          + ")";
      stmt.executeUpdate(receipts);
      Options.setOption(Options.DATABASE, db);
      return true;
    } catch (SQLException ex) {
      Main.log(Level.SEVERE, ErrorMessages.CREATE_DB_ERROR, ex);
      return false;
    }
  }

  private static boolean databaseExists(String db) {
    File dbFile = new File(Options.USER_DIR + "/" + Options.DB_PATH + "/" + db + ".db");
    if (dbFile.exists()) {
      return true;
    } else {
      return false;
    }
  }

  public static String[] getDatabases() {
    return (new File(Options.USER_DIR + "/" + Options.DB_PATH)).list(new FilenameFilter() {

      public boolean accept(File dir, String name) {
        if (name.endsWith(".db")) {
          return true;
        }
        return false;
      }
    });
  }

  public static String[] getBackUpDatabases() {
    return (new File(Options.USER_DIR + "/" + Options.DB_PATH)).list(new FilenameFilter() {

      public boolean accept(File dir, String name) {
        if (name.endsWith(".bak")) {
          return true;
        }
        return false;
      }
    });
  }

  public static void connectToDb(String newDb) {
    try {
      if (!databaseExists(newDb)) {
        return;
      }
      db = newDb;
      Options.setOption(Options.DATABASE, db);
      Class.forName("org.sqlite.JDBC");
      conn = DriverManager.getConnection("jdbc:sqlite:" + Options.USER_DIR + "/" + Options.DB_PATH + "/" + db + ".db");
      stmt = conn.createStatement();
    } catch (SQLException ex) {
      Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ClassNotFoundException ex) {
      Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private static boolean loadDefaultDb() {
    if (Options.toString(Options.DEFAULT_DATABASE).equals(Options.ASK_FOR_DB)) {
      return false;
    } else if (Options.toString(Options.DEFAULT_DATABASE).equals("")) {
      return false;
    }
    return true;
  }

  private Database() {
  }
}
