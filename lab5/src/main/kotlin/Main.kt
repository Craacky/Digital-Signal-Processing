package org.example
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.imageio.ImageIO

// Функция для сжатия изображения с использованием LZW-сжатия
fun compressImage(imageFile: File, compressedFile: File) {
    // Чтение исходного изображения
    val image = ImageIO.read(imageFile)
    val width = image.width
    val height = image.height

    // Создание буфера для сжатого изображения
    val compressedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    // Создание потока для записи сжатого файла
    val outputStream = FileOutputStream(compressedFile)
    val dataOutputStream = outputStream.buffered()

    // Получение массива пикселей из исходного изображения
    val pixels = IntArray(width * height)
    image.getRGB(0, 0, width, height, pixels, 0, width)

    // Сжатие пикселей
    val compressedPixels = compressPixels(pixels)

    // Запись сжатых пикселей в файл
    for (pixel in compressedPixels) {
        dataOutputStream.write(pixel shr 8)
        dataOutputStream.write(pixel)
    }

    // Закрытие потока и сохранение сжатого изображения
    dataOutputStream.close()
    ImageIO.write(compressedImage, "PNG", compressedFile)
}

// Функция для разжатия изображения, сжатого с использованием LZW-сжатия
fun decompressImage(compressedFile: File, decompressedFile: File) {
    // Чтение сжатого изображения
    val compressedImage = ImageIO.read(compressedFile)
    val width = compressedImage.width
    val height = compressedImage.height

    // Создание буфера для разжатого изображения
    val decompressedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    // Создание потока для чтения сжатого файла
    val inputStream = FileInputStream(compressedFile)
    val dataInputStream = inputStream.buffered()

    // Получение сжатых пикселей из файла
    val compressedPixels = mutableListOf<Int>()

    while (dataInputStream.available() > 0) {
        val byte1 = dataInputStream.read()
        val byte2 = dataInputStream.read()
        val pixel = (byte1 shl 8) or byte2
        compressedPixels.add(pixel)
    }

    // Разжатие пикселей
    val decompressedPixels = decompressPixels(compressedPixels.toIntArray())

    // Запись разжатых пикселей в буфер изображения
    decompressedImage.setRGB(0, 0, width, height, decompressedPixels, 0, width)

    // Закрытие потока и сохранение разжатого изображения
    dataInputStream.close()
    ImageIO.write(decompressedImage, "PNG", decompressedFile)
}

// Функция для сжатия пикселей с использованием LZW-сжатия
fun compressPixels(pixels: IntArray): MutableList<Int> {
    val compressedPixels = mutableListOf<Int>()
    val dictionary = mutableMapOf<String, Int>()

    // Инициализация словаря со значениями от 0 до 255
    for (i in 0..255) {
        dictionary[i.toChar().toString()] = i
    }

    var nextCode = 256
    var currentString = ""

    // Сжатие пикселей
    for (pixel in pixels) {
        val pixelString = pixel.toChar().toString()
        val combinedString = currentString + pixelString

        if (dictionary.contains(combinedString)) {
            currentString = combinedString
        } else {
            compressedPixels.add(dictionary[currentString]!!)
            dictionary[combinedString] = nextCode
            nextCode++
            currentString = pixelString
        }
    }

    compressedPixels.add(dictionary[currentString]!!)

    return compressedPixels
}

// Функция для разжатия пикселей, сжатых с использованием LZW-сжатия
fun decompressPixels(compressedPixels: IntArray): IntArray {
    val decompressedPixels = mutableListOf<Int>()
    val dictionary = mutableMapOf<Int, String>()

    // Инициализация словаря со значениями от 0 до 255
    for (i in 0..255) {
        dictionary[i] = i.toChar().toString()
    }

    var nextCode = 256
    var currentCode = compressedPixels[0].toChar().toString()
    var previousCode = currentCode

    decompressedPixels.add(currentCode.toInt())

    // Разжатие пикселей
    for (i in 1 until compressedPixels.size) {
        currentCode = if (dictionary.containsKey(compressedPixels[i])) {
            dictionary[compressedPixels[i]]!!
        } else {
            previousCode + previousCode[0]
        }

        decompressedPixels.add(currentCode.toInt())

        dictionary[nextCode] = previousCode + currentCode[0]
        nextCode++
        previousCode = currentCode
    }

    return decompressedPixels.toIntArray()
}

fun main() {
    val originalImageFile = File("input.png")
    val compressedImageFile = File("compressed.bin")
    val decompressedImageFile = File("decompressed.png")

    // Сжатие изображения
    compressImage(originalImageFile, compressedImageFile)

    // Разжатие изображения
    decompressImage(compressedImageFile, decompressedImageFile)
}
