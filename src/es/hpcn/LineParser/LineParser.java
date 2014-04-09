package es.hpcn.LineParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by carlosvega on 01/04/14.
 */
abstract public class LineParser<T> {
    private int mainColumn = 0;

    public LineParser(int mainColumn){
        this.mainColumn = mainColumn;
    }

    public static LineParser[] getLineParsersFromFile(String filename) throws IOException, MalformedInputException {
        BufferedReader br = new BufferedReader(new FileReader(filename));

        String line = "";
        ArrayList<LineParser> lineParsers = new ArrayList<LineParser>();

        while((line = br.readLine()) != null){
            lineParsers.add(createLineParserFromLine(line.split(",")));
        }

        LineParser[] parsers = new LineParser[lineParsers.size()];

        return lineParsers.toArray(parsers);
    }


    public static LineParser createLineParserFromLine(String[] columns) throws MalformedInputException {

        if(columns.length < 5)
            throw new MalformedInputException(columns.length);

        final LineParserEnum lineParserType = LineParserEnum.valueOf(columns[0].toUpperCase());
        ColumnTypeEnum mainColumnType = null;
        int mainColumn = 0;
        String title = null;
        String xAxis = null;
        String yAxis = null;
        String filename = null;

        switch (lineParserType) {
            case CTR:
                mainColumn = Integer.parseInt(columns[1]);
                mainColumnType = ColumnTypeEnum.valueOf(columns[2].toUpperCase());
                title = columns[3];
                xAxis = columns[4];
                yAxis = columns[5];
                filename = columns[6];
                switch (mainColumnType){
                    case STR:
                        return new LineCounterString(mainColumn, title, xAxis, yAxis, filename);
                    case LONG:
                        return new LineCounterLong(mainColumn, title, xAxis, yAxis, filename);
                    case DOUBLE:
                        return new LineCounterDouble(mainColumn, title, xAxis, yAxis, filename);
                    case IP:
                        return new LineCounterInetAddress(mainColumn, title, xAxis, yAxis, filename);
                }
                break;
            case CCDF:
                mainColumn = Integer.parseInt(columns[1]);
                title = columns[2];
                xAxis = columns[3];
                yAxis = columns[4];
                filename = columns[5];
                return new CCDF(mainColumn, title, xAxis, yAxis, filename);
            case CTREACH:
                mainColumn = Integer.parseInt(columns[1]);
                mainColumnType = ColumnTypeEnum.valueOf(columns[2].toUpperCase());
                int key = Integer.parseInt(columns[3]);
                title = columns[4];
                xAxis = columns[5];
                yAxis = columns[6];
                filename = columns[7];
                switch (mainColumnType){
                        case STR:
                            return new MultipleLineCounter<String>(mainColumn, key, title, xAxis, yAxis, filename, mainColumnType);
                        case LONG:
                            return new MultipleLineCounter<Long>(mainColumn, key, title, xAxis, yAxis, filename, mainColumnType);
                        case DOUBLE:
                            return new MultipleLineCounter<Double>(mainColumn, key, title, xAxis, yAxis, filename, mainColumnType);
                        case IP:
                            return new MultipleLineCounter<InetAddress>(mainColumn, key, title, xAxis, yAxis, filename, mainColumnType);
                }
                break;
            case TIMESERIES:
                mainColumn = Integer.parseInt(columns[1]);
                title = columns[2];
                xAxis = columns[3];
                yAxis = columns[4];
                filename = columns[5];
                return new TimeSeries(mainColumn, title, xAxis, yAxis, filename);
        }

        throw new MalformedInputException(1);
    }

    public static int parseLine(String line, String separator, LineParser[] parsers){
        try {
            String[] splitLine = line.split(Pattern.quote(separator));
            for (int i = 0; i < parsers.length; i++) {
                parsers[i].parseLine(splitLine);
            }
        } catch (PatternSyntaxException e){
            System.err.println("Error, invalid separator.");
            System.exit(-1);
        } catch (IndexOutOfBoundsException e){
            System.err.println("Error parsing lines, check the given columns and separator.");
            System.exit(-1);
        }

        return 0;
    }

    public abstract T parseLine(String[] splitLine) throws IndexOutOfBoundsException;

    public abstract void createChart(int sampleSize);


    public String toString(){
        return "LineParser mC: " + mainColumn;
    }






    //ENUMS
    public static enum LineParserEnum {
        CTR,
        CCDF,
        TIMESERIES,
        CTREACH
    }

    public static enum ColumnTypeEnum {
        STR,
        LONG,
        DOUBLE,
        IP
    }

    //GETTERS
    public int getMainColumn(){
        return mainColumn;
    }

}
