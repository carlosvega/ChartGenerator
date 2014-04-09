package es.hpcn.LineParser;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LineCounterInetAddress extends LineCounter<InetAddress>{

    public LineCounterInetAddress(int mainColumn, String title, String xAxis, String yAxis, String filename){
        super(mainColumn, title, xAxis, yAxis, filename);
    }

    @Override
    public InetAddress convert(String text){
        InetAddress ip = null;
        try {
            ip = InetAddress.getByName(text);
        } catch (UnknownHostException e) {
            System.err.println("Error en el formato de la ip: "
                    + text);
            System.exit(-1);
        }

        return ip;
    }

    @Override
    public String toString(){
        return "LineCounterInetAddress mC: " + this.getMainColumn();
    }

}



