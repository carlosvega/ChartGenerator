package es.hpcn.FileParser;

import es.hpcn.LineParser.LineParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by carlosvega on 03/04/14.
 */
public class FileIntervalParser extends FileParser {

    private final int SKIP_LINES_DEFAULT_MODULE = 2;
    private int load_measure = 0;


    public FileIntervalParser(String filename, LineParser[] lineParsers) {
        super(filename, lineParsers);
    }

    public FileIntervalParser(String filename, String path, LineParser[] lineParsers) {
        super(filename, path, lineParsers);
    }

    private long parseFileByteRange(long startByte, long endByte, LineParser[] lineParsers) {

        final int bytesToRead = (int) (endByte - startByte);

        byte[] buffer = new byte[512];
        StringBuffer sBuffer = new StringBuffer(4096);
        int nRead = 0;
        long total = 0L;
        int last_sec = 0;
        BufferParser bp = new BufferParser(4096);
        String[] lines = null;
        RandomAccessFile rfile = null;
        int offset;
        try {
            rfile = new RandomAccessFile(super.getFilename(), "r");
            //GET SIZE OF FILE
            rfile.seek(startByte);
            readingLoop: while ((nRead = rfile.read(buffer)) != -1) {
                this.semaphore.acquire();

                lines = bp.append(buffer);

                //sBuffer.append(new String(buffer));
                //String[] lines;

                //if(sBuffer.charAt(sBuffer.length()-1) == '\n'){
                //    offset = 1;
                //}

                //lines = sBuffer.toString().split("\n");

                for (int i = 0; i < bp.length()-1; i++) {
                    if(this.load_measure == 0 || i%(this.load_measure*SKIP_LINES_DEFAULT_MODULE) == 0) {
                        LineParser.parseLine(lines[i], " ", lineParsers);
                        //last_sec = f.parseLine(lines[i]);
                    }else{
                        //conections_per_sec.update(last_sec);
                        //line_counter++;
                    }
                }

                bp.reset(lines[bp.length()-1]);
                //sBuffer = new StringBuffer(offset == 1 ? "" : lines[lines.length - 1 + offset]);
                buffer = new byte[512];
                offset = 0;
                if (rfile.getFilePointer() >= endByte) {
                    break readingLoop;
                }

                semaphore.release();
                total += nRead;
            }

            semaphore.release();

            if (rfile != null)
                rfile.close();

        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO Exception");
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            System.out.println("Interrupted Exception");
            e.printStackTrace();
        }

        return total;
    }

}
