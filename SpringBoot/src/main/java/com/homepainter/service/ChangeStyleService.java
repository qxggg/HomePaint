package com.homepainter.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.util.RuleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ChangeStyleService {

    @Autowired
    GetGoods getGoods;


    /**
     List<String> styles = getGoods.getStyleList(userId);
     JSONObject idx = new JSONObject();
     JSONObject room = new JSONObject();
     idx.put("x", x);
     idx.put("y", y);
     for (int i = 0; i < rooms.size(); ++i){
     room = rooms.getJSONObject(i);
     JSONArray wallPoint = room.getJSONArray("point");
     if (RuleUtils.isPeopleInHouse(idx, wallPoint)) {
     System.out.println(i) ;break;}
     }
     */


    /**
     for (int i = 0; i < goods.size(); ++i){
     JSONObject good = goods.getJSONObject(i);
     List<Double> idx = new ArrayList<>();
     idx = (List<Double>) good.get("position");
     double x = idx.get(0);
     double y = idx.get(2);
     */
    public void changeStyle(String style, JSONArray rooms, JSONObject furniture, List<Integer> roomList){
        JSONArray goods = furniture.getJSONArray("goods");
        JSONArray floors = furniture.getJSONArray("floor");
        JSONArray wallpaper = furniture.getJSONArray("WallPaper");

        for (int j = 0; j < roomList.size(); ++j){
            int roomId = roomList.get(j);
            JSONObject roomInfo = new JSONObject();
            JSONArray points = new JSONArray();
            for (int k = 0; k < rooms.size(); ++k)
                if (rooms.getJSONObject(k).getInteger("roomId") == roomId) {
                    roomInfo = rooms.getJSONObject(k);
                    points = roomInfo.getJSONArray("point");
                }
            for (int i = 0; i < goods.size(); ++i){
                JSONObject good = goods.getJSONObject(i);
                String ModalId = good.getString("modalId");
                List<Double> idx = new ArrayList<>();
                idx = (List<Double>) good.get("position");
                JSONObject index = new JSONObject();
                index.put("x", idx.get(0));
                index.put("y", idx.get(2));
                if (RuleUtils.isPeopleInHouse(index, points)){
                    System.out.println(RuleUtils.isPeopleInHouse(index, points));
                    System.out.println();
                    JSONObject goodInfo = (JSONObject) getGoods.GetGoods_ByModalId(ModalId);
                    String category = goodInfo.getString("category");
                    List<Map<String, Object>> changeGood = getGoods.GetGoods_ByStyle_Category(style, category);
                    if (changeGood.isEmpty()) changeGood = getGoods.GetGoods_ByStyle_LianXiang_Category(style, category);
                    if (changeGood.isEmpty()) continue;
                    int random = CreateHouseService.generateRandomNumber(changeGood.size() - 1);
                    Map<String, Object> cg = changeGood.get(random);
                    good.put("modalId", cg.get("modalId"));
                    good.put("title", cg.get("title"));
                }
            }


            List<Map<String, Object>> styleFloor = getGoods.changeFloorStyle(style);
            int random = CreateHouseService.generateRandomNumber(styleFloor.size() - 1);
            Map<String, Object> sf = styleFloor.get(random);
            for (int i = 0; i < floors.size(); ++i){
                JSONObject floor = floors.getJSONObject(i);
                int rId = floor.getInteger("roomId");
                if (rId == roomId){
                    floor.put("imageURL", sf.get("imageURL"));
                    floor.put("id", sf.get("id"));
                    floor.put("price", sf.get("price"));
                }
            }

            for (int i = 0; i < wallpaper.size(); ++i){
                JSONObject wallpape = wallpaper.getJSONObject(i);
                int rId = wallpape.getInteger("roomId");
                if (rId == roomId){
                    List<Map<String, Object>> stylePaper = getGoods.changeWallpaintStyle(style);
                    wallpape.put("imageURL", sf.get("imageURL"));
                    wallpape.put("id", sf.get("id"));
                    wallpape.put("price", sf.get("price"));
                }
            }
            }

        System.out.println(floors);
        }

}
