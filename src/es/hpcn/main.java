package es.hpcn;

import es.hpcn.FileParser.FileParser;
import es.hpcn.LineParser.LineParser;
import lombok.Getter;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.util.regex.Pattern;

public class main {

    @Option(name = "-f", usage = "Filter.", aliases = "--filter")
    private static String filter = null;

    @Option(name = "-fm", usage = "Filter Mode: {ip, url, domain}", aliases = "--filter-mode")
    @Getter private static String filterMode = null;
    @Getter private static int filterModeInt = 0;

    @Option(name = "--flowprocess", usage = "The input is flowprocess output. The filters are useless with this option.")
    private static boolean flowprocess = false;

    @Option(name = "-d", usage = "Directory where output will be save", aliases = "--directory")
    private static String directory = null;

    @Option(name = "-H", usage = "Replaces the hostnames in the URLs by its IPs.", aliases = "--no-hostnames")
    private static boolean noHostNames = false;

    @Option(name = "-i", usage = "Input File. Type '-i -' for reading from sdtin.", required = true, aliases = "--input")
    private static String filename;

    @Option(name = "-I", usage = "File with the charts to be created.", required = true, aliases = "--chartFile")
    private static String chartFile;

    @Option(name = "-q", usage = "Quota. Hours. Create a folder full of charts every <quota> hours.", aliases = "--quota")
    private static int quota = 0;

    @Option(name = "-r", usage = "DPI resolution. Default 1000.", aliases = "--dpi")
    private static int dpi = 1000;

    @Option(name = "-s", usage = "Separator field character. The default is the space.", aliases = "--separator")
    private static String separator = " ";

    @Option(name = "-t", usage = "Chart bars top.", aliases = "--top")
    private static int top = 10;

    @Option(name = "-U", usage = "Removes paramters in the URLs.", aliases = "--chomp-urls")
    private static boolean chompURL = false;

    @Option(name = "-x", usage = "Index. Reads index from given file", aliases = "--index")
    private static String index = null;

    @Option(name = "--short-index", usage = "Short index version.")
    private static boolean shortIndex = false;

    @Option(name = "-F", usage = "Process file from given timestamp in seconds.", aliases = "--from")
    private static Long from = 0L;

    @Option(name = "-T", usage = "Process file up to the given timestamp in seconds.", aliases = "--to")
    private static Long to = 0L;

    private static Pattern pattern = null;

    public static void main(String[] args) {

        CmdLineParser parser = new CmdLineParser(new main());

        parser.setUsageWidth(80);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar chart_scripts.jar arguments...");
            parser.printUsage(System.err);
            System.err.println();
            return;
        }

        if (main.getQuota() > 0) {
            directory = filename + "_folder";
        }

        if (main.getDirectory() != null) {
            File dir = new File(directory);
            if (!dir.exists()) {
                System.err.println("Creating directory: " + directory);
                boolean result = dir.mkdirs();
                if (!result) {
                    System.err.println("Couldn't create the directory: "
                            + directory);
                    System.exit(-2);
                }
            }
        }

        if (filterMode != null || filter != null) {
            if (filter == null) {
                System.err
                        .println("Introduce the filter with the option: --filter");
            }
            if (filterMode == null) {
                System.err
                        .println("Introduce the filter mode with the option: --filter-mode");
            }
        }

        if (filterMode != null && filter != null) {
            pattern = Pattern.compile(filter);
            if (filterMode.equals("ip")) {
                filterModeInt = 1;
            } else if (filterMode.equals("url")) {
                filterModeInt = 2;
            } else if (filterMode.equals("domain")) {
                filterModeInt = 3;
            } else {
                System.err
                        .println("Invalid filter mode. Must be one of {ip, url, domain}");
                return;
            }
        }

        if (!filename.equals("-")) {
            System.out.println(filename);
        } else {
            System.out.println("Reading from STDIN");
        }


        try {
            LineParser[] parsers = LineParser.getLineParsersFromFile(main.getChartFile());
            for(LineParser l : parsers) {
                System.out.println(l);
            }
            FileParser fp = new FileParser(getFilename(), parsers);
            fp.parseFile();

        } catch (MalformedInputException e){
            System.err.println("Error, invalid charts file format.");
        } catch (IOException e){
            e.printStackTrace();
        }

    }





    //GETTERS
    public static String getFilter(){
        return filter;
    }

    public static String getFilterMode(){
        return filterMode;
    }

    public static int getFilterModeInt(){
        return filterModeInt;
    }

    public static boolean isFlowprocess(){
        return flowprocess;
    }

    public static String getDirectory(){
        return directory;
    }

    public static boolean isNoHostNames(){
        return noHostNames;
    }

    public static String getFilename(){
        return filename;
    }

    public static String getChartFile(){
        return chartFile;
    }

    public static int getQuota(){
        return quota;
    }

    public static int getDpi(){
        return dpi;
    }

    public static String getSeparator(){
        return separator;
    }

    public static int getTop(){
        return top;
    }

    public static boolean isChompURL(){
        return chompURL;
    }

    public static String getIndex(){
        return index;
    }

    public static boolean isShortIndex(){
        return shortIndex;
    }

    public static Long getFrom(){
        return from;
    }

    public static Long getTo(){
        return to;
    }
}
