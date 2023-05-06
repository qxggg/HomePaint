package com.homepainter.util;



import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReadFile {

    /**
     * 打开文件，返回btye数组
     * @param filename  带后缀
     * @param dir  从/www/wwwroot/开始  最后带/
     * @return
     */
    public static void readfile(String filename, String dir, HttpServletResponse response) throws IOException {
        String filepath = "";
        String os = System.getProperty("os.name").toLowerCase();
        if(os.indexOf("linux")!=-1){
            filepath = FileUtils.Read_FOLDER_linux;
        }else{
            filepath = FileUtils.Read_FOLDER_win;
        }
        filepath += dir ;

        Path imagePath = Paths.get(filepath, filename);
        if (!Files.exists(imagePath)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType(Files.probeContentType(imagePath));
        Files.copy(imagePath, response.getOutputStream());
    }


}
