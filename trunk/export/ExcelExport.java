/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package export;

import exceptions.ErrorMessages;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import models.Receipt;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import receipts.Main;
import tools.Helper;
import tools.options.Options;

/**
 *
 * @author ssoldatos
 */
public class ExcelExport extends AbstractExport {

  HSSFWorkbook wb = new HSSFWorkbook();
  private HSSFSheet sheet = wb.createSheet("ΑΠΟΔΕΙΞΕΙΣ");
  private HSSFCellStyle headerStyle;
  private HSSFFont headerFont;
  private HSSFCellStyle cellStyle;
  private HSSFCellStyle dateCellStyle;
  private HSSFCellStyle amountCellStyle;

  public ExcelExport() {
    this.type = ExportConstants.EXCEL;
    createHeaderStyle();
    createCellStyle();
    createHeaderFont();
  }

  @Override
  public void export() {
    FileOutputStream out = null;
    int rows = 0;
    try {
      records = getRecords();
      //HEADER
      HSSFRow headerRow = sheet.createRow(rows);

      for (int i = 0; i < HEADERS.length; i++) {
        HSSFCell headerCell = headerRow.createCell( i);
        headerCell.setCellValue(new HSSFRichTextString(HEADERS[i]));
        headerCell.setCellStyle(headerStyle);
      }
      // HEADER

      //BODY
      for (Iterator<Object> it = records.iterator(); it.hasNext();) {
        Receipt receipt = (Receipt) it.next();
        rows++;
        HSSFRow row = sheet.createRow(rows);
        //ID
        HSSFCell cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cell.setCellValue(++receiptId);

        //DATE
        cell = row.createCell(1);
        cell.setCellStyle(dateCellStyle);
        cell.setCellValue(receipt.getDate());

        //TYPE
        cell = row.createCell(2);
        cell.setCellStyle(cellStyle);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(new HSSFRichTextString(receipt.getType()));

        //AFM
        cell = row.createCell(3);
        cell.setCellStyle(cellStyle);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(new HSSFRichTextString(receipt.getAfm()));

        //AMOUNT
        cell = row.createCell(4);
        cell.setCellStyle(amountCellStyle);
        cell.setCellValue(receipt.getAmount());

        //MULTIPLIER
        cell = row.createCell(5);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(receipt.getMultiplier());

        //AMOUNT
        cell = row.createCell(6);
        cell.setCellStyle(amountCellStyle);
        cell.setCellValue(receipt.getAmount()*receipt.getMultiplier());

        totalAmount += receipt.getAmount()*receipt.getMultiplier();
      }
      //BODY
      //TRAILER
      rows++;
      HSSFRow trailerRow = sheet.createRow(rows);
      HSSFCell trailerCell = trailerRow.createCell(6);
      trailerCell.setCellStyle(amountCellStyle);
      trailerCell.setCellValue(totalAmount);

      sheet.autoSizeColumn(0);
      sheet.autoSizeColumn(1);
      sheet.autoSizeColumn(2);
      sheet.autoSizeColumn(3);
      sheet.autoSizeColumn(4);
      sheet.autoSizeColumn(5);
      sheet.autoSizeColumn(6);
      
      out = new FileOutputStream(getExportFile());
      wb.write(out);
      out.close();
      Helper.message("The excel file was exported to " + getExportFile(), "File exported", JOptionPane.INFORMATION_MESSAGE);
      Helper.openFile(getExportFile());
    } catch (IOException ex) {
      Main.logger.log(Level.SEVERE, null, ex);
    } finally {
      try {
        out.close();
      } catch (IOException ex) {
        Main.logger.log(Level.SEVERE, ErrorMessages.IO, ex);
      }
    }
  }

  private void createHeaderStyle() {
    headerStyle = wb.createCellStyle();
    headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
    headerStyle.setBorderTop(HSSFCellStyle.BORDER_THICK);
    headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THICK);
    headerStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
    headerStyle.setFillBackgroundColor(HSSFColor.GREY_50_PERCENT.index);
  }

  private void createHeaderFont() {
    headerFont = wb.createFont();
    headerFont.setFontName(HSSFFont.FONT_ARIAL);
    headerFont.setFontHeightInPoints((short) 10);
    headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    headerFont.setColor(HSSFColor.BLACK.index);
    headerStyle.setFont(headerFont);
  }

  private void createCellStyle() {
    HSSFDataFormat format = wb.createDataFormat();

    //SIMPLE CELL
    cellStyle = wb.createCellStyle();
    cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
    cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    cellStyle.setFillBackgroundColor(HSSFColor.WHITE.index);

    //DATE CELL
    dateCellStyle = wb.createCellStyle();
    dateCellStyle.cloneStyleFrom(cellStyle);
    dateCellStyle.setDataFormat(format.getFormat(Options.DATE_FORMAT));

    //AMOUNT CELL
    amountCellStyle = wb.createCellStyle();
    amountCellStyle.cloneStyleFrom(cellStyle);
    amountCellStyle.setDataFormat(format.getFormat(Options._DECIMAL_FORMAT.replaceAll("'", "")));
  }
}
