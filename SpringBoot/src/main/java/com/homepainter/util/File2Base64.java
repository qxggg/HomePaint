package com.homepainter.util;

import org.springframework.web.multipart.MultipartFile;



import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;


public class File2Base64 {
    /**
     *
     * @return String
     * @description 将文件转base64字符串
     * @date 2018年3月20日
     * @author changyl
     * File转成编码成BASE64
     */
    public static String fileToBase64(MultipartFile file ) {
        String base64 = null;
        InputStream in = null;
        try {
            in = file.getInputStream();
            byte[] bytes=new byte[(int)file.getSize()];
            in.read(bytes);
            base64 = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return base64;
    }

    public static File Base64ToFile(String base64) throws IOException {
        if(base64.contains("data:image")){
            base64 = base64.substring(base64.indexOf(",")+1);
        }
        base64 = base64.toString().replace("\r\n", "");
        //创建文件目录
        String prefix=".jpg";
        File file = File.createTempFile("123", prefix);
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64);

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(decodedBytes);
        }catch (Exception e){
            System.out.println("保存文件时出错：" + e.getMessage());
        }
        return file;
    }

    /**
     * 从网络url中获取图片转为base64
     *
     * @param httpUrl
     * @return
     */
    public static String GET_Image2Base64(String httpUrl) {
        InputStream in = null;
        byte[] data = null;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(5000);
            in = connection.getInputStream();
            data = readInputStream(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (data == null) {
            return "";
        }
        String str = Base64.getEncoder().encodeToString(data);
        return str;
    }
    /**
     * 从输入流中获取字节数组
     *
     * @return
     * @throws IOException
     */
    public static void GETFile_Image2Base64(String httpUrl, String fileName) {
        InputStream in = null;
        byte[] data = null;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(5000);
            in = connection.getInputStream();
            data = readInputStream(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            outputStream.write(data); // 将字节数组写入文件
            System.out.println("字节数组已成功输出到文件 " + fileName);
        } catch (IOException e) {
            System.out.println("发生了 I/O 错误：" + e.getMessage());
        }

    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public void zip(File[] files){

    }



}