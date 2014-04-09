package es.hpcn.LineParser;

/**
 * Created by carlosvega on 01/04/14.
 */
public class LineCounterString extends LineCounter<String>{
    public LineCounterString(int mainColumn, String title, String xAxis, String yAxis, String filename){
        super(mainColumn, title, xAxis, yAxis, filename);
    }

    @Override
    public String convert(String text){
        return text;
    }

    @Override
    public String toString(){
        return "LineCounterString mC: " + this.getMainColumn();
    }
}
