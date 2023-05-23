package com.homepainter.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("cost")
public class CostController {

    @Autowired
    RedisUtil redisUtil;
    @GetMapping("get")
    public JSONObject cost(@RequestHeader String token){
        JSONObject res = new JSONObject();
        String tel =(String) redisUtil.get(token);
        int userId = Integer.parseInt(tel.substring(5));
        JSONObject json = (JSONObject) HouseDataController.GetUserHouse(userId);
        JSONObject furniture = json.getJSONObject("furniture");
        JSONArray floors = furniture.getJSONArray("floor");
        JSONArray wallPapers = furniture.getJSONArray("WallPaper");


        JSONArray rooms = json.getJSONObject("house").getJSONArray("Room");
        for (int i = 0; i < floors.size(); ++i){
            JSONObject floor = floors.getJSONObject(i);
            double price = floor.getDouble("price");
            int roomId = floor.getInteger("roomId");
            for (int j = 0; j < rooms.size(); ++j)
                if (roomId == rooms.getJSONObject(j).getInteger("roomId")) {
                    price = price * rooms.getJSONObject(j).getDouble("area");
                    floor.put("area", rooms.getJSONObject(j).getDouble("area"));
                    floor.put("totalPrice", price * 0.8);
                }


        }

        for (int i = 0; i < wallPapers.size(); ++i){
            JSONObject wallPaper = wallPapers.getJSONObject(i);
            double price = wallPaper.getDouble("price");
            int roomId = wallPaper.getInteger("roomId");
            for (int j = 0; j < rooms.size(); ++j)
                if (roomId == rooms.getJSONObject(j).getInteger("roomId")){
                    JSONObject room = rooms.getJSONObject(j);
                    JSONArray points = room.getJSONArray("point");
                    double area = 0;
                    for (int x = 0; x < points.size(); ++x){
                        JSONObject point1 = points.getJSONObject(x);
                        JSONObject point2 = points.getJSONObject((x + 1) % points.size());
                        double x1 = point1.getDouble("x"), y1 = point1.getDouble("y"),
                        x2 = point2.getDouble("x"), y2 = point2.getDouble("y");
                        area += Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
                    }
                    wallPaper.put("area", area);
                    price = price * area;
                    wallPaper.put("totalPrice", price * 0.8);
                }

        }

        res.put("wallPaper", wallPapers);
        res.put("floor", floors);
        return res;
    }
}
