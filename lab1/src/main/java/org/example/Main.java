package org.example;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static String resourcesPath = "/home/craacky/Projects/Digital-Signal-Processing/lab1/src/main/resources/";

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

        FileInputStream fis = new FileInputStream(file);
        BufferedImage image = ImageIO.read(file);

        byte[] header = new byte[54];
        fis.read(header);

        int colorsCount = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                if (rgb != 0) colorsCount++;
            }

        }

        double fileSize = byteArrayToInt(header, 2);
        int width = byteArrayToInt(header, 18);
        int height = byteArrayToInt(header, 22);
        int bitsPerPixel = byteArrayToInt(header, 28);
        int compression = image.getType();

        System.out.println("File type: bitmap(BMP)");
        System.out.println("Value of file: " + fileSize / (1024L * 1024L) + " MB");
        System.out.println("Width of file: " + width + " pixels");
        System.out.println("Height of file: " + height + " pixels");
        System.out.println("BitsPerPixel: " + bitsPerPixel + " bits");
        if (compression == BufferedImage.TYPE_BYTE_BINARY || compression == BufferedImage.TYPE_BYTE_INDEXED) {
            System.out.println("Compression status: true");
        } else {
            System.out.println("Compression status: false");
        }
        System.out.println("Total value of colors: " + colorsCount);

        int colorModel = image.getColorModel().getColorSpace().getType();
        System.out.println("Color model:" + (colorModel == 5 ? " Indexed" : colorModel == 6 ? " Direct" : " Unknown"));


    }

    public static int byteArrayToInt(byte[] bytes, int offset) {
        return (bytes[offset + 3] & 0xFF) << 24 |
                (bytes[offset + 2] & 0xFF) << 16 |
                (bytes[offset + 1] & 0xFF) << 8 |
                bytes[offset] & 0xFF;
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter name of you file");
        System.out.println(listFiles(resourcesPath));
        System.out.print("Choose one: ");
        String filePath = resourcesPath + scanner.next();


        if (filePath.substring(filePath.lastIndexOf(".") + 1).equals("bmp")) {
            BufferedImage image = ImageIO.read(new File(filePath));

            JFrame frame = new JFrame("COS lab_1 v0.1");
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

}