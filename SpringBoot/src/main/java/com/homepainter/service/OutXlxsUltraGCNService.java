package com.homepainter.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.homepainter.util.FileUtils;
import com.homepainter.util.RedisUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




@Component
public class OutXlxsUltraGCNService {

    @Autowired
    public RedisUtil redisUtil;


    public  void writeStyleXlxs() throws IOException {
        System.out.println("开始覆盖data.xlsx "+new Date().toString());
        String filepath = "";
        String os = System.getProperty("os.name").toLowerCase();
        if(os.indexOf("linux")!=-1){
            filepath = FileUtils.Read_FOLDER_linux;
        }else{
            filepath = FileUtils.Read_FOLDER_win;
        }
        filepath += "UltraGCN/data/my/";

        String[] menu = {"randomConsume","randomSearchClick","randomComment","randomUse","randomClick","randomView","randomCollect"};
        // 读取Excel文件
        FileInputStream inputStream = new FileInputStream(filepath+"data.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        // 查看redis的所有Key
        Set<String> Allkey = redisUtil.getAllKeys();
        // 查看redis的所有Style数据
        for(String key : Allkey){
            if(key.contains("styleBehave")){
                int userid = Integer.parseInt( key.substring(11,key.length()) );
                Map<String, Object> map = (Map<String, Object>) redisUtil.get(key);

                // 遍历menu
                for(int j=0;j<menu.length;j++){
                    if(map.get(menu[j])!=null){
                        // 去掉表头
                        XSSFRow row = sheet.getRow(userid+1);
                        XSSFCell cell = row.getCell(j);
                        cell.setCellValue(map.get(menu[j]).toString());
                        // System.out.println("i:"+userid+2+"J:"+j+"new_data:"+map.get(menu[j]).toString());
                    }
                }
            }

        }

        // 保存文件
        FileOutputStream outputStream = new FileOutputStream(filepath+"new_data.xlsx");
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();

        System.out.println("覆盖data.xlsx执行完毕 "+new Date().toString());
    }



    public  void writeFurnitureXlxs() throws IOException {
        System.out.println("开始覆盖json.xlsx "+new Date().toString());
        String filepath = "";
        String os = System.getProperty("os.name").toLowerCase();
        if(os.indexOf("linux")!=-1){
            filepath = FileUtils.Read_FOLDER_linux;
        }else{
            filepath = FileUtils.Read_FOLDER_win;
        }
        filepath += "UltraGCN/data/my/";

        String[] menu = {"randomConsume","randomComment","randomUse","randomClick","randomView","randomCollect"};
        // 读取Excel文件
        FileInputStream inputStream = new FileInputStream(filepath+"json.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        // 查看redis的所有Key
        Set<String> Allkey = redisUtil.getAllKeys();
        // 查看redis的所有Style数据
        for(String key : Allkey){
            if(key.contains("goodsBehave")){
                int userid = Integer.parseInt( key.substring(11,key.length()) );
                // xlxs的第2行对应 userid:0
                Map<String, Object> map = (Map<String, Object>) redisUtil.get(key);

                // 遍历menu
                for(int j=0;j<menu.length;j++){
                    if(map.get(menu[j])!=null){
                        // 去掉表头
                        XSSFRow row = sheet.getRow(userid+1);
                        XSSFCell cell = row.getCell(j);
                        cell.setCellValue(map.get(menu[j]).toString());
                        // System.out.println("i:"+userid+2+"J:"+j+"new_data:"+map.get(menu[j]).toString());
                    }
                }
            }

        }

        // 保存文件
        FileOutputStream outputStream = new FileOutputStream(filepath+"new_json.xlsx");
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();

        System.out.println("覆盖json.xlsx执行完毕 "+new Date().toString());
    }

}



