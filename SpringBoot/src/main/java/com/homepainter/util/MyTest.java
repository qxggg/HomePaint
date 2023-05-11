package com.homepainter.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class MyTest {

    public static void main(String args[]) throws IOException {
        String path = "G:\\3D-FUTURE-model-part1\\";

        String json = readJson("goods.json");

        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("RECORDS");

        String targetPath = "G:\\target\\";

        for(int i=0;i<jsonArray.size();i++){
            String filepath = String.valueOf (i/100);
            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
            String name = (String) jsonObject1.get("modalId");
            System.out.println(i);
            moveFolder(Paths.get(path+name),Paths.get(targetPath+filepath+"\\"+name));
        }
//        for(int i=0;i<jsonArray.size();i++){
//            String filepath = String.valueOf (i/100);
//            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
//            String name = (String) jsonObject1.get("modalId");
//
//            if(Files.exists(Paths.get(path+filepath+"\\"+name))){
//                System.out.println(name);
//                moveFolder(Paths.get(path+filepath+"\\"+name),Paths.get(path+name));
//            }
//        }
    }

    /**
     * 将文件夹及其子文件夹和文件移动到目标位置
     * @param sourceFolder 源文件夹
     * @param targetFolder 目标文件夹
     * @throws IOException 如果移动失败，抛出IOException异常
     */
    public static void moveFolder(Path sourceFolder, Path targetFolder) throws IOException {
        // 如果目标文件夹不存在，创建目标文件夹
        if (!Files.exists(targetFolder)) {
            Files.createDirectories(targetFolder);
        }

        // 获取源文件夹下的所有文件和子文件夹
        File[] sourceFiles = sourceFolder.toFile().listFiles();
        if (sourceFiles != null) {
            // 遍历源文件夹下的所有文件和子文件夹
            for (File sourceFile : sourceFiles) {
                if (sourceFile.isDirectory()) {
                    // 如果是子文件夹，递归调用移动子文件夹方法
                    Path subTargetPath = targetFolder.resolve(sourceFile.getName());
                    moveFolder(sourceFile.toPath(), subTargetPath);
                } else {
                    // 如果是文件，移动到目标文件夹下
                    Path targetFilePath = targetFolder.resolve(sourceFile.getName());
                    Files.move(sourceFile.toPath(), targetFilePath);
                }
            }
        }

        // 删除源文件夹
        Files.delete(sourceFolder);
    }

    public static String readJson(String filename){
        String res = "";
        String filepath = ".\\"+ filename;

        File file = new File(filepath);
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line ;
            while ((line = br.readLine()) != null) {
                res += line;
            }
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }
}
