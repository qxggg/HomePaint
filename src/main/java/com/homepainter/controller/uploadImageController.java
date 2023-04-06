package com.homepainter.controller;

import com.qcloud.cos.model.PutObjectResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.homepainter.service.Upload_File.putObject;
import static com.homepainter.util.File2Base64.Base64ToFile;

@RestController
public class uploadImageController {

    @PostMapping("/upload_image")
    public Map<String,Object> upload_image(@RequestBody Map<String,Object> data) throws IOException {
        Map<String,Object> res = new HashMap<>();
        if(data.get("image")==null){
            res.put("code",2);
            res.put("msg","没有图片信息");
            return res;
        }

        File file = Base64ToFile(data.get("image").toString());
        String filename = new Date().getTime() + ".jpg";
        PutObjectResult putObjectResult = putObject(filename,file,"images/");
        if(putObjectResult.getETag() == null){
            res.put("code", 1);
            res.put("msg", "图片上传错误");
            return res;
        }
        String Imageurl = "https://image-1304455659.cos.ap-nanjing.myqcloud.com/images/"+filename;
        res.put("code",0);
        res.put("data",Imageurl);
        res.put("msg","上传成功");
        return res;
    }
}
