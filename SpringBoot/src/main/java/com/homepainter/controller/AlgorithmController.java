package com.homepainter.controller;

import com.homepainter.service.Algorithm;
import com.homepainter.service.UserService;
import com.homepainter.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.incrementer.HsqlMaxValueIncrementer;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("algorithm")
public class AlgorithmController {

    public static String commentInfo;


    @Autowired
    Algorithm algorithm;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("comment")
    public Object comment(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("comment", commentInfo);
        return map;
    }

    @GetMapping("goodsBehave")
    public Object goodsBehave(){
        HashMap<String, Object> map = new HashMap<>();
        String sql = "select userId from user order by userId desc limit 1";
        List<Object> list = new ArrayList<>();
        Map<String, Object> m = jdbcTemplate.queryForMap(sql);
        int userId = (int) m.get("userId");
        for (int i = 1; i <= userId; ++i)
            list.add(redisUtil.get("goodsBehave" + i));
        return list;
    }

    @GetMapping("styleBehave")
    public Object styleBehave(){
        String sql = "select userId from user order by userId desc limit 1";
        List<Object> list = new ArrayList<>();
        Map<String, Object> m = jdbcTemplate.queryForMap(sql);
        int userId = (int) m.get("userId");
        for (int i = 1; i <= userId; ++i)
            list.add(redisUtil.get("styleBehave" + i));
        return list;
    }


}
