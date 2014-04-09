package es.hpcn.Charts;

import com.keypoint.PngEncoder;
import es.hpcn.main;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by carlosvega on 07/04/14.
 */
public class Chart {

    public static final int DEFAULT_DPI = 1000;
    public static final int DEFAULT_WIDTH = 2048;
    public static final int DEFAULT_HEIGHT = 1536;
    public static final int DEFAULT_DRAW_WIDTH = 800;
    public static final int DEFAULT_DRAW_HEIGHT = 600;


    protected JFreeChart chart = null;
    protected String title;
    protected String x_axis_label;
    protected String y_axis_label;
    private String filename;
    private int width;
    private int height;
    private double draw_height;
    private double draw_width;

    public Chart(String title, String x_axis_label, String y_axis_label,
                       String filename, int width, int height) {
        this.title = title;
        this.x_axis_label = x_axis_label;
        this.y_axis_label = y_axis_label;
        this.filename = filename;
        this.width = width;
        this.height = height;
        this.draw_width = width / 2;
        this.draw_height = height / 2;
    }

    public Chart(String title, String x_axis_label, String y_axis_label,
                       String filename, int width, int height, int draw_width,
                       int draw_height) {
        this.title = title;
        this.x_axis_label = x_axis_label;
        this.y_axis_label = y_axis_label;
        this.filename = filename;
        this.width = width;
        this.height = height;
        this.draw_width = draw_width;
        this.draw_height = draw_height;
    }

    public void saveChart() {
        try {
            ChartUtilities.saveChartAsPNG(new File(filename), this.chart,
                    this.width, this.height);
        } catch (IOException e) {
            System.out.println("Error creating file: " + this.filename);
        }
    }

    public void saveChart(int dpi) {
        final BufferedImage image = this.chart.createBufferedImage(this.width,
                this.height, this.draw_width, this.draw_height, null);
        final PngEncoder encoder = new PngEncoder(image, true, 0, 5);
        encoder.setDpi(dpi, dpi);
        final byte[] data = encoder.pngEncode();
        BufferedOutputStream out = null;
        try {
            File f = new File(main.getDirectory(), this.filename);
            out = new BufferedOutputStream(new FileOutputStream(f));
            out.write(data);
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't create the file: " + this.filename);
        } catch (IOException e) {
            System.out.println("Couldn't write to file: " + this.filename);
        }

    }

}
