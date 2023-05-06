package com.homepainter.util;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {



    //这是本地存入的格式，上传到服务器的话，格式类似于，"/root/images/pc/"

    public static String Read_FOLDER_win = ".\\";
    public static String Read_FOLDER_linux = "/www/wwwroot/";

    public static String Write_FOLDER_win = ".\\";
    public static String Write_FOLDER_linux = "/www/wwwroot/";

    /**
        fileneme 带后缀
        dir从/www/wwwroot/开始 最后带 /
     */
    public static boolean isExit(String filename,String dir){
        String filepath = "";
        String os = System.getProperty("os.name").toLowerCase();
        if(os.indexOf("linux") != -1){
            filepath = FileUtils.Write_FOLDER_linux + dir + filename;
        }else{
            filepath = FileUtils.Write_FOLDER_win + dir + filename;
        }
        File file = new File(filepath);
        if (file.exists()) {
            return true;
        } else {
            System.out.println(filepath);
            return false;
        }
    }

    public static boolean isExit(String filename){
        return isExit(filename,"");
    }

    /**
     *          创建目录
     *         dir从/www/wwwroot/开始 最后带 /
     */
    public static void createDir(String dir){
        String folderPath = "";
        String os = System.getProperty("os.name").toLowerCase();
        if(os.indexOf("linux") != -1){
            folderPath = FileUtils.Write_FOLDER_linux + dir ;
        }else{
            folderPath = FileUtils.Write_FOLDER_win + dir ;
        }
        File folder = new File(folderPath);
        if (!folder.exists()) {
            // 如果文件夹不存在，则创建文件夹
            boolean result = folder.mkdirs();
            if (result) {
                System.out.println("文件夹创建成功");
            } else {
                System.out.println("文件夹创建失败");
            }
        } else {
            System.out.println("文件夹已存在");
        }
    }




    /**
     * 将Byte数组转换成文件
     *
     * @param bytes
     * @param filePath
     * @param fileName
     */
    public static void getFileByBytes(byte[] bytes, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;

        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath +'/' + fileName);
            System.out.println(filePath +'/' + fileName);

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}