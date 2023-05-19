package com.homepainter.ForTest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.util.ReadJson;
import com.homepainter.util.RedisUtil;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.*;

import static com.homepainter.util.MyTest.readJson;
import static com.homepainter.util.ReadCsv.readCsv;
import static com.homepainter.util.ReadCsv.readCsvFile;

@SpringBootTest
public class LuRuGoods {

    @Autowired
    RedisUtil redis;

    /**
     * 更新JSON表
     */
    @Test
    public void REF_GOOSJSON(){
        String json = ReadJson.readJson("goods.json","");
        JSONObject Alljson = JSON.parseObject(json);
        JSONArray jsonArray =  JSON.parseArray(Alljson.getString("RECORDS"));
        List<Map<String,Object>> temp = new ArrayList<>();
        for(int i=0;i<jsonArray.size();i++){
            temp.add((Map<String, Object>) jsonArray.get(i));
        }

        redis.set("JSON_GOODS",temp);
    }

    /**
     * 建立联想风格索引，再次建立 不需要 删除键值，但是要在风格索引建立好的前提下 运行！
     */
    @Test
    public void JianLi_LianXiang_Style_SuoYin(){
        Map<String, Object> map = StyleMap;

        Set<String> keys = map.keySet();

        for (String key : keys) {
            List<Map<String,Object>> goodsList = (List<Map<String, Object>>) redis.get("STYLE"+key);

            List<String> LianxianStyles = (List<String>) map.get(key);

            // 将每一个风格的拿出来
            for(int i=0;i<LianxianStyles.size();i++){
                List<Map<String ,Object>> item = (List<Map<String, Object>>) redis.get("STYLE"+LianxianStyles.get(i));
                goodsList.addAll(item);
            }
            System.out.println(key);
            System.out.println(goodsList.size());
            redis.set("LianXiangSTYLE"+key,goodsList);
        }
    }


    /**
     * 建立风格索引，如果要重新建立，需要将STYLE*的建全部删掉  redis-cli KEYS "STYLE*" | xargs redis-cli DEL
     */
    @Test
    public void JianLi_Style_SuoYin(){

        // 删除STYLE * 号建
        Set<String> keys = (Set<String>) redis.getAllKeys();
        for(String key :keys){
            if(key.substring(0,5).equals("STYLE")){
               System.out.println(key);
               redis.del(key);
            }
        }

        String json = readJson("goods.json");

        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("RECORDS");

        System.out.println(jsonArray.size());
        for(int i=0;i<jsonArray.size();i++){
            JSONObject goods = jsonArray.getJSONObject(i);

            String style = goods.get("style").toString();
            String modalId = goods.get("modalId").toString();

            List<Map<String,Object>> list = new ArrayList<>();
            if(redis.hasKey("STYLE"+style)){
                // 有这个要拿出来再放
                list = (List<Map<String, Object>>) redis.get("STYLE"+style);
            }else{
                list = new ArrayList<>();
            }
            // 加入新的信息
            Map<String,Object> goodsItem = (Map<String, Object>) redis.get("GOODS"+modalId);
            list.add(goodsItem);

            System.out.println(i);

            redis.set("STYLE"+style,list);
        }
    }


    @Test
    public void Ref_CSV() throws IOException {
        // 参数
        String category = "L形沙发";
        int cnt = 31;

        String csvPath = "G:\\csv\\" +category +"\\";

        // 获取文件下的子文件夹名称，modelId
        String []res = Get_Zi_Dir("G:\\家具类别划分\\L形沙发");

        for(int i=0;i<cnt;i++){
            // 获取商品信息
            Map<String,Object> goodsData = HuiZong_GoodsData_ByCSV(csvPath+i+".csv");

            // 获取modelId
            String modalId = res[i];

            // 存入redis
            Map<String ,Object> goods = (Map<String, Object>) redis.get("GOODS"+modalId);
            goods.putAll(goods);

            redis.set("GOODS"+modalId,(Map<String,Object>)goods);
        }
    }

    /**
     * 重新存入所有JSON细腻
     */
    @Test
    public void test(){
        String json = readJson("goods.json");

        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("RECORDS");

        String targetPath = "G:\\target\\";

        System.out.println(jsonArray.size());
        for(int i=0;i<jsonArray.size();i++){
            JSONObject goods = jsonArray.getJSONObject(i);
            System.out.println(goods.get("modalId"));
            System.out.println(i);
            redis.set("GOODS"+goods.get("modalId").toString(),(Map<String,Object>)goods);
        }
    }

    @Test
    public Map<String,Object> GetGoodsByModalId(String id){
        return (Map<String, Object>) redis.get("GOODS"+id);
    }


    public static Map<String,Object> HuiZong_GoodsData_ByCSV(String filepath) throws IOException {
        // 读csv
        List<String[]> csv = readCsvFile(filepath);
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
        size.put("x",Double.parseDouble( csv.get(9)[0]));
        size.put("y",Double.parseDouble( csv.get(9)[1]));
        size.put("z",Double.parseDouble( csv.get(9)[2]));
        goodsData.put("size",size);

        // Center
        Map<String,Object> Center = new HashMap<>();
        Center.put("x",Double.parseDouble( csv.get(10)[0]));
        Center.put("y",Double.parseDouble( csv.get(10)[1]));
        Center.put("z",Double.parseDouble( csv.get(10)[2]));
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



    public Map<String, Object> StyleMap = new HashMap<String, Object>() {{
        put("东南亚", Arrays.asList("复古", "新古典主义", "工业"));
        put("现代", Arrays.asList("极简主义", "轻奢华", "韩式", "新中式"));
        put("日式", Arrays.asList("韩式", "现代", "极简主义", "轻奢华"));
        put("复古", Arrays.asList( "中国风", "地中海", "古典欧洲", "明清"));
        put("地中海", Arrays.asList("现代", "北欧", "美式"));
        put("韩式", Arrays.asList("日式", "现代", "极简主义", "轻奢华"));
        put("轻奢华", Arrays.asList("现代", "北欧", "新中式", "韩式"));
        put("极简主义", Arrays.asList("韩式", "现代", "儿童"));
        put("工业", Arrays.asList("极简主义", "新古典主义", "复古"));
        put("北欧", Arrays.asList("欧洲", "现代", "地中海", "美式", "儿童"));
                put("美式", Arrays.asList("欧洲", "北欧", "现代", "轻奢华"));
        put("新中式", Arrays.asList("新古典主义", "中国风", "现代", "儿童"));
        put("欧洲", Arrays.asList("北欧", "现代", "轻奢华", "古典欧洲", "复古"));
        put("新古典主义", Arrays.asList("新中式", "中国风", "复古", "北欧", "明清"));
        put("中国风", Arrays.asList("新中式", "新古典主义", "现代", "极简主义"));
        put("明清", Arrays.asList("中国风", "复古", "新中式", "新古典主义"));
        put("其他", Arrays.asList("现代", "极简主义", "轻奢华", "欧洲", "美式", "地中海"));
        put("儿童", Arrays.asList("现代", "极简主义", "轻奢华", "新中式", "日式"));
        put("古典欧洲", Arrays.asList("复古", "欧洲", "北欧", "工业", "美式", "新古典主义"));
    }};

    /**
     * 修改第一次的点错误问题
     */
    @Test
    public void Xiugai() throws IOException {
        // 参数
        String category = "L形沙发";
        int cnt = 31;

        String csvPath = "G:\\csv\\" +category +"\\";

        // 获取文件下的子文件夹名称，modelId
        String []res = Get_Zi_Dir("G:\\家具类别划分\\L形沙发");

        for(int i=0;i<cnt;i++){
            // 获取商品信息
            Map<String,Object> goodsData = HuiZong_GoodsData_ByCSV(csvPath+i+".csv");
            List<Map<String,Object>> vertexs = new ArrayList<>();
            Map<String,Object> size = (Map<String, Object>) goodsData.get("size");
            double x = Double.parseDouble( size.get("x").toString() );
            double y = Double.parseDouble( size.get("y").toString() );
            double z = Double.parseDouble( size.get("z").toString() );
            // 0
            Map<String,Object> temp = new HashMap<>();
            temp.put("x",x/2);
            temp.put("y",y/2);
            temp.put("z",z/2);
            vertexs.add(temp);
            // 1
            temp = new HashMap<>();
            temp.put("x",-1*x/2);
            temp.put("y",y/2);
            temp.put("z",z/2);
            vertexs.add(temp);
            // 2
            temp = new HashMap<>();
            temp.put("x",x/2);
            temp.put("y",-1*y/2);
            temp.put("z",z/2);
            vertexs.add(temp);
            // 3
            temp = new HashMap<>();
            temp.put("x",x/2);
            temp.put("y",y/2);
            temp.put("z",-1*z/2);
            vertexs.add(temp);
            // 4
            temp = new HashMap<>();
            temp.put("x",-1*x/2);
            temp.put("y",-1*y/2);
            temp.put("z",z/2);
            vertexs.add(temp);
            // 5
            temp = new HashMap<>();
            temp.put("x",-1*x/2);
            temp.put("y",y/2);
            temp.put("z",-1*z/2);
            vertexs.add(temp);
            // 6
            temp = new HashMap<>();
            temp.put("x",x/2);
            temp.put("y",-1*y/2);
            temp.put("z",-1*z/2);
            vertexs.add(temp);
            // 7
            temp = new HashMap<>();
            temp.put("x",-1*x/2);
            temp.put("y",-1*y/2);
            temp.put("z",-1*z/2);
            vertexs.add(temp);


            goodsData.put("vertexs",vertexs);

            // 获取modelId
            String modalId = res[i];

            // 存入redis
            Map<String ,Object> goods = (Map<String, Object>) redis.get("GOODS"+modalId);
            goods.putAll(goodsData);
            System.out.println(goods);
            redis.set("GOODS"+modalId,(Map<String,Object>)goods);
        }
    }
}
