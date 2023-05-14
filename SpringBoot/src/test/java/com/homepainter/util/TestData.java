package com.homepainter.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.service.GetGoods;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
public class TestData {

    @Autowired
    GetGoods getGoods;
    @Test
    public void test(){
        String json = JSON.toJSONString(getGoods.GetGoods_ByStyle_LianXiang_Category("日式", "L形沙发"));
        JSONArray jsonArray = JSONArray.parseArray(json);
        System.out.println(jsonArray);
        for (int i = 0; i < jsonArray.size(); ++i){
            JSONObject j = new JSONObject();
            j = jsonArray.getJSONObject(i);
            JSONArray index = j.getJSONArray("vertexs");
            int goodsId = (int) j.get("goodsId");
            JSONObject center = j.getJSONObject("Center");
            System.out.println(index);


        }

    }
}
