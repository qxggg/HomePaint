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
    @GetMapping
    public JSONObject cost(@RequestHeader String token){
        JSONObject j = new JSONObject();
        String tel =(String) redisUtil.get(token);
        int userId = Integer.parseInt(tel.substring(5));
        JSONObject json = (JSONObject) HouseDataController.GetUserHouse(userId);
        JSONObject furniture = json.getJSONObject("furniture");
        JSONArray floors = furniture.getJSONArray("floor");
        JSONArray wallPaper = furniture.getJSONArray("wallPaper");

        return j;
    }
}
