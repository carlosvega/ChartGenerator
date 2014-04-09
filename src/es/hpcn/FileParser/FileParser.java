package es.hpcn.FileParser;
import es.hpcn.LineParser.LineParser;
import es.hpcn.main;

import java.io.*;
import java.util.concurrent.Semaphore;

/**
 * Created by carlosvega on 01/04/14.
 */
public class FileParser {

    private String filename = null;
    private String path = null;
    private long totalBytes = 0;
    private int lineCounter = 0;

    //QUOTA THREAD
    protected Semaphore semaphore = new Semaphore(1);
    private boolean running = true;
    private Thread quotaThread = null;
    private LineParser[] lineParsers = null;

    public FileParser(String filename, LineParser[] lineParsers) {
        this.filename = filename;
        this.lineParsers = lineParsers;
    }

    public FileParser(String filename, String path, LineParser[] lineParsers) {
        this.filename = filename;
        this.path = path;
        this.lineParsers = lineParsers;
    }

    private void loadFileInfo(){
        try {
            File file = new File(this.filename);
            this.totalBytes = file.length();
        }catch (NullPointerException e){
            System.err.println("Error reading the file.");
        }
    }

    private BufferedReader getBufferedReader() throws FileNotFoundException {
        FileReader file = null;
        BufferedReader br = null;
        if (this.filename.equals("-")) {
            br = new BufferedReader(new InputStreamReader(System.in), 1024 * 1024);
        } else {
            loadFileInfo();
            file = new FileReader(this.filename);
            //GET SIZE OF FILE
            br = new BufferedReader(file, 1024 * 1024);
        }

        return br;
    }



    public void parseFile() {

        BufferedReader br = null;
        String line = "";
        try {
            br = getBufferedReader();
            startQuotaThread(); //QUOTA THREAD

            while ((line = br.readLine()) != null) {
                semaphore.acquire();
                lineCounter++;
                LineParser.parseLine(line, main.getSeparator(), this.lineParsers);
                semaphore.release();
            }

            stopQuotaThread(); //QUOTA THREAD

        } catch (FileNotFoundException e) {
            System.err.println("File Not Found\n");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.err.println("The file has been read.");
        parseQuota();

        try {
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void parseQuota(){
        for(LineParser p : this.lineParsers) {
            p.createChart(this.lineCounter);
        }
    }


    //THREAD QUOTA
    private Runnable thread_task = new Runnable() {

        @Override
        public void run() {
            try {
                while (running) {
                    Thread.sleep(main.getQuota() * 60 * 60 * 1000);
                    semaphore.acquire();
                    //parseQuota();

                    //createDirectories();
                    semaphore.release();
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    };

    public void startQuotaThread(){
        if (main.getQuota() > 0) {
            this.quotaThread = new Thread(thread_task);
            this.quotaThread.start();
        }
    }

    public void stopQuotaThread(){
        if (main.getQuota() > 0) {
            running = false;
            this.quotaThread.interrupt();
        }
    }

    //GETTERS
    public String getPath(){
        return this.path;
    }

    public String getFilename(){
        return filename;
    }

}
