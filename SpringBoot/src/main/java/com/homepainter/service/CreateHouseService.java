package com.homepainter.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.controller.HouseDataController;
import com.homepainter.util.RedisUtil;
import com.homepainter.util.RuleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
public class CreateHouseService {

    @Autowired
    GetGoods getGoods;




    public static int generateRandomNumber(int n) {
        Random random = new Random();
        return random.nextInt(n + 1);
    }

    public JSONArray recommend(int userId, String style) {
      return null;
    }

    public JSONArray createLivingRoom(String style){
        JSONArray sum = new JSONArray();
        find(sum, style, "三座/多座沙发");
        find(sum, style, "咖啡桌");
        find(sum, style, "圆形茶几");
        return sum;
    }

    public JSONArray createBathroom(String style){
        JSONArray sum = new JSONArray();
        return sum;
    }

    public JSONArray createKitchen(String style){
        JSONArray sum = new JSONArray();
        return sum;
    }

    public JSONArray createBedroom(String style){
        JSONArray sum = new JSONArray();
        return sum;
    }




    public void find(JSONArray sum, String style, String category) {
        String json = JSON.toJSONString(getGoods.GetGoods_ByStyle_Category(style, category));
        JSONArray jsonArray = JSONArray.parseArray(json);
        if (jsonArray.isEmpty()) return;
        if (jsonArray.size() == 1) sum.add(jsonArray.get(0));
        else {
            for (int i = 0; i < 2; ++i) {
                int random = generateRandomNumber(jsonArray.size() - 1);
                sum.add(jsonArray.get(random));
            }
        }
        return;
    }
}


//    public JSONObject createMainRoom(JSONObject room, JSONArray remove, List<HashMap<String, Object>> goods, String style, String category, float up){
//        JSONArray wall = RuleUtils.roomHandler(room, remove);
//        String json = JSON.toJSONString(getGoods.GetGoods_ByStyle_LianXiang_Category(style, category));
//        JSONArray jsonArray = JSONArray.parseArray(json);
//        int random = generateRandomNumber(jsonArray.size() / 5);
//        for (int i = random; i < jsonArray.size(); ++i){
//            JSONObject good = jsonArray.getJSONObject(i);
//            JSONArray index = good.getJSONArray("vertexs");
//            int goodsId = Integer.parseInt(good.getString("goodsId"));
//            JSONObject center = good.getJSONObject("Center");
//
//
//            for (int j = 1; j < wall.size(); ++j){
//                index = good.getJSONArray("vertexs");
//                JSONObject w = wall.getJSONObject(j);
//                if (w.get("category").equals(2) || w.get("category").equals(1) || w.get("category").equals(3)) continue;
//                RuleUtils.insertWallGoods(index, w, center);
//                if (RuleUtils.inputGoods(goods, wall, up, index)){
//                    w.put("category", 3);
//                }
//            }
//        }
//        return null;
//    }
//}

