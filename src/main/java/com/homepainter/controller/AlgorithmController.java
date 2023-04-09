package com.homepainter.controller;

import com.homepainter.service.Algorithm;
import com.homepainter.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.incrementer.HsqlMaxValueIncrementer;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("algorithm")
public class AlgorithmController {

    public static String commentInfo;

    public static int recommendId;

    public static String recommendType;

    @Autowired
    Algorithm algorithm;

    @Autowired
    RedisUtil redisUtil;

    @GetMapping("comment")
    public Map<String, Object> comment(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("comment", commentInfo);
        return map;
    }

    @GetMapping("get_rec")
    public Map<String, Object> get_rec(){
        HashMap<String, Object> map = new HashMap<>();
        return map;
    }

    @GetMapping("recommandation")
    public Map<String, Object> recommdation(){
        HashMap<String, Object> map = new HashMap<>();
        return map;
    }

    @GetMapping("/recommandationId")
    public Map<String, Object> recommdationId(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", recommendType);
        map.put("userId", recommdationId());
        return map;
    }

}
