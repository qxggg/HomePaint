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
import java.util.Map;

@SpringBootTest
public class TestData {

    @Autowired
    GetGoods getGoods;

    @Autowired
    CreateHouseService createHouseService;

    @Test
    public void test(){
        Map<String, Object> datas = HouseDataController.GetUserHouse(232);
        JSONObject data = (JSONObject) JSONObject.parse(datas.toString());

        JSONObject data1 =  data.getJSONObject("data");
        JSONObject data2 = (JSONObject) data1.get("DWW");

        JSONArray doorList = (JSONArray) data2.get("Doors");
        JSONArray doorPoints = (JSONArray) data2.get("DoorPoints");

        JSONArray wallList = (JSONArray) data2.get("Walls");
        JSONArray wallPoints = (JSONArray) data2.get("WallPoints");

        JSONArray windowList = (JSONArray) data2.get("Windows");
        JSONArray windowPoints = (JSONArray) data2.get("WindowPoints");
//
        String style = (String) data2.get("style");
        JSONObject house = (JSONObject) data1.get("house");

        JSONArray rooms = (JSONArray) house.get("Room");

        JSONArray remove = (JSONArray) data1.get("remove");
        JSONArray j = new JSONArray();

        JSONArray jp = createHouseService.createHouse(232, 3.3, 5.5, rooms);

        }



    public void createMainRoom(JSONObject room, int userId){


    }
}
