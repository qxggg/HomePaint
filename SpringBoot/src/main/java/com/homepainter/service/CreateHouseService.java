package com.homepainter.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.controller.HouseDataController;
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

    public void create(int userId, String style, float up){
        List<HashMap<String, Object>> goods = new ArrayList<>();
        JSONObject data = (JSONObject) JSONObject.parse(HouseDataController.GetUserHouse(userId).toString());
        JSONArray rooms = data.getJSONObject("data").getJSONObject("house").getJSONArray("Room");
        JSONArray remove = data.getJSONObject("data").getJSONObject("DWW").getJSONArray("remove");

        for (int i = 0; i < rooms.size(); ++i){
            JSONObject room = rooms.getJSONObject(i);
            if (room.get("name").equals("卧室"))
                createMainRoom(room, remove, goods, style, "L形沙发",  up);
        }


    }
    public JSONObject createMainRoom(JSONObject room, JSONArray remove, List<HashMap<String, Object>> goods, String style, String category, float up){
        JSONArray wall = RuleUtils.roomHandler(room, remove);
        String json = JSON.toJSONString(getGoods.GetGoods_ByStyle_LianXiang_Category(style, category));
        JSONArray jsonArray = JSONArray.parseArray(json);
        int random = generateRandomNumber(jsonArray.size() / 5);
        for (int i = random; i < jsonArray.size(); ++i){
            JSONObject good = jsonArray.getJSONObject(i);
            JSONArray index = good.getJSONArray("vertexs");
            int goodsId = Integer.parseInt(good.getString("goodsId"));
            JSONObject center = good.getJSONObject("Center");


            for (int j = 1; j < wall.size(); ++j){
                index = good.getJSONArray("vertexs");
                JSONObject w = wall.getJSONObject(j);
                if (w.get("category").equals(2) || w.get("category").equals(1) || w.get("category").equals(3)) continue;
                RuleUtils.insertWallGoods(index, w, center);
                if (RuleUtils.inputGoods(goods, wall, up, index)){
                    w.put("category", 3);
                }
            }
        }
        return null;
    }
}
