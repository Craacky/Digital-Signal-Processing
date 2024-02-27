package org.example

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.math.min


fun main() {
    try {
        val image: BufferedImage = ImageIO.read(File("1.jpg"))

        addNoise(image)
        ImageIO.write(image, "jpg", File("output.jpg"))

        thresholdFilter(image, 210)
        ImageIO.write(image, "jpg", File("result.jpg"))

    } catch (e: IOException) {
        println("Error: " + e.message)
    }
}

private fun addNoise(image: BufferedImage) {
    val width: Int = image.width
    val height: Int = image.height

    for (y in 0..<height) {
        for (x in 0..<width) {
            val color = Color(image.getRGB(x, y))
            val randomValue: Int = (Math.random() * 256).toInt()
            val red: Int = min(255, color.red + randomValue)
            val green: Int = min(255, color.green + randomValue)
            val blue: Int = min(255, color.blue + randomValue)
            val newColor = Color(red, green, blue)
            image.setRGB(x, y, newColor.rgb)
        }
    }
}

private fun thresholdFilter(image: BufferedImage, threshold: Int) {
    val width: Int = image.width
    val height: Int = image.height

    for (y in 0..<height) {
        for (x in 0..<width) {
            val color = Color(image.getRGB(x, y))
            val brightness: Int =
                (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue).toInt()
            if (brightness < threshold) {
                image.setRGB(x, y, Color.BLACK.rgb)
            } else {
                image.setRGB(x, y, Color.WHITE.rgb)
            }
        }
    }
}
