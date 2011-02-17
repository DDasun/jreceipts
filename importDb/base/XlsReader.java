/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package importDb.base;

import exceptions.NoSheetSelectedException;
import exceptions.SheetNotFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tools.Helper;

/**
 *
 * @author ssoldatos
 */
public class XlsReader extends Reader {

  private FileInputStream inp;
  private HSSFFormulaEvaluator formulaEval;
  private XSSFFormulaEvaluator formulaEvalX;
  private boolean xls = false;

  /**
   * Force numeric fields to show as decimal
   */
  public XlsReader(File inputFile, File outputFile) throws IOException {
    super(inputFile, outputFile);
  }

  public boolean readExcel() throws SheetNotFoundException, NoSheetSelectedException {
    try {

      return _readExcel();

    } catch (IOException ex) {
      return false;
    }
  }

  @SuppressWarnings("deprecation")
  private boolean _readExcel() throws IOException, SheetNotFoundException, NoSheetSelectedException {
    String outLine = "";
    String sampleLines = "";
    HSSFWorkbook hwb = null;
    XSSFWorkbook xwb = null;
    int activeSheet = -1;
    try {
      try {
        inp = new FileInputStream(inputFile);
        hwb = new HSSFWorkbook(inp);
        xls = true;
        activeSheet = getActiveSheet(hwb);
        formulaEval = new HSSFFormulaEvaluator(hwb.getSheetAt(activeSheet), hwb);
        
      } catch (OfficeXmlFileException ex) {
        inp.close();
        inp = new FileInputStream(inputFile);
        xwb = new XSSFWorkbook(inp);
        xls = false;
        activeSheet = getActiveSheet(xwb);
        formulaEvalX = new XSSFFormulaEvaluator(xwb);
        
      }
      if (activeSheet == -1) {
        throw new NoSheetSelectedException("Δεν επιλέξατε κάποιο φύλλο");
      }
      int numRows = xls ? hwb.getSheetAt(activeSheet).getLastRowNum() : xwb.getSheetAt(activeSheet).getLastRowNum();
      if (numRows > 0) {
        Object sheet;
        Iterator rows;
        int numOfCells;
        if (xls) {
          sheet = hwb.getSheetAt(activeSheet);
          rows = ((HSSFSheet) sheet).rowIterator();
          numOfCells = ((HSSFSheet) sheet).getRow(0).getLastCellNum();
        } else {
          sheet = xwb.getSheetAt(activeSheet);
          rows = ((XSSFSheet) sheet).rowIterator();
          numOfCells = ((XSSFSheet) sheet).getRow(0).getLastCellNum();
        }
        int lines = 0, cellNo = 0;
        String cellValue = "";
        while (rows.hasNext()) {
          lines++;
          cellNo = 0;
          Object row;
          if (xls) {
            row = (HSSFRow) rows.next();
          } else {
            row = (XSSFRow) rows.next();
          }
          for (int i = 0; i < numOfCells; i++) {
            cellNo++;
            Object cell;
            if (xls) {
              HSSFRow r = (HSSFRow) row;
              cell = (HSSFCell) r.getCell((short) i);
            } else {
              XSSFRow r = (XSSFRow) row;
              cell = (XSSFCell) r.getCell((short) i);
            }
            cellValue = processCell(cell, row);
            if (cellNo < numOfCells) {
              outLine += cellValue + "\t";
            } else {
              outLine += cellValue;
            }
          }
          if (!outLine.trim().equals("")) {
            out.println(outLine);
          }
          outLine = "";
        }
        out.close();
      }
      return true;
    } catch (IOException ex) {
      return false;
    }
  }

  @SuppressWarnings("deprecation")
  private String processCell(Object cell, Object curRow) {
    String v = "";
    if (cell == null) {
      return "";
    }
    Object newCell;
    CellValue value;
    Date date;
    if (xls) {
      cell = (HSSFCell) cell;
    } else {
      cell = (XSSFCell) cell;
    }

    if (getCellType(cell) == HSSFCell.CELL_TYPE_NUMERIC) {
      if (isCellDateFormatted(cell)) {
        double ddate = Double.parseDouble(String.valueOf(getNumericCellValue(cell)));
        date = HSSFDateUtil.getJavaDate(ddate);
        formatter = new SimpleDateFormat(dateFormat);
        v = formatter.format(date);
      } else if (getNumericCellValue(cell) == (long) getNumericCellValue(cell)
          && !Helper.isInArray(String.valueOf(getColumnIndex(cell) + 1), decimalFields != null ? decimalFields : new String[]{})) {
        v = String.valueOf((long) getNumericCellValue(cell));
      } else {
        DecimalFormat myFormatter = new DecimalFormat(decimalFormat);
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator(groupSeperator);
        dfs.setDecimalSeparator(decimalSeperator);
        myFormatter.setDecimalFormatSymbols(dfs);
        v = myFormatter.format(getNumericCellValue(cell));

        //v = String.valueOf(cell.getNumericCellValue());
      }
    } else if (getCellType(cell) == HSSFCell.CELL_TYPE_STRING) {
      v = String.valueOf(getRichStringCellValue(cell));
      v = v.replaceAll("\r", " ");
      v = v.replaceAll("\n", " ");
      v = v.trim();
    } else if (getCellType(cell) == HSSFCell.CELL_TYPE_BOOLEAN) {
      v = String.valueOf(getBooleanCellValue(cell));
    } else if (getCellType(cell) == HSSFCell.CELL_TYPE_BLANK) {
      v = "";
    } else if (getCellType(cell) == HSSFCell.CELL_TYPE_ERROR) {
    } else if (getCellType(cell) == HSSFCell.CELL_TYPE_FORMULA) {
      if (xls) {
        formulaEval.setCurrentRow((HSSFRow) curRow);
        newCell = formulaEval.evaluateInCell((HSSFCell) cell);
      } else {
        newCell = formulaEvalX.evaluateInCell((XSSFCell) cell);
      }
      v = processCell(newCell, curRow);
    }
    return v.replaceAll("\n", " ");
  }

  private int getActiveSheet(Object wb) throws SheetNotFoundException {
    HSSFWorkbook hwb = null;
    XSSFWorkbook xwb = null;
    int sheets = 0;
    if (xls) {
      hwb = (HSSFWorkbook) wb;
      sheets = hwb.getNumberOfSheets();
    } else {
      xwb = (XSSFWorkbook) wb;
      sheets = xwb.getNumberOfSheets();
    }

    if (sheets == 1) {
      return 0;
    } else if (sheets == 0) {
      throw new SheetNotFoundException("Δεν υπάρχει κάποιο φύλλο στο excel αρχείο");
    }
    String[] names = new String[sheets];
    for (int i = 0; i < sheets; i++) {
      names[i] = xls ? hwb.getSheetAt(i).getSheetName() : xwb.getSheetAt(i).getSheetName();
    }
    String name = Helper.ask("Δημιουργία βάσης από αρχείο", "Επιλέξτε το φύλλο που θέλετε να εισάγετε στη βάση", names);
    if (name != null) {
      return xls ? hwb.getSheetIndex(name) : xwb.getSheetIndex(name);
    } else {
      return -1;
    }
  }

  private int getCellType(Object cell) {
    if (xls) {
      HSSFCell hcell = (HSSFCell) cell;
      return hcell.getCellType();
    } else {
      XSSFCell xcell = (XSSFCell) cell;
      return xcell.getCellType();
    }
  }

  private boolean isCellDateFormatted(Object cell) {
    if (xls) {
      return HSSFDateUtil.isCellDateFormatted((HSSFCell) cell);
    } else {
      return HSSFDateUtil.isCellDateFormatted((XSSFCell) cell);
    }
  }

  private double getNumericCellValue(Object cell) {
    if (xls) {
      return ((HSSFCell) cell).getNumericCellValue();
    } else {
      return ((XSSFCell) cell).getNumericCellValue();
    }
  }

  private int getColumnIndex(Object cell) {
    if (xls) {
      return ((HSSFCell) cell).getColumnIndex();
    } else {
      return ((XSSFCell) cell).getColumnIndex();
    }
  }

  private String getRichStringCellValue(Object cell) {
    if (xls) {
      return String.valueOf(((HSSFCell) cell).getRichStringCellValue());
    } else {
      return String.valueOf(((XSSFCell) cell).getRichStringCellValue());
    }
  }

  private boolean getBooleanCellValue(Object cell) {
    if (xls) {
      return ((HSSFCell) cell).getBooleanCellValue();
    } else {
      return ((XSSFCell) cell).getBooleanCellValue();
    }
  }
}
