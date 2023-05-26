package com.homepainter.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ZipFloors {

    public static void compressImage(String inputPath, String outputPath, int width, int height, String outputFormat) throws IOException {
        // 从指定路径读取图像
        BufferedImage image = ImageIO.read(new File(inputPath));

        // 创建一个新的缩放后的图像
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();

        // 将缩放后的图像写入文件
        ImageIO.write(resizedImage, outputFormat, new File(outputPath));
    }
//    public static File downloadAndCompressImage(String imageUrl, int targetWidth, int targetHeight, double quality) throws IOException {
//        // 打开 HTTP 连接
//        URL url = new URL(imageUrl);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//
//        // 读取图片
//        BufferedImage inputImage = ImageIO.read(connection.getInputStream());
//
//        // 计算压缩比例
//        double widthRatio = (double) targetWidth / inputImage.getWidth();
//        double heightRatio = (double) targetHeight / inputImage.getHeight();
//        double ratio = Math.min(widthRatio, heightRatio);
//
//        // 计算目标图片尺寸
//        int newWidth = (int) (inputImage.getWidth() * ratio);
//        int newHeight = (int) (inputImage.getHeight() * ratio);
//
//        // 创建缩放后的图片
//        BufferedImage outputImage = new BufferedImage(newWidth, newHeight, inputImage.getType());
//
//        // 缩放原始图片到目标尺寸
//        outputImage.createGraphics().drawImage(inputImage.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
//
//        // 创建压缩后的图片文件
//        File outputFile = new File("output2.jpg");
//
//
//        // 写入压缩后的图片
//        ImageIO.write(outputImage, "jpg", outputFile);
//
//        return outputFile;
//    }

    public static void main(String[] args) throws IOException {
//        downloadAndCompressImage("https://image-1304455659.cos.ap-nanjing.myqcloud.com/DiBan/299.jpg", 2001, 4029, 80);

    }
}
