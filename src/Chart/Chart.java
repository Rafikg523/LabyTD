package Chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


import java.awt.*;
import java.io.File;
import java.io.IOException;


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


}
