package com.homepainter.util;

import java.io.File;

public class DirectoryDeleter {
    /**
     * 删除文件夹接口,会删除目录下的文件
     * @param dir 文件夹目录
     */
    public static void deleteDirectory(String dir) {
        String filepath = "";
        String os = System.getProperty("os.name").toLowerCase();
        if(os.indexOf("linux")!=-1){
            filepath = FileUtils.Read_FOLDER_linux;
        }else{
            filepath = FileUtils.Read_FOLDER_win;
        }
        filepath += dir;

        File directory = new File(filepath);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(String.valueOf(file));
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
            System.out.println("目录 '" + directory.getAbsolutePath() + "' 已成功删除。");
        } else {
            System.out.println("目录 '" + directory.getAbsolutePath() + "' 不存在。");
        }
    }


}
