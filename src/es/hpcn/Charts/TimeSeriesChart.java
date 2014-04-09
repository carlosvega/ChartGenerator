package es.hpcn.Charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by carlosvega on 09/04/14.
 */
public class TimeSeriesChart extends Chart {

    private XYDataset dataset = null;
    private long from;
    private long to;

    public TimeSeriesChart(String title, String x_axis_label, String y_axis_label, String filename, int width, int height, TimeSeriesCollection dataset, long from, long to){
        super(title, x_axis_label, y_axis_label, filename, width, height);
        this.dataset = dataset;
        this.prepareChart();
        this.from = from;
        this.to = to;
        this.prepareChart();
    }

    public TimeSeriesChart(String title, String x_axis_label, String y_axis_label, String filename, int width, int height, int draw_width, int draw_height, TimeSeriesCollection dataset, long from, long to){
        super(title, x_axis_label, y_axis_label, filename, width, height, draw_width, draw_height);
        this.dataset = dataset;
        this.prepareChart();
        this.from = from;
        this.to = to;
        this.prepareChart();
    }

    private void prepareChart() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MMM/yyyy",
                Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String x_axis_label = "From: " + sdf.format(new Date(this.from))
                + "       To: " + sdf.format(new Date(this.to));

        this.chart = ChartFactory.createTimeSeriesChart(this.title, // title
                x_axis_label, // x-axis label
                this.y_axis_label, // y-axis label
                dataset, // data
                false, // create legend?
                false, // generate tooltips?
                false // generate URLs?
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        XYItemRenderer r = plot.getRenderer();
    }
}
