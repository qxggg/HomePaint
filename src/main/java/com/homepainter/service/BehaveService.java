package com.homepainter.service;

import com.homepainter.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BehaveService {

    @Autowired
    RedisUtil redisUtil;

    public void initStyle(int userId){
        List<Integer> intList = new ArrayList<>();
        List<Float> floatList = new ArrayList<>();
        for (int i = 0; i < 19; ++i){
            intList.add(0);
            floatList.add(0.0f);
        }
        List<Integer> idx = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("randomConsume", intList);
        map.put("randomClick", intList);
        map.put("randomCollect", intList);
        map.put("randomComment", floatList);
        map.put("randomUse", intList);
        map.put("randomView", intList);
        map.put("randomSearchClick", intList);
        map.put("idx", idx);
        redisUtil.set("styleBehave" + userId, map);

    }

    public void initGoods(int userId){
        List<Integer> intList = new ArrayList<>();
        List<Float> floatList = new ArrayList<>();
        for (int i = 0; i < 2000; ++i){
            intList.add(0);
            floatList.add(0.0f);
        }
        List<Integer> idx = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("randomConsume", intList);
        map.put("randomClick", intList);
        map.put("randomCollect", intList);
        map.put("randomComment", floatList);
        map.put("randomUse", intList);
        map.put("randomView", intList);
        map.put("idx", idx);
        redisUtil.set("goodsBehave" + userId, map);
    }

}
