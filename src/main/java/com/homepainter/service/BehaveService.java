package com.homepainter.service;

import com.homepainter.mapper.GoodsMapper;
import com.homepainter.util.RedisUtil;
import com.homepainter.util.getStyleUtils;
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

    @Autowired
    GoodsMapper goodsMapper;

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

    public void updateGoods(int userId, int goodsId, String behave, Object update){
        Map<String, Object> map = (Map<String, Object>) redisUtil.get("goodsBehave" + userId);
        List<Object> list = (List<Object>) map.get(behave);
        list.set(goodsId, update);
        map.put(behave, list);
        List<Object> idx = (List<Object>) map.get("idx");
        if (!idx.contains(goodsId)) idx.add(goodsId);
        System.out.println(idx);
        redisUtil.set("goodsBehave" + userId, map);
    }

    public void updateAddGoods(int userId, int goodsId, String behave, int add){
        Map<String, Object> map = (Map<String, Object>) redisUtil.get("goodsBehave" + userId);
        List<Object> list = (List<Object>) map.get(behave);
        list.set(goodsId, (Integer)list.get(goodsId) + add);
        map.put(behave, list);
        List<Object> idx = (List<Object>) map.get("idx");
        if (!idx.contains(goodsId)) idx.add(goodsId);
        System.out.println(idx);
        redisUtil.set("goodsBehave" + userId, map);
    }

    public void updateStyle(int userId, int goodsId, String behave, Object update){
        Map<String, Object> map = (Map<String, Object>) redisUtil.get("styleBehave" + userId);
        List<Object> list = (List<Object>) map.get(behave);
        int styleId = getStyleUtils.getId(goodsMapper.getStyleById(goodsId));
        list.set(styleId, update);
        map.put(behave, list);
        List<Object> idx = (List<Object>) map.get("idx");
        if (!idx.contains(styleId)) idx.add(styleId);
        redisUtil.set("styleBehave" + userId, map);
    }

    public void updateAddStyle(int userId, int goodsId, String behave, int add){
        Map<String, Object> map = (Map<String, Object>) redisUtil.get("styleBehave" + userId);
        List<Object> list = (List<Object>) map.get(behave);
        int styleId = getStyleUtils.getId(goodsMapper.getStyleById(goodsId));
        list.set(styleId, (Integer)list.get(styleId) + add);
        map.put(behave, list);
        List<Object> idx = (List<Object>) map.get("idx");
        if (!idx.contains(styleId)) idx.add(styleId);
        System.out.println(idx);
        redisUtil.set("styleBehave" + userId, map);
    }

}
