package com.homepainter.controller;

import com.homepainter.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/family")
public class FamilyController {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostMapping("/create")
    public Map<String, Object> createFamily(@RequestHeader String token, @RequestBody Map<String, Object> data){
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        String family_name = (String) data.get("family_name");
        String nick_name = (String) data.get("nick_name");
        String sql = "insert into family values (?, ?, ?, ?, ?)";
        if (jdbcTemplate.update(sql, null, family_name, userId, nick_name, true) == 1){
            map.put("code", 0);
            map.put("msg", "创建成功");
        }
        else{
            map.put("code", 1);
            map.put("msg", "创建失败");
        }
        return map;
    }

    @PostMapping("join")
    public Map<String, Object> joinFamily(@RequestHeader String token, @RequestBody Map<String, Object> data){
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        int family_id = (int) data.get("family_id");
        String nickname = (String) data.get("nickname");
        String sqls = "select familyName from family where familyId = " + family_id;
        Map<String, Object> res = jdbcTemplate.queryForMap(sqls);
        String family_name = (String) res.get("familyName");
        String sql = "insert into family values (?, ?, ?, ?)";
        if (jdbcTemplate.update(sql, family_id, family_name, userId, nickname) == 1){
            map.put("code", 0);
            map.put("msg", "加入成功");
        }
        else{
            map.put("code", 1);
            map.put("msg", "加入失败");
        }
        return map;
    }


    @PostMapping("delete")
    public Map<String, Object> deleteFamily(@RequestHeader String token, @RequestBody Map<String, Object> data){
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        int family_id = (int) data.get("family_id");

        String sql = "delete from family where familyId = " + family_id + " and userId = " + userId;
        if (jdbcTemplate.update(sql) == 1){
            map.put("code", 0);
            map.put("msg", "删除成功");
        }
        else{
            map.put("code", 1);
            map.put("msg", "删除失败");
        }
        return map;
    }

    @GetMapping("get")
    public Map<String, Object> getFamily(@RequestHeader String token){
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        String sql = "select familyId from family where userId =" + userId;
        Map<String, Object> res = jdbcTemplate.queryForMap(sql);
        int familyId = (int) res.get("familyId");
        String sql2 = "select family.userId, telephone, avatar, nickName from family inner join user on family.userId = `user`.userId where family.userId = " + userId;
        map.put("code", 0);
        map.put("data", jdbcTemplate.queryForList(sql2));
        map.put("msg", "查询成功");
        return map;
    }
}
