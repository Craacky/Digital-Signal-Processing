package org.example
import org.opencv.core.*
import org.opencv.core.CvType.CV_8UC1
import org.opencv.imgcodecs.*
import org.opencv.imgproc.*
import java.util.*

fun main() {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    val image = Imgcodecs.imread("input.png", Imgcodecs.IMREAD_GRAYSCALE)

    val sobelX = Mat()
    val sobelY = Mat()
    Imgproc.Sobel(image, sobelX, CV_8UC1, 1, 0)
    Imgproc.Sobel(image, sobelY, CV_8UC1, 0, 1)

    val absSobelX = Mat()
    val absSobelY = Mat()
    Core.convertScaleAbs(sobelX, absSobelX)
    Core.convertScaleAbs(sobelY, absSobelY)

    val gradient = Mat()
    Core.addWeighted(absSobelX, 0.5, absSobelY, 0.5, 0.0, gradient)

    val scanner = Scanner(System.`in`)
    print("Enter a threshold value: ")
    val threshold = scanner.nextInt()

    val thresholded = Mat()
    Imgproc.threshold(gradient, thresholded, threshold.toDouble(), 255.0, Imgproc.THRESH_BINARY)
    Imgcodecs.imwrite("result.png", thresholded)
}
