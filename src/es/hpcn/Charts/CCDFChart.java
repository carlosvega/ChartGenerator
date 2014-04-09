package es.hpcn.Charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Created by carlosvega on 07/04/14.
 */
public class CCDFChart extends Chart{

    private XYSeriesCollection dataset = null;
    private Double lastNumber = null;

    public CCDFChart(String title, String x_axis_label, String y_axis_label, String filename, int width, int height, XYSeriesCollection dataset, double lastNumber){
        super(title, x_axis_label, y_axis_label, filename, width, height);
        this.lastNumber = lastNumber;
        this.dataset = dataset;
        this.prepareChart();
    }

    public CCDFChart(String title, String x_axis_label, String y_axis_label, String filename, int width, int height, int draw_width, int draw_height, XYSeriesCollection dataset, double lastNumber){
        super(title, x_axis_label, y_axis_label, filename, width, height, draw_width, draw_height);
        this.lastNumber = lastNumber;
        this.dataset = dataset;
        this.prepareChart();
    }

    private void prepareChart(){
        this.chart = ChartFactory.createXYLineChart(this.title,
                this.x_axis_label, this.y_axis_label, dataset,
                PlotOrientation.VERTICAL, false, false, false);
        this.chart.setAntiAlias(true);
        this.chart.setTextAntiAlias(true);

        this.chart.getXYPlot().getRangeAxis().setRange(0, 1);
        LogarithmicAxis hla = new LogarithmicAxis(this.x_axis_label);
        hla.setRange(1, Math.pow(10, Math.log10(lastNumber) + 1));
        this.chart.getXYPlot().setDomainAxis(hla);
    }

}
