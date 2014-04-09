package es.hpcn.Charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Created by carlosvega on 07/04/14.
 */
public class CTRChart extends Chart{

    private DefaultCategoryDataset dataset = null;
    private Double lastNumber = null;

    public CTRChart(String title, String x_axis_label, String y_axis_label, String filename, int width, int height, DefaultCategoryDataset dataset){
        super(title, x_axis_label, y_axis_label, filename, width, height);
        this.dataset = dataset;
        this.prepareChart();
    }

    public CTRChart(String title, String x_axis_label, String y_axis_label, String filename, int width, int height, int draw_width, int draw_height, DefaultCategoryDataset dataset){
        super(title, x_axis_label, y_axis_label, filename, width, height, draw_width, draw_height);
        this.dataset = dataset;
        this.prepareChart();
    }

    private void prepareChart(){
        BarRenderer.setDefaultBarPainter(new StandardBarPainter());
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        this.chart = ChartFactory.createBarChart(this.title, this.x_axis_label,
                this.y_axis_label, dataset, PlotOrientation.VERTICAL, false,
                false, false);

        this.chart.setAntiAlias(true);
        this.chart.setTextAntiAlias(true);

        this.chart.getCategoryPlot().getDomainAxis()
                .setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        BarRenderer renderer = new BarRenderer();
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
        this.chart.getCategoryPlot().setRenderer(renderer);
    }
}
