package com.homepainter.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.controller.HouseDataController;
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
    @Test
    public void test(){
        JSONObject data = (JSONObject) JSONObject.parse(HouseDataController.GetUserHouse(232).toString());
        JSONObject room = data.getJSONObject("data").getJSONObject("house").getJSONArray("Room").getJSONObject(1);
        System.out.println(room);
        JSONArray remove = data.getJSONObject("data").getJSONObject("DWW").getJSONArray("remove");

        System.out.println();
        JSONArray wall = RuleUtils.roomHandler(room, remove);
        String json = JSON.toJSONString(getGoods.GetGoods_ByStyle_LianXiang_Category("日式", "L形沙发"));
        JSONArray jsonArray = JSONArray.parseArray(json);

        for (int i = 1; i < 2; ++i){
            JSONObject j = new JSONObject();
            j = jsonArray.getJSONObject(i);
            System.out.println(j);
            JSONArray index = j.getJSONArray("vertexs");
            int goodsId = Integer.parseInt(j.getString("goodsId"));
            JSONObject center = j.getJSONObject("Center");
            System.out.println(index);

            RuleUtils.insertWallGoods(index, wall.getJSONObject(0), center);


        }

    }

    public void createMainRoom(JSONObject room, int userId){


    }
}
