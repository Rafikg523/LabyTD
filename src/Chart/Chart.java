package Chart;

import org.jfree.chart.*;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
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

    public static void saveChart(
            XYSeries series,
            String Xname,
            String Yname,
            String filename,
            double a,
            double b,
            boolean norm) {

        int precision = Math.max(1, Math.min(50, 800 / series.getItemCount()));

        if (norm) {
            XYSeries nSeries = new XYSeries(series.getKey());
            double minY = series.getMinY();
            double maxY = series.getMaxY();
            for (int i = 0; i < series.getItemCount(); i++) {
                double y = (series.getY(i).doubleValue() - minY) / (maxY - minY);
                nSeries.add(series.getX(i), y);
            }
            series = nSeries;
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        NumberAxis xAxis = new NumberAxis(Xname);
        xAxis.setAutoRange(false);
        xAxis.setRange(a * 1.1, b * 1.1);

        NumberAxis yAxis = new NumberAxis(Yname);

        XYSplineRenderer renderer = new XYSplineRenderer(precision);
        renderer.setSeriesShapesVisible(0, false);

        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        JFreeChart chart = new JFreeChart(
                series.getDescription(),
                JFreeChart.DEFAULT_TITLE_FONT,
                plot,
                false);

        try {
            File file = new File(filename);
            ChartUtilities.saveChartAsPNG(file, chart, 1600, 600);
            System.out.println("Zapisano wykres: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveChart3(
            XYSeries series,
            String Xname,
            String Yname,
            String filename) {

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        NumberAxis xAxis = new NumberAxis(Xname);
        xAxis.setAutoRangeIncludesZero(false);

        NumberAxis yAxis = new NumberAxis(Yname);
        yAxis.setAutoRangeIncludesZero(false);
        yAxis.setRange(-0.2, 1.2);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesStroke(0, new BasicStroke(4.0f));


        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        JFreeChart chart = new JFreeChart(
                series.getDescription(),
                JFreeChart.DEFAULT_TITLE_FONT,
                plot,
                false);

        try {
            File file = new File(filename);
            ChartUtilities.saveChartAsPNG(file, chart, 1600, 600);
            System.out.println("Zapisano wykres: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveChart4(
            XYSeries series1,
            XYSeries series2,
            String Xname,
            String Yname,
            String filename,
            String title) {

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series2);
        dataset.addSeries(series1);

        NumberAxis xAxis = new NumberAxis(Xname);
        xAxis.setAutoRangeIncludesZero(true);
        xAxis.setTickUnit(new NumberTickUnit(1.0));

        NumberAxis yAxis = new NumberAxis(Yname);
        yAxis.setAutoRangeIncludesZero(false);
        yAxis.setRange(-0.2, 1.2);
        yAxis.setTickUnit(new NumberTickUnit(1));

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesStroke(0, new BasicStroke(
                3.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL,
                0,
                new float[]{10.0f, 10.0f},
                0.0f
        ));

        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, false);
        renderer.setSeriesStroke(1, new BasicStroke(3.0f));

        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        JFreeChart chart = new JFreeChart(
                title,
                JFreeChart.DEFAULT_TITLE_FONT,
                plot,
                true);

        try {
            File file = new File(filename);
            ChartUtilities.saveChartAsPNG(file, chart, 1600, 600);
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

        double minmin = min[0];
        for (int i = 0; i < min.length; i++) {
            minmin = Math.min(minmin, min[i]);
        }

        double maxmax = max[0];
        for (int i = 0; i < max.length; i++) {
            maxmax = Math.max(maxmax, max[i]);
        }

        double center = (maxmax + minmin) / 2.0;
        double side1 = Math.abs(maxmax - center);
        double side2 = Math.abs(center - minmin);
        double side = Math.max(side1, side2);

        side *= 1.5;

        maxmax = center + side;
        minmin = center - side;

        maxmax = maxmax <= series.getMaxX() ? maxmax : series.getMaxX();
        minmin = minmin >= series.getMinX() ? minmin : series.getMinX();

        NumberAxis xAxis = new NumberAxis(Xname);

        xAxis.setAutoRange(false);
        xAxis.setRange(minmin , maxmax);

        NumberAxis yAxis = new NumberAxis(Yname);
        yAxis.setAutoRange(true);

        XYSeriesCollection extraLines = new XYSeriesCollection();

        double yTop = series.getMaxY();
        double end = series.getMaxX();
        XYSeries lineTop = new XYSeries("lineTop");
        lineTop.add(series.getMinX(), yTop);
        lineTop.add(end, yTop);
        extraLines.addSeries(lineTop);

        if (B != null && min != null && max != null && B.length == min.length && B.length == max.length && Bwidth != null && B.length == Bwidth.length) {
            double epsilon = (maxmax - minmin) / 500.0;

            Map<Double, Integer> minDup = new HashMap<>();
            Map<Double, Integer> maxDup = new HashMap<>();

            for (int i = 0; i < B.length; i++) {
                double xMin = min[i];
                double xMax = max[i];

                int minIdx = minDup.merge(xMin, 1, Integer::sum) - 1;
                int maxIdx = maxDup.merge(xMax, 1, Integer::sum) - 1;

                double xMinShifted = xMin + minIdx * epsilon;
                double xMaxShifted = xMax + maxIdx * epsilon;

                double yBottom = yTop - B[i];

                XYSeries lineBottom = new XYSeries("lineBottom_" + i);
                lineBottom.add(series.getMinX(), yBottom);
                lineBottom.add(end, yBottom);

                XYSeries lineMin = new XYSeries("lineMin_" + i);
                lineMin.add(xMinShifted, series.getMinY());
                lineMin.add(xMinShifted, yBottom);

                XYSeries lineMax = new XYSeries("lineMax_" + i);
                lineMax.add(xMaxShifted, series.getMinY());
                lineMax.add(xMaxShifted, yBottom);

                extraLines.addSeries(lineBottom);
                extraLines.addSeries(lineMin);
                extraLines.addSeries(lineMax);
            }

        }

        XYSplineRenderer renderer = new XYSplineRenderer(40);
        renderer.setSeriesShapesVisible(0, true);

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
                String label = "B" + B[i] + " = " + Bwidth[i] + " Hz";
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
            ChartUtilities.saveChartAsPNG(file, chart, 1600, 600);
            System.out.println("Zapisano wykres: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveChart2(XYSeries series, String title, String Xname, String Yname, String filename, int faId, int fbId, String[] dane) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        NumberAxis xAxis = new NumberAxis(Xname);
        xAxis.setAutoRange(true);
        NumberAxis yAxis = new NumberAxis(Yname);

        XYSeriesCollection extraLines = new XYSeriesCollection();

        double yTop = series.getMaxY();
        double end = series.getMaxX();

        double fa = series.getX(faId).doubleValue();
        double fb = series.getX(fbId).doubleValue();

        XYSeries lineTop = new XYSeries("lineTop");
        lineTop.add(fa, yTop);
        lineTop.add(fb, yTop);
        extraLines.addSeries(lineTop);

        XYSeries lineFa = new XYSeries("lineFa");
        lineFa.add(fa, 0);
        lineFa.add(fa, yTop);
        extraLines.addSeries(lineFa);

        XYSeries lineFb = new XYSeries("lineFb");
        lineFb.add(fb, 0);
        lineFb.add(fb, yTop);
        extraLines.addSeries(lineFb);

        XYSplineRenderer renderer = new XYSplineRenderer();
        renderer.setSeriesShapesVisible(0, false);

        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);

        XYLineAndShapeRenderer extraRenderer = new XYLineAndShapeRenderer(true, false);
        Stroke dashed = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0);

        extraRenderer.setSeriesPaint(0, Color.BLUE);
        extraRenderer.setSeriesStroke(0, dashed);

        extraRenderer.setSeriesPaint(1, Color.BLUE);
        extraRenderer.setSeriesStroke(1, dashed);

        extraRenderer.setSeriesPaint(2, Color.BLUE);
        extraRenderer.setSeriesStroke(2, dashed);

        plot.setDataset(1, extraLines);
        plot.setRenderer(1, extraRenderer);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);

        LegendItemCollection legendItems = new LegendItemCollection();
        Color[] colors = new Color[]{Color.BLUE, Color.RED, Color.BLUE};
        for (int i = 0; i < dane.length; i++) {
            LegendItem item = new LegendItem(dane[i], null, null, null, new Rectangle(10, 10), colors[0]);
            legendItems.add(item);
        }
        plot.setFixedLegendItems(legendItems);

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
            ChartUtilities.saveChartAsPNG(file, chart, 2000, 600);
            System.out.println("Zapisano wykres: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
