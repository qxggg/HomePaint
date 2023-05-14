package com.homepainter.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alinlp.model.v20200629.GetPosChEcomRequest;
import com.aliyuncs.alinlp.model.v20200629.GetPosChEcomResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.homepainter.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.homepainter.util.ReadJson.get_json;
import static com.homepainter.util.ReadJson.readJson;
import static com.homepainter.util.getStyleUtils.getStyle;

@Service
public class Search_Service {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    RedisUtil redisUtil;

    public String[] styleMenu = {"东南亚","现代","日式","复古","地中海","韩式","轻奢华","极简主义","工业","北欧","美式","新中式","欧洲","新古典主义","中国风","明清","其他","儿童","古典欧洲"};

    public List<Map<String, Object>> searchByStyle(List<Map<String, Object>> StyleList,int skip){
        List<Map<String, Object>> res = new ArrayList<>();

        // 空值检查
        if(StyleList.isEmpty()){
            Map<String, Object> m1 = new HashMap<>();
            m1.put("styleId", 0);
            StyleList.add(m1);
        }

        // 根据风格搜索家具
        String querySql = "select * from goods where style in (";
        for (Map<String, Object> m : StyleList)
            querySql += "\'" + getStyle((int) m.get("styleId")) + "\'" + ",";
        String Qsql = querySql.substring(0, querySql.length() - 1) + ") limit "+skip+",20";
        res = jdbcTemplate.queryForList(Qsql);


        // 添加评价信息和图片
        for (Map<String, Object> item : res){
            String sql1 = "select * from goods_appraise where goodsId = " + item.get("goodsId");
            String sql2 = "select * from goods_image where goodsId = " + item.get("goodsId");
            item.put("goods_appraise", jdbcTemplate.queryForList(sql1));
            List<Map<String, Object>> l = new ArrayList<>();
            l = jdbcTemplate.queryForList(sql2);
            item.put("goods_image", l);
            item.put("imageURL", l.get(0).get("imageUrl"));
        }

        return res;

    }


    /**
     * kmp搜索
     * @param args 搜索内容
     * @param skip 分页
     * @return
     * @throws ClientException
     */
    public List<Map<String,Object>> kmpSearch(String args,int skip) throws ClientException {
        int cnt = 0;
        // 调用分词
        GetPosChEcomResponse response = fenci(args);
        // 获取分词结果
        String fenci_result = response.getData();
        System.out.println("分词结果："+fenci_result + new Date().toString());
        // 获取结果数组
        JSONArray jsonArray = JSON.parseArray(JSON.parseObject(fenci_result).getString("result"));
        // 结果集
        List<Map<String,Object>> data = new ArrayList<>();
        for(int i=0;i<jsonArray.size();i++){
            // 单个结果集转Object
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            // 获取搜索结果
            List<Map<String,Object>> temp = searchByname(jsonObject.getString("word"),skip+20);
            System.out.println("多模态 文字 搜索完毕"+new Date().toString());
            // 筛选是否已有
            for(Map<String,Object> item :temp){
                boolean is_have = false;
                for(Map<String,Object> it :data){
                    if(it.get("goodsId").toString().equals(item.get("goodsId").toString())){
                        is_have = true;
                        break;
                    }
                }
                if(!is_have){
                    data.add(item);
                }
            }
        }
        // 执行分页
        System.out.println("size"+data.size());
        data = data.subList(Math.min(skip,data.size()),Math.min(skip+20,data.size()));
        // 查看结果集
        for(int i=0;i<data.size();i++){
            data.get(i).put("imageURL","https://image-1304455659.cos.ap-nanjing.myqcloud.com/3D-FUTURE-model-part1/"+data.get(i).get("modalId")+"/image.jpg");
        }
        return data;
    }

    /**
     * 分词接口
     * @param text 要分词的内容
     * @return
     * @throws ClientException
     */
    public static GetPosChEcomResponse fenci(String text) throws ClientException {
        DefaultProfile defaultProfile = DefaultProfile.getProfile(
                "cn-hangzhou",
                "LTAI5t7SkD8SohPvVLoPbUeo",
                "DoMnPYROzBzSileNuVDK6BMA4lZYoD");
        IAcsClient client = new DefaultAcsClient(defaultProfile);
        //构造请求参数，其中GetPosChEcom是算法的actionName, 请查找对应的《API基础信息参考》文档并替换为您需要的算法的ActionName，示例详见下方文档中的：更换API请求
        GetPosChEcomRequest request = new GetPosChEcomRequest();
        //固定值，无需更改
        request.setSysEndpoint("alinlp.cn-hangzhou.aliyuncs.com");
        //固定值，无需更改
        request.setServiceCode("alinlp");
        //请求参数, 具体请参考《API基础信息文档》进行替换与填写
        request.setText(text);
        request.setTokenizerId("MAINSE");
        long start = System.currentTimeMillis();
        //获取请求结果，注意这里的GetPosChEcom也需要替换
        GetPosChEcomResponse response = client.getAcsResponse(request);
        return response;
    }

    /**
     * 检索接口，根据要检索的内容，在goods.json中查找
     * @param content
     * @return
     */
    public  List<Map<String,Object>> searchByname(String content,int search_cnt){

        List<Map<String,Object>>  data = new ArrayList<>();
        // 打开JSON信息表
        List<Map<String,Object>> goodsData = (List<Map<String, Object>>) redisUtil.get("JSON_GOODS");

        System.out.println("打开了JSON"+new Date().toString());
        for(Map<String,Object> item :goodsData){

            List<Integer> list = kmp(item.get("title").toString(),content);
            if(list.size()!=0){
                data.add(item);
            }
            if(data.size()>=search_cnt){
                break;
            }
        }
        return data;
    }

    /**
     * 根据list在json中查找对应的家具，并分页
     * @param inputList
     * @param skip 分页
     * @return
     */
    public static List<Map<String,Object>> searchByList(List<Integer> inputList,int skip){

        List<Map<String,Object>>  data = new ArrayList<>();

        String json = readJson("goods.json","");
        JSONObject Alljson = JSON.parseObject(json);
        JSONArray jsonArray =  JSON.parseArray(Alljson.getString("RECORDS"));
        int len = inputList.size();
        for(int i=skip;i<skip+20;i++){
            i = i%len;
            data.add( (Map<String,Object>)jsonArray.getJSONObject(inputList.get(i)) );
        }

        return data;
    }


    /**
     * 计算前缀数组
     * @param pattern
     * @return
     */
    public static int[] computePrefixFunction(String pattern) {
        int[] prefix = new int[pattern.length()];
        int j = 0;
        for (int i = 1; i < pattern.length(); i++) {
            while (j > 0 && pattern.charAt(j) != pattern.charAt(i)) {
                j = prefix[j - 1];
            }
            if (pattern.charAt(j) == pattern.charAt(i)) {
                j++;
            }
            prefix[i] = j;
        }
        return prefix;
    }

    /**
     * KMP算法
     * @param text
     * @param pattern
     * @return
     */
    public static List<Integer> kmp(String text, String pattern) {
        List<Integer> matches = new ArrayList<Integer>();
        if (pattern.length() == 0) {
            matches.add(0);
            return matches;
        }
        int[] prefix = computePrefixFunction(pattern);
        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && pattern.charAt(j) != text.charAt(i)) j = prefix[j - 1];
            if (pattern.charAt(j) == text.charAt(i)) j++;
            if (j == pattern.length()) {
                matches.add(i - pattern.length() + 1);
                j = prefix[j - 1];
            }
        }
        return matches;
    }


    /**
     * 根据风格 list 进行排序
     * @param list
     * @param style
     * @return
     */
    public List<Map<String,Object>> SortByStyleList(List<Map<String,Object>> list,List<Map<String, Object>> style){
        List<Map<String,Object>> res = new ArrayList<>();

        // 首先将style的加入
        for(int i=0;i<list.size();i++){
            boolean havesameStyle =false;
            for (Map<String, Object> item : style)
                if(list.get(i).get("style").toString().equals( getStyle( Integer.parseInt( item.get("styleId").toString() ) ) )){
                    havesameStyle = true;
                    break;
                }
            if(havesameStyle)   res.add(list.get(i));
        }
        // 将其他的加入
        for(int i=0;i<list.size();i++){
            boolean havesameStyle =false;
            for (Map<String, Object> item : style)
                if(list.get(i).get("style").toString().equals( getStyle( Integer.parseInt( item.get("styleId").toString() ) ))){
                    havesameStyle = true;
                    break;
                }
            if(!havesameStyle)   res.add(list.get(i));
        }
        return res;
    }

    /**
     * 重载
     * @param list
     * @param style
     * @return
     */
    public List<Map<String,Object>> SortByStyleList_String(List<Map<String,Object>> list,List<String> style){
        List<Map<String,Object>> res = new ArrayList<>();

        // 首先将style的加入
        for(int i=0;i<list.size();i++){
            boolean havesameStyle =false;
            for (String item : style)
                if(list.get(i).get("style").toString().equals(item) ) {
                    havesameStyle = true;
                    break;
                }
            if(havesameStyle)   res.add(list.get(i));
        }
        // 将其他的加入
        for(int i=0;i<list.size();i++){
            boolean havesameStyle =false;
            for (String item : style)
                if(list.get(i).get("style").toString().equals( item)){
                    havesameStyle = true;
                    break;
                }
            if(!havesameStyle)   res.add(list.get(i));
        }
        return res;
    }


    public List<Map<String,Object>> SortByFurnitureList(List<Map<String,Object>> list,List<Integer> furnitureList){
        List<Map<String,Object>> res = new ArrayList<>();

        // 首先将匹配的加入
        for(int i=0;i<list.size();i++){
            boolean havesame =false;
            for (Integer item : furnitureList)
                if(list.get(i).get("goodsId").toString().equals( String.valueOf(item) ) ){
                    havesame = true;
                    break;
                }
            if(havesame)   res.add(list.get(i));
        }

        // 将其他的加入
        for(int i=0;i<list.size();i++){
            boolean havesame =false;
            for (Integer item : furnitureList)
                if(list.get(i).get("goodsId").toString().equals( String.valueOf(item) ) ){
                    havesame = true;
                    break;
                }
            if(!havesame)   res.add(list.get(i));
        }
        return res;
    }

}
