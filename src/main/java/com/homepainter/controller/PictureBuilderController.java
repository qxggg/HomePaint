package com.homepainter.controller;

import com.homepainter.service.UserFurnitureService;
import com.homepainter.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/Module")
public class PictureBuilderController {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UserFurnitureService userFurnitureService;

    @PostMapping("/list")
    public Map<String, Object> getList(@RequestHeader String token){
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        map.put("code", 0);
        map.put("msg", "查询成功");
        map.put("data", userFurnitureService.getById(userId));
        return map;
    }

}
