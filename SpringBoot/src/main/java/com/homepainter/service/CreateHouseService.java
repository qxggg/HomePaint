package com.homepainter.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.util.RuleUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CreateHouseService {
    public void createMainRoom(JSONArray index, JSONObject room, JSONArray remove){
        List<HashMap<String, Object>> goods = new ArrayList<>();
        JSONArray walls = RuleUtils.roomHandler(room, remove);


    }
}
