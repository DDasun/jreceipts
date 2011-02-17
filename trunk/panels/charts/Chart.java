/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package panels.charts;

import java.util.Vector;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author ssoldatos
 */
public abstract class Chart {
   protected Vector<Object> collection;
  protected int page;
  protected int type;
  protected boolean sort;
  protected int totalPages;
  public JFreeChart chart;

}
