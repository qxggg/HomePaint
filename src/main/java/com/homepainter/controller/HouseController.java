package com.homepainter.controller;

import com.homepainter.util.File2Base64;
import com.homepainter.util.RedisUtil;
import com.qcloud.cos.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.homepainter.service.Upload_File.putObject;

@RestController
@RequestMapping("/house")
public class HouseController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    RedisUtil redisUtil;

    @PostMapping("/create")
    public Map<String, Object> create(@RequestHeader String token, @RequestBody Map<String, Object> data) throws IOException {
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        String houseInfo = (String) data.get("house_json");

        String querySql = "select familyId from family where userId = " + userId;
        int familyId;

        if (jdbcTemplate.queryForList(querySql).isEmpty()){
            String querySql2 = "select username from `user` where userId = " + userId;
            Map<String, Object> tmp = jdbcTemplate.queryForMap(querySql2);
            String username = (String) tmp.get("username");

            String sql = "insert into family values(?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, null, username + "的小屋", userId, username, true);
        }

        Map<String, Object> res = jdbcTemplate.queryForMap(querySql);
        familyId = (int) res.get("familyId");


        String image = (String) data.get("picture");
        File f = File2Base64.Base64ToFile(image);
        PutObjectResult putObjectResult = putObject(familyId + ".jpg", f,"House/");
        String insertSql = "insert into house values (?, ?, ?, ?, ?)";
        String image_url = "https://image-1304455659.cos.ap-nanjing.myqcloud.com/House/" + familyId + ".jpg";

        Date date = new Date();

        if (jdbcTemplate.update(insertSql, null, familyId, image_url, houseInfo, date) == 1){
            map.put("code", 0);
            map.put("msg", "创建成功");
        }
        else {
            map.put("code", 1);
            map.put("msg", "创建失败");
        }

        return map;
    }

    @PostMapping("/change")
    public Map<String, Object> update(@RequestHeader String token, @RequestBody Map<String, Object> data) throws IOException {
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        String houseInfo = (String) data.get("house_json");

        String querySql = "select familyId from family where userId = " + userId;
        int familyId;


        Map<String, Object> res = jdbcTemplate.queryForMap(querySql);
        familyId = (int) res.get("familyId");


        String image = (String) data.get("picture");
        File f = File2Base64.Base64ToFile(image);
        PutObjectResult putObjectResult = putObject(familyId + ".jpg", f,"House/");
        Date date = new Date();
        String updateSql = "update house set houseInfo = ? , time = ? where familyId = " + familyId;

        if (jdbcTemplate.update(updateSql, houseInfo, date) == 1){
            map.put("code", 0);
            map.put("msg", "修改成功");
        }
        else {
            map.put("code", 1);
            map.put("msg", "修改失败");
        }

        return map;
    }


    @GetMapping("get")
    public Map<String, Object> get(@RequestHeader String token){
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        String sql = "select familyId from family where userId = " + userId;
        Map<String, Object> res = jdbcTemplate.queryForMap(sql);
        int familyId = (int) res.get("familyId");

        String sql2 = "select * from house where familyId = " + familyId;

        map.put("data", jdbcTemplate.queryForMap(sql2));
        map.put("code", 0);
        map.put("msg", "查询成功！");

        return map;
    }

    @PostMapping("delete")
    public Map<String, Object> delete(@RequestHeader String token) {
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        String sql = "select familyId from family where userId = " + userId;
        Map<String, Object> res = jdbcTemplate.queryForMap(sql);
        int familyId = (int) res.get("familyId");

        String delete = "delete from house where familyId = " + familyId;
        if (jdbcTemplate.update(delete) == 1){
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
