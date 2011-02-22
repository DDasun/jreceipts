/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package panels.charts;

import java.awt.Color;
import java.awt.Paint;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Vector;
import models.Statistics;
import models.Statistics.KindStats;
import models.Statistics.MonthlyStats;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;
import tools.Helper;

/**
 *
 * @author ssoldatos
 */
public class BarsChart extends Chart{
    protected DefaultCategoryDataset[] barsDatasets;

  BarsChart(Vector<Object> collection, int page, int type, boolean sort) {
    this.collection = collection;
    this.page = page;
    this.type = type;
    this.sort = sort;
    barsDatasets = createBarsDataset();
    createBarsChart(barsDatasets[page]);

  }

  private DefaultCategoryDataset[] createBarsDataset() {
    KindStats kcol;
    MonthlyStats mcol;
    int totals = collection.size();
    totalPages = (int) Math.ceil((double) totals / 3);
    if(totalPages==0) {
      totalPages =1;
    }
    barsDatasets = new DefaultCategoryDataset[totalPages];
    for (int j = 0; j < barsDatasets.length; j++) {
      barsDatasets[j] = new DefaultCategoryDataset();
    }

    Object[] sorted = collection.toArray();
    if (sort) {
      Arrays.sort(sorted);
    }
    for (int i = 0; i < sorted.length; i++) {
      Object col = sorted[i];
      String desc = "";
      Double val = 0.0;
      int tot = 0;
      if (type == Statistics.KIND) {
        kcol = (KindStats) col;
        desc = kcol.description;
        val = kcol.amount;
        tot = kcol.totals;

      } else if (type == Statistics.MONTHLY) {
        mcol = (MonthlyStats) col;
        desc = Helper.getGreekMonth(mcol.month);
        val = mcol.amount;
        tot = mcol.totals;
      }
      int k = i / Graph.MAX_RESULTS;
      barsDatasets[k].setValue(val, "", desc);
    }
    return barsDatasets;
  }

  private void createBarsChart(DefaultCategoryDataset dataset) {
    chart = ChartFactory.createBarChart3D(
        "Στατιστικά ανά " + (type == Statistics.KIND ? "Είδος" : "Μήνα"), // chart title
        type == Statistics.KIND ? "Είδος" : "Μήνας",
        "Ποσό",
        dataset, // data
        PlotOrientation.HORIZONTAL,
        false, // include legend
        true,
        false);

    customizeChart(chart);


  }

  private void customizeChart(JFreeChart chart) {
    ValueAxis rangeAxis = null;
    // get a reference to the plot for further customisation...
    CategoryPlot plot = (CategoryPlot) chart.getPlot();
    plot.setNoDataMessage("NO DATA!");
    final CategoryItemRenderer renderer = (CategoryItemRenderer) new CustomBarRenderer(
        new Paint[]{Color.red, Color.blue, Color.green,
          Color.yellow, Color.orange, Color.cyan,
          Color.magenta, Color.blue});
//        renderer.setLabelGenerator(new StandardCategoryLabelGenerator());
    renderer.setBaseItemLabelsVisible(true);
    final ItemLabelPosition p = new ItemLabelPosition(
        ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 90.0);
    renderer.setBasePositiveItemLabelPosition(p);
    plot.setRenderer(renderer);
    plot.getRenderer().setBaseToolTipGenerator(
        new StandardCategoryToolTipGenerator("{1} : {2} €",  NumberFormat.getNumberInstance()));
    // change the margin at the top of the range axis...
    rangeAxis = plot.getRangeAxis();
    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    rangeAxis.setLowerMargin(0.15);
    rangeAxis.setUpperMargin(0.15);
  }
}
