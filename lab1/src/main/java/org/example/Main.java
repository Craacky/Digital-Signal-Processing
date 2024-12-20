package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    // Windows path
//    public static String resourcesPath = "D:\\Projects\\Digital-Signal-Processing\\lab1\\src\\main\\resources\\";
//Linux path
    public static String resourcesPath = "/home/craacky/Projects/Digital-Signal-Processing/lab1/src/main/resources/";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter name of you file");
        System.out.println(listFiles(resourcesPath));
        System.out.print("Choose one: ");
        String filePath = resourcesPath + scanner.next();

        if (filePath.substring(filePath.lastIndexOf(".") + 1).equals("bmp")) {
            BufferedImage image = ImageIO.read(new File(filePath));

            JFrame frame = new JFrame("COS lab_1");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(image.getWidth(), image.getHeight());

            JLabel label = new JLabel(new ImageIcon(image));
            frame.add(label);

            JScrollPane scrollPane = new JScrollPane(label);
            frame.add(scrollPane);

            frame.setVisible(true);
            bitmapMetadata(filePath);
        } else {
            System.out.println("Wrong file type. Image viewer support only BMP! BYE BYE !");
            System.exit(0);
        }
    }

    public static Set<String> listFiles(String dir) throws IOException {
        Set<String> fileSet = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    fileSet.add(path.getFileName().toString());
                }
            }
        }
        return fileSet;
    }

    public static void bitmapMetadata(String filePath) throws IOException {
        File file = new File(filePath);
        BufferedImage image = ImageIO.read(file);
        int compression = image.getType();

        System.out.println("File type: bitmap(BMP)");
        System.out.println("Value of file: " + ((double) file.length() / (1024L * 1024L)) + " MB");
        System.out.println("Width of file: " + image.getWidth() + " pixels");
        System.out.println("Height of file: " + image.getHeight() + " pixels");
        System.out.println("BitsPerPixel: " + image.getColorModel().getPixelSize() + " bits");

        if (compression == BufferedImage.TYPE_BYTE_BINARY || compression == BufferedImage.TYPE_BYTE_INDEXED) {
            System.out.println("Compression status: true");
        } else {
            System.out.println("Compression status: false");
        }

        System.out.println("Total value of colors: " + colorsCounter(image));
        determineColorPallet(image);
    }

    public static void determineColorPallet(BufferedImage image) {
        ColorModel colorModel = image.getColorModel();
        int colorSpaceType = colorModel.getColorSpace().getType();

        if (colorSpaceType == ColorSpace.TYPE_RGB) {
            System.out.println("Color pallet: RGB");
        } else if (colorSpaceType == ColorSpace.TYPE_GRAY) {
            System.out.println("Color pallet: Greyscale");
        } else if (colorSpaceType == ColorSpace.TYPE_CMYK) {
            System.out.println("Color pallet: CMYK");
        } else {
            System.out.println("Color pallet: Unknown");
        }
    }

    public static int colorsCounter(BufferedImage image) {
        HashSet<Integer> colors = new HashSet<>();
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                int pixel = image.getRGB(j, i);
                colors.add(pixel);
            }
        }
        return colors.size();
    }


}