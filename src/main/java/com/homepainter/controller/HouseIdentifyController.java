package com.homepainter.controller;

import com.alibaba.fastjson.JSONObject;
import com.homepainter.service.HouseIdentify;
import com.homepainter.service.SpliteHouseService;
import com.homepainter.util.HouseIdentifyHandler;
import com.qcloud.cos.model.PutObjectResult;
import org.springframework.web.bind.annotation.*;


import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static com.homepainter.service.Upload_File.putObject;
import static com.homepainter.util.File2Base64.Base64ToFile;


@RestController
@RequestMapping("/identify")
public class HouseIdentifyController {

    /**
     * 户型图识别算法
     * @param data
     * @return
     * @throws Exception
     */
    @PostMapping("/upload")
    public Object houseIdentify(@RequestBody Map<String,Object> data) throws Exception {
        // 结果数据集
        JSONObject res = new JSONObject();
        res.put("code", 1);
        res.put("msg", "API调用报错");
        // url和base64分类
        boolean use_imageurl = false;
        String image = "";
        if(data.get("image_url") != null){
            use_imageurl = true;
            image = data.get("image_url").toString();
        }else{
            use_imageurl = false;
            image = data.get("image").toString();
        }

        File file_res = Base64ToFile(image);
        String filename =  System.currentTimeMillis() + "model.jpg";
        PutObjectResult putObjectResult = putObject(filename, file_res, "module/");
        String Imageurl = "https://image-1304455659.cos.ap-nanjing.myqcloud.com/module/" + filename;
        JSONObject r = HouseIdentify.houseIdentify(Imageurl);

        SpliteHouseService spliteHouse = new SpliteHouseService();
        Map<String,Object> house = spliteHouse.SpliteHouseController(r);

        res.put("house",house);
        res.put("data", r);
        res.put("code", 0);
        res.put("msg", "户型识别检索成功");

        return HouseIdentifyHandler.getResult(res);
    }

    @GetMapping("")
    public Object haha() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("C:\\Users\\25697\\Desktop\\第十六届全国大学生软件创新大赛\\HomePaint\\json.json")));
        JSONObject j = (JSONObject) JSONObject.parse(content);
        return HouseIdentifyHandler.getResult(j);
    }
}
