package utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.awt.*;
import java.time.LocalDate;
import java.util.Map;

public class ChartUtils {

    // Colores para los gráficos
    private static final Color[] CHART_COLORS = {
            new Color(41, 128, 185),   // Azul
            new Color(39, 174, 96),    // Verde
            new Color(231, 76, 60),    // Rojo
            new Color(243, 156, 18),   // Naranja
            new Color(142, 68, 173),   // Púrpura
            new Color(22, 160, 133),   // Turquesa
            new Color(52, 73, 94),     // Gris oscuro
            new Color(230, 126, 34)    // Naranja oscuro
    };

    // Fuentes para los gráficos
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 12);

    public static ChartPanel createBarChart(String title, String xLabel, String yLabel,
                                            Map<String, Number> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Number> entry : data.entrySet()) {
            dataset.addValue(entry.getValue(), "Valor", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                title,
                xLabel,
                yLabel,
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        CategoryPlot plot = chart.getCategoryPlot();

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, CHART_COLORS[0]);
        renderer.setDrawBarOutline(false);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelFont(LABEL_FONT);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickLabelFont(LABEL_FONT);

        chart.getTitle().setFont(TITLE_FONT);

        return new ChartPanel(chart);
    }

    public static ChartPanel createPieChart(String title, Map<String, Number> data) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Map.Entry<String, Number> entry : data.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                title,
                dataset,
                true,
                true,
                false
        );

        PiePlot plot = (PiePlot) chart.getPlot();

        int colorIndex = 0;
        for (Object key : dataset.getKeys()) {
            plot.setSectionPaint((Comparable<?>) key, CHART_COLORS[colorIndex % CHART_COLORS.length]);
            colorIndex++;
        }

        plot.setLabelFont(LABEL_FONT);

        chart.getTitle().setFont(TITLE_FONT);

        return new ChartPanel(chart);
    }

    public static ChartPanel createLineChart(String title, String xLabel, String yLabel,
                                             Map<LocalDate, Number> data) {
        TimeSeries series = new TimeSeries("Datos");

        for (Map.Entry<LocalDate, Number> entry : data.entrySet()) {
            LocalDate date = entry.getKey();
            series.add(new Day(date.getDayOfMonth(), date.getMonthValue(), date.getYear()),
                    entry.getValue());
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                title,
                xLabel,
                yLabel,
                dataset,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, CHART_COLORS[0]);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        chart.getTitle().setFont(TITLE_FONT);

        return new ChartPanel(chart);
    }

    public static ChartPanel createMultiLineChart(String title, String xLabel, String yLabel,
                                                  Map<String, Map<LocalDate, Number>> seriesData) {
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        int seriesIndex = 0;
        for (Map.Entry<String, Map<LocalDate, Number>> seriesEntry : seriesData.entrySet()) {
            String seriesName = seriesEntry.getKey();
            Map<LocalDate, Number> data = seriesEntry.getValue();

            TimeSeries series = new TimeSeries(seriesName);

            for (Map.Entry<LocalDate, Number> entry : data.entrySet()) {
                LocalDate date = entry.getKey();
                series.add(new Day(date.getDayOfMonth(), date.getMonthValue(), date.getYear()),
                        entry.getValue());
            }

            dataset.addSeries(series);
            seriesIndex++;
        }

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                title,
                xLabel,
                yLabel,
                dataset,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            renderer.setSeriesPaint(i, CHART_COLORS[i % CHART_COLORS.length]);
            renderer.setSeriesStroke(i, new BasicStroke(2.0f));
        }

        chart.getTitle().setFont(TITLE_FONT);

        return new ChartPanel(chart);
    }

    public static ChartPanel createCategoryLineChart(String title, String xLabel, String yLabel,
                                                     Map<String, Map<String, Number>> seriesData) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Map<String, Number>> seriesEntry : seriesData.entrySet()) {
            String seriesName = seriesEntry.getKey();
            Map<String, Number> data = seriesEntry.getValue();

            for (Map.Entry<String, Number> entry : data.entrySet()) {
                dataset.addValue(entry.getValue(), seriesName, entry.getKey());
            }
        }

        JFreeChart chart = ChartFactory.createLineChart(
                title,
                xLabel,
                yLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        CategoryPlot plot = chart.getCategoryPlot();

        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();

        for (int i = 0; i < dataset.getRowCount(); i++) {
            renderer.setSeriesPaint(i, CHART_COLORS[i % CHART_COLORS.length]);
            renderer.setSeriesStroke(i, new BasicStroke(2.0f));
        }

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelFont(LABEL_FONT);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickLabelFont(LABEL_FONT);

        chart.getTitle().setFont(TITLE_FONT);

        return new ChartPanel(chart);
    }
}