package com.homepainter.controller;

import com.homepainter.service.DataControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    DataControlService dataControlService;

    @GetMapping
    public Map<String, Object> getAllData(){
        Map<String, Object> map = new HashMap<>();
        map.put("data", dataControlService.styleService());
        return map;
    }

    @GetMapping("/goods")
    public Map<String, Object> getAllGoods(){
        Map<String, Object> map = new HashMap<>();
        map.put("data", dataControlService.goodsService());
        return map;
    }

}
