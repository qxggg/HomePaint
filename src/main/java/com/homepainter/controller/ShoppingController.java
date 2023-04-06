package com.homepainter.controller;

import com.homepainter.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/shopping")
public class ShoppingController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    RedisUtil redisUtil;

    @PostMapping("/insert")
    public Map<String, Object> insert(@RequestBody Map<String, Object> data, @RequestHeader String token){
        Map<String, Object> map = new HashMap<>();
        int goodsId = (int) data.get("goods_id");
        int count = (int) data.get("count");
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        String sql = "INSERT INTO shopping (goodsId, userId, time, count) VALUES (?, ?, ?, ?)\n";
        Date date = new Date();
        if (jdbcTemplate.update(sql, goodsId, userId, date, count) == 1){
            map.put("code", 0);
            map.put("msg", "插入成功");
        }
        else {
            map.put("code", 1);
            map.put("msg", "插入失败");
        }
        return map;
    }

    @GetMapping("/get")
    public Map<String, Object> select(@RequestHeader String token){
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        String sql = "SELECT * FROM shopping INNER JOIN goods WHERE shopping.goodsId = goods.`goodsId`" + " and userId = " + userId;
        map.put("data", jdbcTemplate.queryForList(sql));
        map.put("code", 0);
        map.put("msg", "查询成功");
        return map;
    }

    @PostMapping("/delete")
    public Map<String, Object> delete(@RequestHeader String token, @RequestBody Map<String, Object> data){
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        int goodsId = (int) data.get("goods_id");
        String sql = "delete from shopping where userId = ? and goodsId = " + goodsId;
        if (jdbcTemplate.update(sql, userId) == 1){
            map.put("code", 0);
            map.put("msg", "删除成功");
        }
        else{
            map.put("code", 1);
            map.put("msg", "删除失败");
        }
        return map;

    }

}
