package es.hpcn.LineParser;

import es.hpcn.Charts.Chart;
import es.hpcn.Charts.TimeSeriesChart;
import es.hpcn.Counter;
import es.hpcn.main;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeriesCollection;

import java.util.*;

/**
 * Created by carlosvega on 02/04/14.
 */
public class TimeSeries extends LineCounterLong {

    public TimeSeries(int mainColumn, String title, String xAxis, String yAxis, String filename) {
        super(mainColumn, title, xAxis, yAxis, filename);
    }

    @Override
    public Long convert(String text) {
        return Double.valueOf(text).longValue();
    }

    @Override
    public String toString() {
        return "TimeSeries, mC: " + this.getMainColumn();
    }

    @Override
    public void createChart(int lineCounter) {

        System.err.println(String.format("Creating CTR chart %s... in file %s", this.title, this.filename));

        Counter.CounterComparatorByKey<Long> comparator = new Counter.CounterComparatorByKey<Long>();
        PriorityQueue<Map.Entry<Long, Integer>> secsHeap = new PriorityQueue<Map.Entry<Long, Integer>>(counter.size() + 1, comparator);
        secsHeap.addAll(counter.getDictionary().entrySet());

        org.jfree.data.time.TimeSeries s1 = new org.jfree.data.time.TimeSeries("Connections per Second");
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        Long from = null;
        long milisecs = 0;
        Map.Entry<Long, Integer> entry = null;
        while (!secsHeap.isEmpty()) {
            entry = secsHeap.poll();
            milisecs = entry.getKey() * 1000;

            s1.add(new Second(new Date(milisecs), TimeZone.getTimeZone("UTC"),
                    Locale.ENGLISH), (Number) entry.getValue());
            if (from == null) {
                from = milisecs;
            }
        }
        dataset.addSeries(s1);

        TimeSeriesChart chart = new TimeSeriesChart(this.title, this.xAxis, this.yAxis, this.filename,
                Chart.DEFAULT_WIDTH, Chart.DEFAULT_HEIGHT, Chart.DEFAULT_DRAW_WIDTH, Chart.DEFAULT_DRAW_HEIGHT, dataset, from, milisecs);
        chart.saveChart(main.getDpi());
    }

}
