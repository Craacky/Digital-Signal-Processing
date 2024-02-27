package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String inputImagePath = "/home/craacky/Projects/Digital-Signal-Processing/lab2/src/main/resources/1.bmp";
        String outputImagePath = "/home/craacky/Projects/Digital-Signal-Processing/lab2/src/main/resource/";
        Scanner sc = new Scanner(System.in);
        System.out.print("Choose scale(1.5; 2; 5.3; 15): ");

        enlargeImage(inputImagePath, outputImagePath, sc.nextDouble());

    }

    public static void enlargeImage(String inputImagePath, String outputImagePath, double scale) {
        try {
            File inputFile = new File(inputImagePath);
            BufferedImage originalImage = ImageIO.read(inputFile);
            int newWidth = (int) (originalImage.getWidth() / scale);
            int newHeight = (int) (originalImage.getHeight() / scale);
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());

            Graphics2D g2d = resizedImage.createGraphics();
            g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g2d.dispose();

            ImageIO.write(resizedImage, "bmp", new File(outputImagePath));
            System.out.println("Image resized successfully.");
            System.out.println("Original size: " + originalImage.getWidth() + "x" + originalImage.getHeight());
            System.out.println("Resized image: " + resizedImage.getWidth() + "x" + resizedImage.getHeight());
        } catch (IOException ex) {
            System.out.println("Error resizing the image: " + ex.getMessage());
        }
    }
}