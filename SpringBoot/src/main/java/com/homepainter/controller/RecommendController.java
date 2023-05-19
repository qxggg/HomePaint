package com.homepainter.controller;

import com.alibaba.fastjson.JSONObject;
import com.homepainter.service.CreateHouseService;
import com.homepainter.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    CreateHouseService createHouseService;

    @Autowired
    RedisUtil redisUtil;

    @PostMapping("")
    public Map<String, Object> recommend(@RequestHeader String token, @RequestBody Map<String, Object> data) {
        Map<String, Object> map = new HashMap<>();
        String id = (String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        String style = (String) data.get("style");
        double x = (double) data.get("x");
        double y = (double) data.get("y");
        JSONObject j = (JSONObject) JSONObject.parse(HouseDataController.GetUserHouse(userId).toString());
//
//        createHouseService.createHouse(userId, x, y, rooms)

        map.put("data", createHouseService.recommend(userId, style));
        map.put("code", 0);
        map.put("msg", "推荐结果获取成功！");
        return map;
    }
}
