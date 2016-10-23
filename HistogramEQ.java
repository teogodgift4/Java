//histogram equalization programm in Java

package histogrameq;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Image histogram equalization
 *
 * Author: freaksoul
 *
 */
/**
 * @param args the command line arguments
 */
public class HistogramEQ extends JFrame {

    private static BufferedImage original, equalized;
    public static String name;

    public static void main(String[] args) throws IOException {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int res = fileChooser.showOpenDialog(fileChooser);
        if (res == JFileChooser.CANCEL_OPTION) {
            System.exit(1);
        }
        File filename = fileChooser.getSelectedFile();
        name = filename.getAbsolutePath();
        original = ImageIO.read(new File(name));
        equalized = histogramEqualization(original);
        writeImage("Equalized_Image");

    }

    private static void writeImage(String output) throws IOException {
        File file = new File(output + ".jpg");
        ImageIO.write(equalized, "jpg", file);
    }

    private static BufferedImage histogramEqualization(BufferedImage initial) {

        int red;
        int green;
        int blue;
        int alpha;
        int Pixel = 0;

        // histogram table
        ArrayList<int[]> histTable = histogramEqualizationTable(initial);

        BufferedImage histogramEQ = new BufferedImage(initial.getWidth(), initial.getHeight(), initial.getType());

        for (int i = 0; i < initial.getWidth(); i++) {
            for (int j = 0; j < initial.getHeight(); j++) {

                // Get pixels by R, G, B
                alpha = new Color(initial.getRGB(i, j)).getAlpha();
                red = new Color(initial.getRGB(i, j)).getRed();
                green = new Color(initial.getRGB(i, j)).getGreen();
                blue = new Color(initial.getRGB(i, j)).getBlue();

                // Set new pixel values using the histogram lookup table
                red = histTable.get(0)[red];
                green = histTable.get(1)[green];
                blue = histTable.get(2)[blue];

                // Return back to original format
                Pixel = colorToRGB(alpha, red, green, blue);

                // Write pixels into image
                histogramEQ.setRGB(i, j, Pixel);

            }
        }

        return histogramEQ;

    }

    // R, G, B
    private static ArrayList<int[]> histogramEqualizationTable(BufferedImage initial) {

        // Get an image histogram - calculated values by R, G, B channels
        ArrayList<int[]> imageHist = getHistogram(initial);

        // Create the lookup table
        ArrayList<int[]> imageTable = new ArrayList<int[]>();

        // Fill the lookup table
        int[] redhistogram = new int[256];
        int[] greenhistogram = new int[256];
        int[] bluehistogram = new int[256];

        for (int i = 0; i < redhistogram.length; i++) {
            redhistogram[i] = 0;
        }
        for (int i = 0; i < greenhistogram.length; i++) {
            greenhistogram[i] = 0;
        }
        for (int i = 0; i < bluehistogram.length; i++) {
            bluehistogram[i] = 0;
        }

        long redSum = 0;
        long greenSum = 0;
        long blueSum = 0;

        // scale factor
        float scale = (float) (255.0 / (initial.getWidth() * initial.getHeight()));

        for (int i = 0; i < redhistogram.length; i++) {
            redSum += imageHist.get(0)[i];
            int RedVal = (int) (redSum * scale);
            if (RedVal > 255) {
                redhistogram[i] = 255;
            } else {
                redhistogram[i] = RedVal;
            }

            greenSum += imageHist.get(1)[i];
            int greenVal = (int) (greenSum * scale);
            if (greenVal > 255) {
                greenhistogram[i] = 255;
            } else {
                greenhistogram[i] = greenVal;
            }

            blueSum += imageHist.get(2)[i];
            int blueVal = (int) (blueSum * scale);
            if (blueVal > 255) {
                bluehistogram[i] = 255;
            } else {
                bluehistogram[i] = blueVal;
            }
        }

        imageTable.add(redhistogram);
        imageTable.add(greenhistogram);
        imageTable.add(bluehistogram);

        return imageTable;

    }

    // R, G, B histogram
    public static ArrayList<int[]> getHistogram(BufferedImage initial) {

        int[] redhistogram = new int[256];
        int[] greenhistogram = new int[256];
        int[] bluehistogram = new int[256];

        for (int i = 0; i < redhistogram.length; i++) {
            redhistogram[i] = 0;
        }
        for (int i = 0; i < greenhistogram.length; i++) {
            greenhistogram[i] = 0;
        }
        for (int i = 0; i < bluehistogram.length; i++) {
            bluehistogram[i] = 0;
        }

        for (int i = 0; i < initial.getWidth(); i++) {
            for (int j = 0; j < initial.getHeight(); j++) {

                int red = new Color(initial.getRGB(i, j)).getRed();
                int green = new Color(initial.getRGB(i, j)).getGreen();
                int blue = new Color(initial.getRGB(i, j)).getBlue();

                
                redhistogram[red]++;
                greenhistogram[green]++;
                bluehistogram[blue]++;

            }
        }

        ArrayList<int[]> histogram = new ArrayList<int[]>();
        histogram.add(redhistogram);
        histogram.add(greenhistogram);
        histogram.add(bluehistogram);

        return histogram;

    }

    // 8 bit convertion
    private static int colorToRGB(int alpha, int red, int green, int blue) {

        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;

    }

}
