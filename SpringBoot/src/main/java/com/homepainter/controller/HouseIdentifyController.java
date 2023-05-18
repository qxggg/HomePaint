package com.homepainter.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.service.HouseIdentify;
import com.homepainter.service.SpliteHouseService;
import com.homepainter.util.HouseIdentifyHandler;
import com.homepainter.util.RedisUtil;
import com.homepainter.util.RuleUtils;
import com.qcloud.cos.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.homepainter.service.Upload_File.putObject;
import static com.homepainter.util.File2Base64.Base64ToFile;
import static com.homepainter.util.ImageToBase64.getImageAsBase64;


@RestController
@RequestMapping("/identify")
public class HouseIdentifyController {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    SpliteHouseService spliteHouse;

    /**
     * 户型图识别算法
     * @param input
     * @return
     * @throws Exception
     */
    @PostMapping("/upload")
    public Map<String,Object> houseIdentify(@RequestBody Map<String,Object> input, @RequestHeader String token) throws Exception {
        // 获取userId
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        // 结果数据集
        JSONObject res = new JSONObject();
        res.put("code", 1);
        res.put("msg", "API调用报错");
        // url和base64分类
        String image = "";
        if(input.get("image_url") != null){
            image = getImageAsBase64(input.get("image_url").toString());
        }else{
            image = input.get("image").toString();
        }

        Map<String,Object> data = new HashMap<>();

        // 图片base64转换并上传
        File file_res = Base64ToFile(image);
        String filename =  System.currentTimeMillis() + "model.jpg";
        PutObjectResult putObjectResult = putObject(filename, file_res, "module/");
        String Imageurl = "https://image-1304455659.cos.ap-nanjing.myqcloud.com/module/" + filename;
        // 获取分房结果
        Map<String,Object> IdentifyResult = HouseIdentify.houseIdentify(Imageurl);
        // 原始数据
        Map<String,Object> origin = new HashMap<>();
        try {
            origin = (Map<String, Object>) IdentifyResult.get("data");
            data.put("origin",origin);
        }catch (Exception e){
            res.put("code",20);
            res.put("Exception",e);
            res.put("msg","户型图识别失败");
            return res;
        }

        // 抠门扣窗
        try{
            data.put("DWW",HouseIdentifyHandler.getResult(JSON.parseObject( JSON.toJSONString( origin))));
        }catch (Exception e){
            res.put("code",21);
            res.put("Exception",e);
            res.put("msg","抠门扣窗失败");
            return res;
        }

        // 分房算法
        try {
            Map<String,Object> house = spliteHouse.SpliteHouseController(IdentifyResult,userId);
            data.put("house",house);
        }catch (Exception e){
            res.put("code",22);
            res.put("Exception",e);
            res.put("msg","分房算法失败");
            return res;
        }


        res.put("data", data);
        res.put("code", 0);
        res.put("msg", "户型识别检索成功");

        //给房间绑定门窗
        try{
            JSONArray rooms = RuleUtils.findDoorWindow((JSONObject) JSONObject.parse(res.toJSONString()));
            HashMap<String, Object> house = (HashMap<String, Object>) data.get("house");
            house.put("Room", rooms);
        }catch (Exception e){
            res.put("code", 23);
            res.put("Exception", e);
            res.put("msg", "绑定门窗失败");
        }


        return res;
    }

    @GetMapping("")
    public Object haha() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("C:\\Users\\25697\\Desktop\\第十六届全国大学生软件创新大赛\\HomePaint\\json.json")));
        JSONObject j = (JSONObject) JSONObject.parse(content);
        return HouseIdentifyHandler.getResult(j);
    }
}
