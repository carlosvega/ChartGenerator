package es.hpcn.FileParser;

/**
 * Created by carlosvega on 03/04/14.
 */
public class BufferParser{

    private StringBuffer sBuffer = null;
    private int stringBufferSize = 0;
    private int offset = 0;
    private String[] lines;

    public BufferParser(int size){
        this.stringBufferSize = size;
        sBuffer = new StringBuffer(size);
    }

    public String[] append(byte[] buffer){
        sBuffer.append(new String(buffer));

        if(sBuffer.charAt(sBuffer.length()-1) == '\n'){
            offset = 1;
        }

        lines = sBuffer.toString().split("\n");

        return lines;
    }

    public int length(){
        return lines.length + offset;
    }

    public void reset(String s){
        sBuffer = new StringBuffer(offset == 1 ? "" : s);
    }

}
