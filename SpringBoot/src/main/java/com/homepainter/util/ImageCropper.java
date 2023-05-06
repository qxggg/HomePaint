package com.homepainter.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.util.Date;


public class ImageCropper {

    private static String dir = "PictureData/";

    public static String crop(String filename,double width,double height,String newFilename) throws IOException {
        String filepath = "";
        String os = System.getProperty("os.name").toLowerCase();
        if(os.indexOf("linux")!=-1){
            filepath = FileUtils.Read_FOLDER_linux;
        }else{
            filepath = FileUtils.Read_FOLDER_win;
        }
        filepath += dir;

        File input = new File(filepath+filename);
        BufferedImage image = ImageIO.read(input);
        Graphics2D g2d = image.createGraphics();
        int dpi = (int) g2d.getDeviceConfiguration().getNormalizingTransform().getScaleX();

        BufferedImage croppedImage = cropImage(image, width, height,dpi);

        File output = new File(filepath+newFilename);
        ImageIO.write(croppedImage, "png", output);
        return newFilename;
    }

    public static BufferedImage cropImage(BufferedImage src, double width, double height,int dpi) {

        int ix = (int) Math.round(0 *dpi /2.54);
        int iy = (int) Math.round(0 *dpi /2.54);
        int iw = (int) Math.round(width*dpi /2.54);
        int ih = (int) Math.round(height*dpi /2.54);

        BufferedImage dest = new BufferedImage(iw, ih, src.getType());
        Graphics2D g = dest.createGraphics();

        g.drawImage(src, 0, 0, iw, ih, ix, iy, ix + iw, iy + ih, null);
        g.dispose();
        return dest;
    }


    public static String cropImageToPolygon(int[] xPoints, int[] yPoints, String filename) throws IOException {
        // 读取文件
        String filepath = "";
        String os = System.getProperty("os.name").toLowerCase();
        if(os.indexOf("linux")!=-1){
            filepath = FileUtils.Read_FOLDER_linux;
        }else{
            filepath = FileUtils.Read_FOLDER_win;
        }
        filepath += dir;

        File input = new File(filepath+filename);
        BufferedImage inputImage = ImageIO.read(input);

        // 创建一个多边形对象
        Polygon polygon = new Polygon(xPoints, yPoints, xPoints.length);

        // 获取多边形的边界矩形
        Rectangle bounds = polygon.getBounds();

        // 创建一个新的BufferedImage对象，用于存储裁剪后的图像
        BufferedImage outputImage = new BufferedImage(bounds.width, bounds.height, inputImage.getType());

        // 创建一个Graphics2D对象，用于绘制裁剪后的图像
        Graphics2D g2d = outputImage.createGraphics();

        // 将Graphics2D对象的坐标系平移，使多边形的左上角位于(0, 0)
        g2d.translate(-bounds.x, -bounds.y);

        // 绘制多边形
        g2d.clip(polygon);
        g2d.drawImage(inputImage, 0, 0, null);

        // 释放Graphics2D对象
        g2d.dispose();

        // 保存图片
        String outputImagePath = filename.substring(0,filename.length()-4) + "_" + new Date().getTime() +".png";

        // ImageIO.write(outputImage, "png", new File(dir+outputImagePath));
        File output = new File(filepath+outputImagePath);
        ImageIO.write(outputImage, "png", output);


        return outputImagePath;
    }

}
