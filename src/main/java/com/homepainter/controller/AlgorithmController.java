package com.homepainter.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("algorithm")
public class AlgorithmController {

    @GetMapping("comment")
    public Map<String, Object> comment(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("comment", "你好坏哦！我好喜欢");
        return map;
    }
}
