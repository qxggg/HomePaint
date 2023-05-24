package com.homepainter.service;

import com.homepainter.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class TestBehave{

    @Autowired
    BehaveService behaveService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Test
    public void behave(){
//       behaveService.initGoods(117);
//       behaveService.initStyle(117);
        String sql = "insert into tiebagoods values(?, ?)";

        for (int i = 0; i < 20; ++i){
            int random = CreateHouseService.generateRandomNumber(1999);
            jdbcTemplate.update(sql, random, i);
        }
    }
}
