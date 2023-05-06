package com.homepainter.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class TestRedis {

    @Autowired
    RedisUtil redisUtil;
    @Test
    public void testRedis(){
        List<Integer> insert = new ArrayList<>();
        for (int i = 0; i < 2000; ++i) insert.add(0);
        Map<String, Object> map = new HashMap<>();
        map.put("RandomConsume", insert);
        map.put("RandomView", insert);
        redisUtil.set("4Behave", map);
        map = (Map<String, Object>) redisUtil.get("4Behave");
        System.out.println(map);
    }
}
