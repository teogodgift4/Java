// A simple image equalization program in Java
// By Teo Theodoridis
// teo.godgift4@gmail.com
package histogrameq;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.scene.input.KeyCode.T;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
public class Histeq extends JFrame {

    private static BufferedImage original, equalized;
    private ArrayList<int[]> histogram;
    public static String name;
    private JButton OriginalImageButton;
    private JButton ModifiedImageButton;
    private JButton OriginalHistogramButton;
    private JButton ModifiedHistogramButton;
    private JLabel OriginalImagelabel;
    private JLabel ModifiedImageLabel;
    private JLabel ImageLabel;
    private Icon image;

    public Histeq() {
        super("A Simple Histogram Equalization Programm In Java");
        setLayout(new FlowLayout());
        OriginalImageButton = new JButton("Choose an Image");
        OriginalImageButton.setToolTipText("Please select an image from your computer");
        ModifiedImageButton = new JButton("Show the difference");
        ModifiedImageButton.setToolTipText("Show the equilized image");
        OriginalHistogramButton = new JButton("Original Histogram");
        OriginalHistogramButton.setToolTipText("Show the histogram of the image you selected");
        ModifiedHistogramButton = new JButton("Modified Histogram");
        ModifiedHistogramButton.setToolTipText("Show the histogram of the equilized image");
        ImageLabel = new JLabel();
        image = new ImageIcon(getClass().getResource("image_processing.jpg"));
        ImageLabel.setIcon(image);
        add(OriginalImageButton);
        add(ModifiedImageButton);
        add(OriginalHistogramButton);
        add(ModifiedHistogramButton);
        ImageLabel.setSize(384, 384);
        ImageLabel.setVisible(true);
        add(ImageLabel);

        //event Handlers
        ButtonHandler handler = new ButtonHandler();
        OriginalImageButton.addActionListener(handler);
        ModifiedImageButton.addActionListener(handler);
        OriginalHistogramButton.addActionListener(handler);
        ModifiedHistogramButton.addActionListener(handler);

    }

    private class ButtonHandler implements ActionListener {

        //events
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == OriginalImageButton) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int res = fileChooser.showOpenDialog(fileChooser);
                if (res == JFileChooser.CANCEL_OPTION) {
                    System.exit(1);
                }
                File filename = fileChooser.getSelectedFile();
                name = filename.getAbsolutePath();
                try {
                    original = ImageIO.read(new File(name));
                } catch (IOException ex) {
                    Logger.getLogger(Histeq.class.getName()).log(Level.SEVERE, null, ex);
                }
                equalized = histogramEqualization(original);
                try {
                    writeImage("Equalized_Image");
                } catch (IOException ex) {
                    Logger.getLogger(Histeq.class.getName()).log(Level.SEVERE, null, ex);
                }

                ImageIcon icon = new ImageIcon(original);
                OriginalImagelabel = new JLabel(icon);
                OriginalImagelabel.setIcon(icon);
                JFrame frame = new JFrame("Original Image");
                frame.add(OriginalImagelabel);
                frame.setVisible(rootPaneCheckingEnabled);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(600, 600);

            } else if (e.getSource() == ModifiedImageButton) {

                ImageIcon icon = new ImageIcon(equalized);
                ModifiedImageLabel = new JLabel(icon);
                ModifiedImageLabel.setIcon(icon);
                JFrame frame = new JFrame("Modified Image");
                frame.add(ModifiedImageLabel);
                frame.setVisible(rootPaneCheckingEnabled);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(600, 600);
            } else if (e.getSource() == OriginalHistogramButton) {

                //method for barchart
                String output = "Colors\tValues\tFreequency";
                int[] OriginalHistogram = new int[getHistogram(original).size()];

                for (int i = 0; i < getHistogram(original).size(); i++) {
                    if (getHistogram(original).get(i) != null) {
                        OriginalHistogram = getHistogram(original).get(i);

                    }

                    for (int counter = 0; counter < OriginalHistogram.length; counter++) {
                        output += "\n" + i + "\t" + OriginalHistogram[counter] + "\t";

                        for (int stars = 0; stars < OriginalHistogram[counter]; stars++) {
                            output += "*";
                        }
                    }

                }

                //Method for displaying data
                JTextArea textarea = new JTextArea(300, 300);
                textarea.setText(output);
                JFrame frame = new JFrame("Original Image's Histogram");
                frame.add(new JScrollPane(textarea));
                frame.setSize(400, 450);
                frame.setVisible(rootPaneCheckingEnabled);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            } else if (e.getSource() == ModifiedHistogramButton) {
                String output = "Colors\tValues\tFreequency";
                int[] ModifiedHistogram = new int[histogramEqualizationTable(equalized).size()];

                for (int i = 0; i < histogramEqualizationTable(equalized).size(); i++) {
                    if (histogramEqualizationTable(equalized).get(i) != null) {
                        ModifiedHistogram = histogramEqualizationTable(equalized).get(i);

                    }

                    for (int counter = 0; counter < ModifiedHistogram.length; counter++) {
                        output += "\n" + i + "\t" + ModifiedHistogram[counter] + "\t";

                        for (int stars = 0; stars < ModifiedHistogram[counter]; stars++) {
                            output += "*";
                        }
                    }

                }

                JTextArea textarea = new JTextArea(300, 300);
                textarea.setText(output);
                JFrame frame = new JFrame("Modified Image's Histogram");
                frame.add(new JScrollPane(textarea));
                frame.setSize(400, 450);
                frame.setVisible(rootPaneCheckingEnabled);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }

        }
    }

    public static void main(String[] args) throws IOException {

        Histeq histeq = new Histeq();
        histeq.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        histeq.setVisible(true);
        histeq.setResizable(false);
        histeq.setSize(400, 500);

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

        //calculate and save the new equalization table
        ArrayList<int[]> histTable = histogramEqualizationTable(initial);

        BufferedImage histogramEQ = new BufferedImage(initial.getWidth(), initial.getHeight(), initial.getType());

        for (int i = 0; i < initial.getWidth(); i++) {
            for (int j = 0; j < initial.getHeight(); j++) {

                // Get pixels by R, G, B and alpha
                alpha = new Color(initial.getRGB(i, j)).getAlpha();
                red = new Color(initial.getRGB(i, j)).getRed();
                green = new Color(initial.getRGB(i, j)).getGreen();
                blue = new Color(initial.getRGB(i, j)).getBlue();

                // Set new pixel values using the histogram table
                red = histTable.get(0)[red];
                green = histTable.get(1)[green];
                blue = histTable.get(2)[blue];

                Pixel = colorToRGB(alpha, red, green, blue);

                //make the image
                histogramEQ.setRGB(i, j, Pixel);

            }
        }

        return histogramEQ;

    }

    // convert the new equalization table
    private static ArrayList<int[]> histogramEqualizationTable(BufferedImage initial) {

        // Get the initial histogram
        ArrayList<int[]> imageHist = getHistogram(initial);

        ArrayList<int[]> imageTable = new ArrayList<int[]>();

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

        // Calculate the scale 
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

    // Return an ArrayList containing histogram values for separate R, G, B channels of the initial/original image
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

                // Increase the values of colors
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

    // Convert R, G, B, Alpha to 8 bit
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
