package com.homepainter.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestBehave{

    @Autowired
    BehaveService behaveService;
    @Test
    public void behave(){
       behaveService.initGoods(105);
       behaveService.initGoods(1);
    }
}
