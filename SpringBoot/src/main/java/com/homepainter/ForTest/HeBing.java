package com.homepainter.ForTest;

import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.*;

import static com.homepainter.util.ReadCsv.readCsv;
import static com.homepainter.util.SortUtil.sortByFileName;

public class HeBing {

    public static void hebingcsv(String category,int cnt) throws IOException {
        String csvPath = "G:\\csv\\" +category +"\\";

        // 获取文件下的子文件夹名称，modelId
        String []res = Get_Zi_Dir("G:\\家具类别划分\\L形沙发");

        for(int i=0;i<cnt;i++){
            // 获取商品信息
            Map<String,Object> goodsData = HuiZong_GoodsData_ByCSV(csvPath+i);

            // 获取modelId

        }

    }

    public static Map<String,Object> HuiZong_GoodsData_ByCSV(String filepath) throws IOException {
        // 读csv
        List<String[]> csv = readCsv(filepath);
        // 家具信息
        Map<String,Object> goodsData = new HashMap<>();
        List<Map<String,Object>> vertexs = new ArrayList<>();
        // 前8行是顶点信息
        for(int j=0;j<8;j++){
            Map<String,Object> temp = new HashMap<>();
            temp.put("x",Double.parseDouble( csv.get(j)[0]));
            temp.put("y",Double.parseDouble( csv.get(j)[1]));
            temp.put("z",Double.parseDouble( csv.get(j)[2]));
            vertexs.add(temp);
        }
        goodsData.put("vertexs",vertexs);

        // BoxColliderCenter
        Map<String,Object> BoxColliderCenter = new HashMap<>();
        BoxColliderCenter.put("x",Double.parseDouble( csv.get(8)[0]));
        BoxColliderCenter.put("y",Double.parseDouble( csv.get(8)[1]));
        BoxColliderCenter.put("z",Double.parseDouble( csv.get(8)[2]));
        goodsData.put("BoxColliderCenter",BoxColliderCenter);

        // size
        Map<String,Object> size = new HashMap<>();
        size.put("x",Double.parseDouble( csv.get(8)[0]));
        size.put("y",Double.parseDouble( csv.get(8)[1]));
        size.put("z",Double.parseDouble( csv.get(8)[2]));
        goodsData.put("size",size);

        // Center
        Map<String,Object> Center = new HashMap<>();
        Center.put("x",Double.parseDouble( csv.get(8)[0]));
        Center.put("y",Double.parseDouble( csv.get(8)[1]));
        Center.put("z",Double.parseDouble( csv.get(8)[2]));
        goodsData.put("Center",Center);
        return goodsData;
    }

    /**
     * 获取文件夹下的子文件名称
     * @param dirPath
     */
    public static String[] Get_Zi_Dir(String dirPath) {
        File directory = new File(dirPath);
        String[] subDirectories = directory.list((current, name) -> new File(current, name).isDirectory());
        Collator collator = Collator.getInstance(Locale.getDefault()); // 获取当前区域设置的Collator实例

        return subDirectories;
    }

    public static void main(String[] args) {

        String []res = Get_Zi_Dir("G:\\家具类别划分\\L形沙发");
        sortByFileName(Arrays.asList(res));
        for(int i=0;i<res.length;i++)
            System.out.println(res[i]);
        System.out.println();
    }
}
