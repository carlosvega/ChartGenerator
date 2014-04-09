package es.hpcn.LineParser;

/**
 * Created by carlosvega on 01/04/14.
 */
public class LineCounterLong extends LineCounter<Long>{

    public LineCounterLong(int mainColumn, String title, String xAxis, String yAxis, String filename){
        super(mainColumn, title, xAxis, yAxis, filename);
    }

    @Override
    public Long convert(String text){
        return Long.parseLong(text);
    }

    @Override
    public String toString(){
        return "LineCounterLong mC: " + this.getMainColumn();
    }

}
