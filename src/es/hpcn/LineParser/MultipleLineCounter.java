package es.hpcn.LineParser;

import java.util.HashMap;

/**
 * Created by carlosvega on 09/04/14.
 */
public class MultipleLineCounter<T> extends LineCounter<T> {

    private int key = 0;
    private ColumnTypeEnum mainColumnClass = null;
    private HashMap<String, LineCounter> lineParsers = new HashMap<String, LineCounter>();

    public MultipleLineCounter(int mainColumn, int key, String title, String xAxis, String yAxis, String filename, LineParser.ColumnTypeEnum mainColumnClass){
        super(key, title, xAxis, yAxis, filename);
        this.key = mainColumn;
        this.mainColumnClass = mainColumnClass;
    }

    @Override
    public void clear(){
        counter.clear();
        counter = null;
        for(LineCounter l : lineParsers.values()){
            l.clear();
        }
        lineParsers.clear();
        lineParsers = null;
    }

    @Override
    public T parseLine(String[] splitLine) {
        final String key = splitLine[this.key];

        LineCounter parser = getLineParserFromHash(key);
        parser.parseLine(splitLine);
        return null;
    }

    @Override
    public T convert(String text){
        return null;
    }


    @Override
    public void createChart(int lineCounter){
        for(LineParser l : lineParsers.values()){
            l.createChart(lineCounter);
        }
    }

    @Override
    public String toString(){
        return "MultipleLineCounter mC: " + this.getMainColumn() + " Key: " + this.key;
    }

    private LineCounter getLineParserFromHash(String key){
        if(!lineParsers.containsKey(key)){
            lineParsers.put(key, createNewLineParser(key));
        }

        return lineParsers.get(key);
    }

    private LineCounter createNewLineParser(String key){
        switch (mainColumnClass){
            case STR:
                return new LineCounterString(getMainColumn(), key + " " + title, xAxis, yAxis, key+filename);
            case LONG:
                return new LineCounterLong(getMainColumn(), key + " " + title, xAxis, yAxis, key+filename);
            case DOUBLE:
                return new LineCounterDouble(getMainColumn(), key + " " + title, xAxis, yAxis, key + filename);
            case IP:
                return new LineCounterInetAddress(getMainColumn(), key + " " + title, xAxis, yAxis, key + filename);
        }

        return null;
    }

}
