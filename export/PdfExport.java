/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package export;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import models.Receipt;
import receipts.Main;
import sun.font.FontManager;
import tools.Helper;
import tools.Options;

/**
 *
 * @author ssoldatos
 */
public class PdfExport extends AbstractExport {

  private BaseFont bf;
  private Font titleFont;
  private Font headersFont;
  private Font plainFont;
  private int[] widths = {10, 15, 25, 15, 15, 10, 15};

  public PdfExport() {
    try {
      this.type = ExportConstants.PDF;
      records = getRecords();
      GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
      java.awt.Font[] fonts = e.getAllFonts(); // Get the fonts
      String fontFilePath = FontManager.getFontPath(true) + "/" + FontManager.getFileNameForFontName(DEFAULT_FONT);
      bf = BaseFont.createFont(fontFilePath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
      titleFont = new Font(bf, TITLE_FONT_SIZE, Font.BOLD);
      headersFont = new Font(bf, HEADERS_FONT_SIZE, Font.BOLD);
      plainFont = new Font(bf, PLAIN_FONT_SIZE);
    } catch (DocumentException ex) {
      Main.log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Main.log(Level.SEVERE, null, ex);
    }

  }

  @Override
  protected void export() {
    try {
      Document document = new Document();
      PdfWriter.getInstance(document, new FileOutputStream(getExportFile()));
      addMetaData(document);
      document.open();
      addContent(document);
      document.close();

      Helper.message("The pdf file was exported to " + getExportFile(), "File exported", JOptionPane.INFORMATION_MESSAGE);
      Helper.openFile(getExportFile());
    } catch (DocumentException ex) {
      Main.logger.log(Level.SEVERE, null, ex);
    } catch (FileNotFoundException ex) {
      Main.logger.log(Level.SEVERE, null, ex);
    }

  }

  private static void addMetaData(Document document) {
    document.addTitle("Αποδείξεις έτους " +Options.YEAR);
    document.addSubject("Λίστα αποδείξεων");
    document.addAuthor("Spyros Soldatos");
    document.addCreator("Receipts v" + Main.version);
  }

  private void addContent(Document document) {
    try {
      Paragraph para = new Paragraph();
      Phrase title = new Phrase("Αποδείξεις", titleFont);
      para.setAlignment(Paragraph.ALIGN_CENTER);
      para.add(title);
      Table t = createTable();
      para.add(t);
      document.add(para);
      Phrase total = new Phrase("Σύνολο : " + Helper.convertAmountForViewing(totalAmount), headersFont);
      Paragraph totalPar = new Paragraph(total);
      totalPar.setAlignment(Paragraph.ALIGN_RIGHT);
      document.add(totalPar);
    } catch (DocumentException ex) {
      Main.logger.log(Level.SEVERE, null, ex);
    }
  }

  private Table createTable() throws BadElementException, DocumentException {
    Table t = new Table(HEADERS.length);
    t.setBorderColor(Color.BLACK);
    t.setPadding(CELL_PADDING);
    t.setSpacing(CELL_SPACING);
    t.setBorderWidth(BORDER_WIDTH);
    t.setWidths(widths);
    t.setWidth(100);
    createHeader(t);
    createBody(t);

    return t;
  }

  private void createHeader(Table t) throws BadElementException {
    for (int i = 0; i < HEADERS.length; i++) {
      Phrase ph = new Phrase(HEADERS[i], headersFont);
      Cell c = new Cell(ph);
      c.setHeader(true);
      c.setHorizontalAlignment(Cell.ALIGN_CENTER);
      c.setBackgroundColor(Color.LIGHT_GRAY);
      t.addCell(c);
    }
    t.endHeaders();
  }

  private void createBody(Table t) throws BadElementException {
    for (Iterator<Object> it = records.iterator(); it.hasNext();) {
      Receipt receipt = (Receipt) it.next();
      //AA
      Phrase ph = new Phrase(String.valueOf(++receiptId), plainFont);
      Cell c = new Cell(ph);
      c.setHorizontalAlignment(Cell.ALIGN_RIGHT);
      t.addCell(c);
      //DATE
      ph = new Phrase(Helper.convertDateForView(receipt.getDate()), plainFont);
      c = new Cell(ph);
      c.setHorizontalAlignment(Cell.ALIGN_CENTER);
      t.addCell(c);
      //TYPE
      ph = new Phrase(receipt.getType(), plainFont);
      c = new Cell(ph);
      c.setHorizontalAlignment(Cell.ALIGN_LEFT);
      t.addCell(c);
      //AFM
      ph = new Phrase(receipt.getAfm(), plainFont);
      c = new Cell(ph);
      c.setHorizontalAlignment(Cell.ALIGN_LEFT);
      t.addCell(c);
      //AMOUNT
      ph = new Phrase(Helper.convertAmountForViewing(receipt.getAmount()), plainFont);
      c = new Cell(ph);
      c.setHorizontalAlignment(Cell.ALIGN_RIGHT);
      t.addCell(c);
      //MULT
      ph = new Phrase(String.valueOf(receipt.getMultiplier()), plainFont);
      c = new Cell(ph);
      c.setHorizontalAlignment(Cell.ALIGN_RIGHT);
      t.addCell(c);
       //AMOUNT
      ph = new Phrase(Helper.convertAmountForViewing(receipt.getAmount()*receipt.getMultiplier()), plainFont);
      c = new Cell(ph);
      c.setHorizontalAlignment(Cell.ALIGN_RIGHT);
      t.addCell(c);

      totalAmount += receipt.getAmount()*receipt.getMultiplier();
    }
  }
}
