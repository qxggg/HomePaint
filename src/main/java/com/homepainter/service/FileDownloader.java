package com.homepainter.service;

import com.qcloud.cos.model.PutObjectResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.homepainter.service.Upload_File.putObject;
import static com.homepainter.util.ZipUtil.unzip;

public class FileDownloader {

    public static void get_zip(String fp_id, String format, int telephone) throws IOException {
        long projectName = System.currentTimeMillis();
        PictureBuilder pictureBuilderController = new PictureBuilder();
        String fileUrl = "https://www.gongchuangshijie.com:86/api/FileDownload/FileDownloadObj?fp_id=" + fp_id + "&format=" + format;
        String fullname = "download";
        String fullname2 = "download";

        String os = System.getProperty("os.name").toLowerCase();

        if (os.indexOf("linux") != -1) fullname = "/www/wwwroot/" + fullname;

        String savePath = fullname + "/" + telephone + "/" + projectName + "/" + projectName + ".zip" ;
        Path path = Paths.get(fullname + "\\"  + telephone + "\\" + projectName);
        Path pathCreate = Files.createDirectories(path);


        try {
            URL url = new URL(fileUrl);

            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestProperty("token", pictureBuilderController.getToken());
            httpConn.setRequestProperty("requestType-Content","ModelingStand");
            int responseCode = httpConn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpConn.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(savePath);

                int bytesRead = -1;
                byte[] buffer = new byte[4096];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                System.out.println("文件已保存到 " + savePath);
            } else {
                System.out.println("无法下载文件，响应代码为 " + responseCode);
            }

            httpConn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        String zip = fullname + "/" + telephone + "/" + projectName + "/";
        String tmp = "download" + "/" + telephone + "/" + projectName + "/";
        unzip(savePath, zip);


        String filename = "model.obj";
        File file = new File(zip + filename);
        PutObjectResult putObjectResult = putObject(filename, file,"modeling-result/" + tmp);

        filename = "model_m2.jpg";
        file = new File(zip + filename);
        putObjectResult = putObject(filename, file,"modeling-result/" + tmp);

        filename = "model.mtl";
        file = new File(zip + filename);
        putObjectResult = putObject(filename,file,"modeling-result/" + tmp);
    }

}

