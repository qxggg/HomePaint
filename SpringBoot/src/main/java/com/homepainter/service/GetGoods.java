package com.homepainter.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetGoods {

    @Autowired
    RedisUtil redisUtil;

    public List<String> getStyleList(int userId){
        List<String> styles = new ArrayList<>();
        if(redisUtil.hasKey("UserStyle" + userId)) styles = (List<String>) redisUtil.get("UserStyle"+ userId);
        else styles.add("现代");
        return styles;
    }

    public List<Map<String,Object>> GetGoods_ByStyle_Category(String style, String category){
        List<Map<String, Object>> res = new ArrayList<>();
        List<Map<String, Object>> list = (List<Map<String, Object>>) redisUtil.get("STYLE"+style);
        for(int i=0;i< list.size();i++){
            if(list.get(i).get("category").toString().equals(category)){
                res.add(list.get(i));
            }
        }

        return res;
    }

    /**
     * 根据 Id 查家具
     * @param id
     * @return
     */
    public  Map<String,Object> GetGoods_ByModalId(String id){
        return (Map<String, Object>) redisUtil.get("GOODS"+id);
    }

    /**
     * 根据 风格 选家具
     * @param style
     * @return
     */
    public List<Map<String,Object>> GetGoods_ByStyle(String style){
        return (List<Map<String, Object>>) redisUtil.get("STYLE"+style);
    }




    /**
     * 根据 联想风格 去选家具
     * @param style
     * @return
     */
    public List<Map<String,Object>> GetGoods_ByStyle_LianXiang(String style){
        return (List<Map<String, Object>>) redisUtil.get("LianXiangSTYLE"+style);
    }

    /**
     * 根据 风格联想 和 类别 进行搜索商品
     * @param style
     * @param category
     * @return
     */
    public List<Map<String,Object>> GetGoods_ByStyle_LianXiang_Category(String style,String category){
        List<Map<String, Object>> res = new ArrayList<>();
        List<Map<String, Object>> list = (List<Map<String, Object>>) redisUtil.get("LianXiangSTYLE"+style);
        for(int i=0;i< list.size();i++){
            if(list.get(i).get("category").toString().equals(category)){
                res.add(list.get(i));
            }
        }

        return res;
    }



    /**
        相关度
     */
    public Map<String, Object> map = new HashMap<String, Object>() {{
        put("东南亚", Arrays.asList("现代", "日式", "复古"));
        put("现代", Arrays.asList("现代", "日式", "复古"));
        put("复古", Arrays.asList("现代", "日式"));
    }};

    public List<String> GetStyle_LianXiang(String style){
        return (List<String>) map.get(style);
    }

    public  List<Map<String, Object>> changeFloorStyle(String style) {
        List<Map<String, Object>> array = new ArrayList<>();
        List<Map<String, Object>> floorList = (List<Map<String, Object>>) redisUtil.get("floorList");
        for (int i = 0; i < floorList.size(); ++i) {
            Map<String, Object> floor = floorList.get(i);
            String s = (String) floor.get("style");
            if (s == null) continue;
            if (s.equals(style))
                array.add(floor);
        }
        return array;
    }


    public  List<Map<String, Object>> changeWallpaintStyle(String style) {
        List<Map<String, Object>> array = new ArrayList<>();
        List<Map<String, Object>> floorList = (List<Map<String, Object>>) redisUtil.get("wallpaintList");
        for (int i = 0; i < floorList.size(); ++i) {
            Map<String, Object> floor = floorList.get(i);
            String s = (String) floor.get("style");
            if (s == null) continue;
            if (s.equals(style))
                array.add(floor);
        }
        return array;
    }
}
