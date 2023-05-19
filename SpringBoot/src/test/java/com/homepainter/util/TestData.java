package com.homepainter.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.controller.HouseDataController;
import com.homepainter.service.CreateHouseService;
import com.homepainter.service.GetGoods;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
public class TestData {

    @Autowired
    GetGoods getGoods;

    @Autowired
    CreateHouseService createHouseService;
    @Test
    public void test(){
        JSONArray j = new JSONArray();
        createHouseService.find(j, "日式", "单人床");
        System.out.println(j);
        }


    public void createMainRoom(JSONObject room, int userId){


    }
}
