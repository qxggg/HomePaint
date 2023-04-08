package com.homepainter.controller;

import com.homepainter.service.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("algorithm")
public class AlgorithmController {

    public static String commentInfo;

    @Autowired
    Algorithm algorithm;

    @GetMapping("comment")
    public Map<String, Object> comment(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("comment", commentInfo);
        return map;
    }


}
