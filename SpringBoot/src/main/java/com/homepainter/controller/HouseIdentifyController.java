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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;


import javax.xml.bind.annotation.W3CDomHandler;
import java.io.File;
import java.io.IOException;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.homepainter.service.Upload_File.putObject;
import static com.homepainter.util.File2Base64.Base64ToFile;
import static com.homepainter.util.HouseIdentifyHandler.toDouble;
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
        Map<String,Object> house = spliteHouse.SpliteHouseController(IdentifyResult,userId);
        data.put("house",house);


        res.put("data", data);
        res.put("code", 0);
        res.put("msg", "户型识别检索成功");

        //给房间绑定门窗 //给room打id
        try{
            JSONArray rooms = RuleUtils.findDoorWindow((JSONObject) JSONObject.parse(res.toJSONString()));
             house = (HashMap<String, Object>) data.get("house");
            JSONObject furniture = new JSONObject();
            data.put("furniture", furniture);
            JSONArray floor = new JSONArray();
            JSONArray door = new JSONArray();
            JSONArray wallPaper = new JSONArray();
            JSONArray light = new JSONArray();
            JSONArray defaultFloors = new JSONArray();
            JSONArray defaultWallpaints = new JSONArray();
            JSONArray defaultLights = new JSONArray();
            for (int i = 0; i < rooms.size(); ++i) {
                JSONObject defaultFloor = new JSONObject();
                JSONObject defaultWallpaint = new JSONObject();
                JSONObject defaultLight = new JSONObject();
                JSONObject room = rooms.getJSONObject(i);
                defaultWallpaint.put("id", 780);
                defaultWallpaint.put("imageURL", "https://image-1304455659.cos.ap-nanjing.myqcloud.com/BiZhi/780.jpg");
                defaultWallpaint.put("roomId", i);
                defaultWallpaint.put("price", 15);
                defaultWallpaints.add(defaultWallpaint);
                defaultFloor.put("id", 6);
                defaultFloor.put("imageURL", "https://image-1304455659.cos.ap-nanjing.myqcloud.com/DiBan/6.jpg");
                defaultFloor.put("roomId", i);
                defaultFloor.put("price", 299.9);

                defaultLight.put("roomId", i);
                if (room.getString("name").equals("洗漱间") || room.getString("name").equals("卫生间")){
                    defaultLight.put("modalId", "0f4227d2-88a2-4c39-bfd1-6d8603c9f0eb");
                    defaultLight.put("goodsId", 881);
                    defaultLight.put("price", 1000);
                }
                else if (room.getString("name").equals("阳台") || room.getString("name").equals("厨房")){
                    defaultLight.put("modalId", "1bc7ff20-2031-44e9-91a9-4dd7bcf1b679");
                    defaultLight.put("goodsId", 1602);
                    defaultLight.put("price", 1000);
                }
                else if (room.getString("name").equals("客厅")){
                    defaultLight.put("modalId", "38ea2314-1803-442e-9add-ace97d2959a2");
                    defaultLight.put("goodsId", 141);
                    defaultLight.put("price", 1000);
                }
                else{
                    defaultLight.put("modalId", "3f546069-dc34-425c-87d0-f1cc1f858a5c");
                    defaultLight.put("goodsId", 1345);
                    defaultLight.put("price", 1000);
                    defaultLight.put("center", room.getJSONObject("center"));
                }
                rooms.getJSONObject(i).put("roomId", i);


                defaultFloors.add(defaultFloor);
           //     defaultWallpaints.add(defaultWallpaint);
                defaultLights.add(defaultLight);
            }
            furniture.put("floor", defaultFloors);
            furniture.put("WallPaper", defaultWallpaints);
            furniture.put("goods", new ArrayList());
            furniture.put("light", defaultLights);

            house.put("Room", rooms);
        }catch (Exception e){
            res.put("code", 24);
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
