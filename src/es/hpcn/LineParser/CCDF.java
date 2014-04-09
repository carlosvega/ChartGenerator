package es.hpcn.LineParser;

import es.hpcn.Charts.CCDFChart;
import es.hpcn.Charts.Chart;
import es.hpcn.Counter;
import es.hpcn.main;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by carlosvega on 03/04/14.
 */
public class CCDF extends LineCounterLong {

    public CCDF(int mainColumn, String title, String xAxis, String yAxis, String filename){
        super(mainColumn, title, xAxis, yAxis, filename);
    }

    @Override
    public Long convert(String text){
        Double d = Double.valueOf(text)*1000;
        return d.longValue();
    }

    @Override
    public void createChart(int sampleSize){
        System.err.println(String.format("Creating CCDF chart %s... in file %s", this.title, this.filename));

        Counter.CounterComparatorByKey<Long> comparator = new Counter.CounterComparatorByKey<Long>();
        PriorityQueue<Map.Entry<Long, Integer>> ccdfHeap = new PriorityQueue<Map.Entry<Long, Integer>>(
                20, comparator);
        ccdfHeap.addAll(counter.getDictionary().entrySet());

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("CCDF");

        double suma = 0;
        double lastNumber = 0;
        ccdfHeap.poll();
        while (!ccdfHeap.isEmpty()) {
            Map.Entry<Long, Integer> entry = ccdfHeap.poll();

            lastNumber = entry.getKey();
            double cant = entry.getValue();
            series.add(lastNumber, 1 - suma);
            suma += cant / sampleSize;
        }

        dataset.addSeries(series);

        CCDFChart chart = new CCDFChart(this.title, this.xAxis, this.yAxis, this.filename,
                Chart.DEFAULT_WIDTH, Chart.DEFAULT_HEIGHT, Chart.DEFAULT_DRAW_WIDTH, Chart.DEFAULT_DRAW_HEIGHT, dataset, lastNumber);
        chart.saveChart(main.getDpi());

    }

    @Override
    public String toString(){
        return String.format("CCDF mC: %d Title: %s xAxis: %s yAxis: %s", this.getMainColumn(), this.title, this.xAxis, this.yAxis);
    }
}
