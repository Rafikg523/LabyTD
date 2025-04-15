package Chart;

import org.jfree.chart.*;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.ColumnArrangement;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Chart {

    public static void saveChart(XYSeries series, String Xname, String Yname, String filename) {
        saveChart(series, Xname, Yname, filename,series.getMinX(), series.getMaxX(), false);
    }
    public static void saveChart(XYSeries series, String Xname, String Yname, String filename, boolean norm) {
        saveChart(series, Xname, Yname, filename,series.getMinX(), series.getMaxX(), norm);
    }

    public static void saveChart(XYSeries series, String Xname, String Yname, String filename, double a, double b) {
        saveChart(series, Xname, Yname, filename, a, b, true);
    }

    public static void saveChart(XYSeries series, String Xname, String Yname, String filename, double a, double b, boolean norm) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        if (norm) {
            XYSeries Nseries = new XYSeries(series.getKey());
            for (int i = 0; i < series.getItemCount(); i++) {
                double y = (series.getY(i).doubleValue() - series.getMinY())/(series.getMaxY() - series.getMinY());
                Nseries.add(series.getX(i), y);
            }
            series = Nseries;
        }

        dataset.addSeries(series);

        NumberAxis xAxis = new NumberAxis(Xname);
        xAxis.setAutoRange(false);
        a *= 1.1;
        b *= 1.1;
        xAxis.setRange(a, b);
        NumberAxis yAxis = new NumberAxis(Yname);

        XYSplineRenderer renderer = new XYSplineRenderer();
        renderer.setSeriesShapesVisible(0, false);

        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        JFreeChart chart = new JFreeChart(series.getDescription(), JFreeChart.DEFAULT_TITLE_FONT, plot, false);

        try {
            File file = new File(filename);
            ChartUtilities.saveChartAsPNG(file, chart, 800, 600);
            System.out.println("Zapisano wykres: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveChart(XYSeries series1, XYSeries series2, String Xname, String Yname, String filename) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);

        NumberAxis xAxis = new NumberAxis(Xname);
        NumberAxis yAxis = new NumberAxis(Yname);

        XYSplineRenderer renderer = new XYSplineRenderer();
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesPaint(1, Color.BLUE);

        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        JFreeChart chart = new JFreeChart(series1.getDescription(), JFreeChart.DEFAULT_TITLE_FONT, plot, true);

        try {
            File file = new File(filename);
            ChartUtilities.saveChartAsPNG(file, chart, 800, 600);
            System.out.println("Zapisano wykres: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveChart(XYSeries series, String title, String Xname, String Yname, String filename, int[] B, double[] Bwidth, double[] min, double[] max) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        NumberAxis xAxis = new NumberAxis(Xname);
        xAxis.setAutoRange(true);
        NumberAxis yAxis = new NumberAxis(Yname);

        XYSeriesCollection extraLines = new XYSeriesCollection();

        double yTop = series.getMaxY();
        double end = series.getMaxX();
        XYSeries lineTop = new XYSeries("lineTop");
        lineTop.add(0, yTop);
        lineTop.add(end, yTop);
        extraLines.addSeries(lineTop);

        if (B != null && min != null && max != null && B.length == min.length && B.length == max.length && Bwidth != null && B.length == Bwidth.length) {
            for (int i = 0; i < B.length; i++) {
                double yBottom = yTop - B[i];

                XYSeries lineBottom = new XYSeries("lineBottom_" + i);
                lineBottom.add(0, yBottom);
                lineBottom.add(end, yBottom);

                XYSeries lineMin = new XYSeries("lineMin_" + i);
                lineMin.add(min[i], 0);
                lineMin.add(min[i], yTop);

                XYSeries lineMax = new XYSeries("lineMax_" + i);
                lineMax.add(max[i], 0);
                lineMax.add(max[i], yTop);

                extraLines.addSeries(lineBottom);
                extraLines.addSeries(lineMin);
                extraLines.addSeries(lineMax);
            }
        }

        XYSplineRenderer renderer = new XYSplineRenderer();
        renderer.setSeriesShapesVisible(0, false);

        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);

        if (extraLines.getSeriesCount() > 0) {
            XYLineAndShapeRenderer extraRenderer = new XYLineAndShapeRenderer(true, false);
            Stroke dashed = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0);
            Stroke dotted = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0);

            Color[] colors = new Color[]{Color.MAGENTA, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.PINK, Color.YELLOW};

            extraRenderer.setSeriesPaint(0, Color.BLACK);
            extraRenderer.setSeriesStroke(0, dashed);

            for (int i = 0; i < B.length; i++) {
                Color color = colors[i % colors.length];
                int baseIndex = i * 3 + 1;
                extraRenderer.setSeriesPaint(baseIndex, color);
                extraRenderer.setSeriesStroke(baseIndex, dashed);
                extraRenderer.setSeriesPaint(baseIndex + 1, color);
                extraRenderer.setSeriesStroke(baseIndex + 1, dotted);
                extraRenderer.setSeriesPaint(baseIndex + 2, color);
                extraRenderer.setSeriesStroke(baseIndex + 2, dotted);
            }

            plot.setDataset(1, extraLines);
            plot.setRenderer(1, extraRenderer);
            plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);

            LegendItemCollection legendItems = new LegendItemCollection();
            for (int i = 0; i < B.length; i++) {
                Color color = colors[i % colors.length];
                String label = "B" + B[i] + " = " + Bwidth[i];
                LegendItem item = new LegendItem(label, null, null, null, new Rectangle(10, 10), color);
                legendItems.add(item);
            }
            plot.setFixedLegendItems(legendItems);
        }

        JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, false);
        LegendTitle legend = new LegendTitle(plot);
        legend.setPosition(RectangleEdge.RIGHT);
        legend.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(VerticalAlignment.TOP);
        legend.setBackgroundPaint(new Color(255, 255, 255, 200));
        legend.setFrame(new BlockBorder(Color.BLACK));
        legend.setItemFont(new Font("SansSerif", Font.PLAIN, 12));
        legend.setMargin(5, 5, 5, 5);
        legend.setPadding(5, 5, 5, 5);
        legend.setLegendItemGraphicPadding(new RectangleInsets(2, 2, 2, 2));
        legend.setItemLabelPadding(new RectangleInsets(2, 2, 2, 2));

        chart.addSubtitle(legend);

        try {
            File file = new File(filename);
            ChartUtilities.saveChartAsPNG(file, chart, 800, 600);
            System.out.println("Zapisano wykres: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
