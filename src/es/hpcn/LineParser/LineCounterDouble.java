package es.hpcn.LineParser;

/**
 * Created by carlosvega on 01/04/14.
 */
public class LineCounterDouble extends LineCounter<Double> {

    public LineCounterDouble(int mainColumn, String title, String xAxis, String yAxis, String filename){
        super(mainColumn, title, xAxis, yAxis, filename);
    }

    @Override
    public Double convert(String text){
        return Double.parseDouble(text);
    }

    @Override
    public String toString(){
        return "LineCounterDouble mC: " + this.getMainColumn();
    }

}
