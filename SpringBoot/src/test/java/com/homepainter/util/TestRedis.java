package com.homepainter.util;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

@SpringBootTest
public class TestRedis {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    JdbcTemplate jdbcTemplate;

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

    public static double generate() {
        double[] numbers = {29.9, 39.9, 52, 66.6, 46.9, 99.9, 70.9, 15, 168.0};
        Random random = new Random();
        int index = random.nextInt(numbers.length);
        return numbers[index];
    }
    @Test
    public void insertFloor(){

        String sql = "insert into floors values(?, ?, ?)";
        for (int i = 0; i < 1016; ++i){
            double number = generate();
            jdbcTemplate.update(sql, i, "https://image-1304455659.cos.ap-nanjing.myqcloud.com/DiBan/" + i + ".jpg", number);
        }
    }

    @Test
    public void insertWallPaint(){
        String sql = "insert into wallpaint values(?, ?, ?)";
        for (int i = 0; i < 801; ++i){
            double number = generate();
            jdbcTemplate.update(sql, i, "https://image-1304455659.cos.ap-nanjing.myqcloud.com/BiZhi/" + i + ".jpg", number);
        }
    }

    @Test
    public void insertModalId(){
        Set<String> keys = (Set<String>) redisUtil.getAllKeys();
        for (String key : keys) {
            String a = key.substring(0, 5);
            String b = key.substring(5, key.length());
            if (a.equals("GOODS")) {
                JSONObject j = (JSONObject) redisUtil.get(key);
                j.put("imageURL", "https://image-1304455659.cos.ap-nanjing.myqcloud.com/3D-FUTURE-model-part1/" + b + "/image.jpg");
                redisUtil.set(key, j);
            }
        }
    }
}
