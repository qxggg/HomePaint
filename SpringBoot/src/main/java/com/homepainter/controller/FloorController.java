package com.homepainter.controller;

import com.homepainter.service.CreateHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/floor")
public class FloorController {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @GetMapping("/getFloor")
    public Map<String, Object> getFloor(){
        Map<String, Object> map = new HashMap<>();
        map.put("data", jdbcTemplate.queryForList("select * from floors limit " + CreateHouseService.generateRandomNumber(990) + ", 20"));
        map.put("code", 0);
        map.put("msg", "地板查询成功");
        return map;
    }

    @GetMapping("/getWallpaint")
    public Map<String, Object> getWallpaint(){
        Map<String, Object> map = new HashMap<>();
        map.put("data", jdbcTemplate.queryForList("select * from wallpaint limit " + CreateHouseService.generateRandomNumber(770) + ", 20"));
        map.put("code", 0);
        map.put("msg", "墙纸查询成功");
        return map;
    }

}
