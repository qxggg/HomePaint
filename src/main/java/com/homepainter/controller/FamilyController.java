package com.homepainter.controller;

import com.homepainter.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        Date date = new Date();
        String sql = "insert into family values (?, ?, ?, ?, ?, ?)";
        if (jdbcTemplate.update(sql, null, family_name, userId, nick_name, true, date) == 1){
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
        System.out.println(token);
        System.out.println(id);
        int userId = Integer.parseInt(id.substring(5));

        int family_id = (int) data.get("family_id");
        String nickname = (String) data.get("nickname");

        String sq = "select * from family where userId = " + userId;
        if (!jdbcTemplate.queryForList(sq).isEmpty()){
            map.put("code", 1);
            map.put("msg", "该用户已有家庭");
            return map;
        }

        String sqls = "select familyName from family where familyId = " + family_id;
        List<Map<String, Object>> res = jdbcTemplate.queryForList(sqls);
        String family_name = (String) res.get(0).get("familyName");
        Date date = new Date();
        String sql = "insert into family values (?, ?, ?, ?, ?, ?)";
        if (jdbcTemplate.update(sql, family_id, family_name, userId, nickname, false, date) == 1){
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
        int user_id = (int) data.get("user_id");
        System.out.println(family_id);
        System.out.println(userId);
        String sql = "delete from family where familyId = " + family_id + " and userId = " + user_id;
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
        if (jdbcTemplate.queryForList(sql).isEmpty()){
            List<Integer> l = new ArrayList<>();
            map.put("code", 0);
            map.put("msg", "该用户没有家庭");
            map.put("data", l);
            return map;
        }
        Map<String, Object> res = jdbcTemplate.queryForMap(sql);
        int familyId = (int) res.get("familyId");
        String sql1 = "select * from family inner join user on family.userId = `user`.userId inner join house on family.familyId = house.familyId where family.familyId = " + familyId;
        String sql2 = "select * from family inner join user on family.userId = `user`.userId where family.familyId = " + familyId;
        if (jdbcTemplate.queryForList(sql1).isEmpty())
            map.put("data", jdbcTemplate.queryForList(sql2));
        else map.put("data", jdbcTemplate.queryForList(sql1));
        map.put("code", 0);
        map.put("msg", "查询成功");
        return map;
    }
}
