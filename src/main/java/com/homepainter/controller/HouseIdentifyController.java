package com.homepainter.controller;

import com.homepainter.service.HouseIdentify;
import com.homepainter.util.File2Base64;
import com.qcloud.cos.model.PutObjectResult;
import org.springframework.web.bind.annotation.*;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.homepainter.service.Upload_File.putObject;
import static com.homepainter.util.File2Base64.Base64ToFile;


@RestController
@RequestMapping("/identify")
public class HouseIdentifyController {

    @PostMapping("/upload")
    public Map<String,Object> houseIdentify(@RequestBody Map<String,Object> data) throws IOException {
        // 结果数据集
        Map<String,Object> res = new HashMap<>();
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
        String r = HouseIdentify.houseIdentify(Imageurl);

        res.put("data", r);
        res.put("code", 0);
        res.put("msg", "户型识别检索成功");

        return res;
    }
}
