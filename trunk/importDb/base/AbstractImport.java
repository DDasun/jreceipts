/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package importDb.base;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author ssoldatos
 */
public abstract class AbstractImport {
  protected File file;
  protected File tmp;
  protected int fileType;
  protected ArrayList<String[]> data = new ArrayList<String[]>();
  protected int[] fields;
  protected String errorMessage;
  protected String database;
  protected boolean headers;
  protected int thousandDel;
  protected int decimalDel;
  protected int multiDecimalDel;


  public abstract boolean start();
  public abstract boolean selectFile();
  public abstract boolean createTmpFile();
  public abstract boolean readTmpFile();
  public abstract boolean assignFields();
  public abstract boolean createDb();
  public abstract boolean importFile();
  public abstract boolean deleteTmpFile();


}
