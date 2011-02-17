/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package panels.charts;

import java.awt.Paint;
import org.jfree.chart.renderer.category.BarRenderer;

/**
 *
 * @author ssoldatos
 */
public class CustomBarRenderer extends BarRenderer {
  private static final long serialVersionUID = 23453464356464L;
    /** The colors. */
    private Paint[] colors;

    /**
     * Creates a new renderer.
     *
     * @param colors  the colors.
     */
    public CustomBarRenderer(final Paint[] colors) {
      this.colors = colors;
    }

    /**
     * Returns the paint for an item.  Overrides the default behaviour inherited from
     * AbstractSeriesRenderer.
     *
     * @param row  the series.
     * @param column  the category.
     *
     * @return The item color.
     */
    public Paint getItemPaint(final int row, final int column) {
      return this.colors[column % this.colors.length];
    }
   }
