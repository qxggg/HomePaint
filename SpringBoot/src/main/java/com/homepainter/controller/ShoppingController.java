package com.homepainter.controller;

import com.homepainter.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
        String qsql = "SELECT * FROM shopping where goodsId = " + goodsId + " and userId = " + userId;
        List<Map<String, Object>> mapp = jdbcTemplate.queryForList(qsql);
        if (mapp.isEmpty()){
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
        }
        else {
            String sql = "update shopping set count = count + " + count + " where goodsId = " + goodsId + " and userId = " + userId;
            jdbcTemplate.update(sql);
            map.put("code", 1);
            map.put("msg", "插入成功");
        }
        return map;
    }

    @GetMapping("/get")
    public Map<String, Object> select(@RequestHeader String token){
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        String sql = "SELECT * FROM shopping INNER JOIN goods on shopping.goodsId = goods.`goodsId`  where userId = " + userId;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> obj : list){
            int goodsId = (int) obj.get("goodsId");
            String sql2 = "select * from goods_image where goodsId = " + goodsId;
            List<Map<String, Object>> o = jdbcTemplate.queryForList(sql2);
            obj.put("goods_image", o);
        }
        map.put("data", list);
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

    @PostMapping("/update")
    public Map<String, Object> update(@RequestHeader String token, @RequestBody Map<String, Object> data){
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        int goodsId = (int) data.get("goods_id");
        int count = (int) data.get("count");
        String sql = "update shopping set count = " + count + " where userId = " + userId + " and goodsId = " + goodsId;
        jdbcTemplate.update(sql);
        map.put("code", 0);
        map.put("msg", "修改成功");
        return map;
    }

}
