/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package panels.charts;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Vector;
import models.Statistics;
import models.Statistics.KindStats;
import models.Statistics.MonthlyStats;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import tools.Helper;

/**
 *
 * @author ssoldatos
 */
public class PieChart extends Chart {

  private DefaultPieDataset[] pieDatasets;

  public PieChart(Vector<Object> collection, int page, int type, boolean sort) {
    this.collection = collection;
    this.type = type;
    this.sort = sort;
    this.page = page;
    pieDatasets = createPieDataset();
    createPieChart(pieDatasets[page]);


  }

  private DefaultPieDataset[] createPieDataset() {

    KindStats kcol;
    MonthlyStats mcol;
    int totals = collection.size();
    totalPages = (int) Math.ceil((double) totals / 3);
    if(totalPages==0) {
      totalPages =1;
    }
    pieDatasets = new DefaultPieDataset[totalPages];

    for (int j = 0; j < pieDatasets.length; j++) {
      pieDatasets[j] = new DefaultPieDataset();
    }

    Object[] sorted = collection.toArray();
    if (sort) {
      Arrays.sort(sorted);
    }
    for (int i = 0; i < sorted.length; i++) {
      Object col = sorted[i];
      String desc = "";
      Double val = 0.0;
      if (type == Statistics.KIND) {
        kcol = (KindStats) col;
        desc = kcol.description;
        val = kcol.amount;

      } else if (type == Statistics.MONTHLY) {
        mcol = (MonthlyStats) col;
        desc = Helper.getGreekMonth(mcol.month);
        val = mcol.amount;
      }
      int k = i / Graph.MAX_RESULTS;
      pieDatasets[k].setValue(desc, val);
    }

    return pieDatasets;
  }

  private void createPieChart(DefaultPieDataset dataset) {
//    piechartPanel.removeAll();
//    piechartPanel.validate();
//    piechartPanel.repaint();
//    if (cpanel != null) {
//      cpanel = null;
//    }
    chart = ChartFactory.createPieChart3D(
        "Στατιστικά ανά " + (type == Statistics.KIND ? "Είδος" : "Μήνα"), // chart title
        dataset, // data
        true, // include legend
        true,
        false);

    customizeChart(chart);
//    cpanel = new ChartPanel(chart);
//    cpanel.setSize(piechartPanel.getSize());
//    piechartPanel.add(cpanel);
//    piechartPanel.getParent().validate();

  }

  private void customizeChart(JFreeChart chart) {
    final PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setCircular(true);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
            "{0} : {1} € ({2})", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()
        ));
        plot.setToolTipGenerator(
            new StandardPieToolTipGenerator(
            "{0} : {1} € ({2})", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance())
            );
        plot.setNoDataMessage("No data available");
  }

}
