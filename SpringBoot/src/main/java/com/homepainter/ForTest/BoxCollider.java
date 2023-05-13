package com.homepainter.ForTest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static com.homepainter.util.ReadJson.readJson;

public class BoxCollider {

    public static void main(String []args){
        GetCategory("L形沙发         ");
    }

    public static void GetCategory(String category){
        List<JSONObject> data = new ArrayList<>();
        String json = readJson("goods.json","");
        JSONObject Alljson = JSON.parseObject(json);
        JSONArray jsonArray =  JSON.parseArray(Alljson.getString("RECORDS"));

        String targetPath = "G:\\家具类别划分\\"+category+"\\";
        String filepath  = "G:\\3D-FUTURE-model-part1\\";

        int cnt = 0 ;
        for(int i=0;i< jsonArray.size();i++){

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(jsonObject.get("category").equals(category)){
                cnt++;
                System.out.println(cnt);
                moveDir(filepath+jsonObject.get("modalId"),targetPath+jsonObject.get("modalId"));
            }
        }
    }

    public static void removeBack(){
        String targetPath = "G:\\3D-FUTURE-model-part1";
        String filepath = "G:\\家具备份\\";
        for(int i=0;i<20;i++){
            System.out.println(i);
            moveDir(filepath+i,targetPath);
        }
    }

    public static void moveDir(String 源文件夹路径,String 目标文件夹路径) {



        // 创建源文件夹对象和目标文件夹对象
        File 源文件夹 = new File(源文件夹路径);
        File 目标文件夹 = new File(目标文件夹路径);

        // 复制文件夹
        try {
            Files.walk(源文件夹.toPath())
                    .filter(Files::isRegularFile)
                    .forEach(source -> {
                        Path destination = 目标文件夹.toPath().resolve(源文件夹.toPath().relativize(source));
                        try {
                            Files.createDirectories(destination.getParent());
                            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            System.out.println("文件夹复制成功！");
        } catch (IOException e) {
            System.out.println("文件夹复制失败！");
            e.printStackTrace();
        }

    }
}
