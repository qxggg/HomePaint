package com.homepainter.util;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class WriteJson {

    /**
        写入JSON，传入Object和filename
        根据系统写入对应文件夹
        filename 带.json后缀
        filedirectory只写/home/ubuntu/后面的，最后 不需要 /
     */
    public static void writejson(Object data,String filename,String filedirectory) throws IOException {
        //写JSON
        String filepath = "";
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("windows") != -1) {;
            filepath = FileUtils.Write_FOLDER_win;
        }else if(os.indexOf("linux") != -1){
            filepath = FileUtils.Write_FOLDER_linux;
        }
        filepath += filedirectory;
        // System.out.println(filepath);
        // 要写的内容
        byte[] bytes = JSON.toJSON(data).toString().getBytes();
        //要存入本地的地址放到path里面
        Path path = Paths.get( filepath + "\\");
        //如果没有files文件夹，则创建
        if (!Files.isWritable(path)) {
            Files.createDirectories(path);
        }


        FileUtils.getFileByBytes(bytes, filepath, filename );
    }
}
