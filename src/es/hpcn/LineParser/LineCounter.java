package es.hpcn.LineParser;

import es.hpcn.Charts.CTRChart;
import es.hpcn.Charts.Chart;
import es.hpcn.Counter;
import es.hpcn.main;
import org.jfree.data.category.DefaultCategoryDataset;

import java.net.InetAddress;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by carlosvega on 01/04/14.
 */
public abstract class LineCounter<T> extends LineParser {

    protected Counter<T> counter = new Counter<T>();
    protected String title = null;
    protected String xAxis = null;
    protected String yAxis = null;
    protected String filename = null;

    public LineCounter(int mainColumn, String title, String xAxis, String yAxis, String filename){
        super(mainColumn);
        this.title = title;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.filename = filename;
    }

    public T parseLine(String[] splitLine) {
        final T converted = convert(splitLine[this.getMainColumn()]);
        counter.update(converted);
        return converted;
    }

    public abstract T convert(String text);

    @Override
    public void createChart(int sampleSize){
        System.err.println(String.format("Creating CTR chart %s... in file %s", this.title, this.filename));

        final Counter.CounterComparator<T> comparator = new Counter.CounterComparator<T>();

        // ORDER IPS
        PriorityQueue<Map.Entry<T, Integer>> heap = new PriorityQueue<Map.Entry<T, Integer>>(
                20, comparator);
        heap.addAll(counter.getDictionary().entrySet());

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < main.getTop() && !heap.isEmpty(); i++) {
            Map.Entry<T, Integer> entry = heap.poll();
            if(entry.getKey() instanceof InetAddress){
                InetAddress ip = (InetAddress) entry.getKey();
                dataset.setValue((Number) entry.getValue(), 1, ip.getHostAddress());
            }else{
                dataset.setValue((Number) entry.getValue(), 1, entry.getKey().toString());
            }
        }

        CTRChart chart = new CTRChart(this.title, this.xAxis, this.yAxis, filename,
                Chart.DEFAULT_WIDTH, Chart.DEFAULT_HEIGHT, dataset);

        chart.saveChart(main.getDpi());

    }

    public void createChart(){
        System.out.println(toString()+ " " + counter.size());

        return;
    }



    public void clear(){
        counter.clear();
        counter = null;
    }

    @Override
    public String toString(){
        return "LineCounter mC: " + this.getMainColumn();
    }

}
