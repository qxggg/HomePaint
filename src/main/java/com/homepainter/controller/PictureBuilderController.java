package com.homepainter.controller;

import com.homepainter.service.PictureBuilder;
import com.homepainter.service.UserFurnitureService;
import com.homepainter.util.File2Base64;
import com.homepainter.util.RedisUtil;
import com.qcloud.cos.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Watchable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Module")
public class PictureBuilderController {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UserFurnitureService userFurnitureService;

    @Autowired
    PictureBuilder pictureBuilder;
    @GetMapping("/list")
    public Map<String, Object> getList(@RequestHeader String token) throws IOException {
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        map.put("code", 0);
        map.put("msg", "查询成功");
        map.put("data", pictureBuilder.getList(userId));
        return map;
    }

    @PostMapping("/CreateMoudle")
    public Map<String, Object> pictureBuilderController(@RequestBody Map<String, Object> data, @RequestHeader String token) throws IOException, InterruptedException {
        Map<String, Object> map = new HashMap<>();

        String fullname = "upload";

        String os = System.getProperty("os.name").toLowerCase();

        if (os.indexOf("linux") != -1) fullname = "/www/wwwroot/" + fullname;

        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        String projectName = (String) data.get("name");
        List<String> httpUrl = (List<String>) data.get("images");


        String fp_id = pictureBuilder.up(httpUrl, projectName, userId, "5", "2", "0.7");
        pictureBuilder.deleteFolder(new File("upload/" + fp_id));
        Date date = new Date();
        map.put("code", 0);
        map.put("msg", "模型上传成功，图片数据");
        map.put("fp_id", fp_id);
        map.put("date", date);
        map.put("imageUrl", "https://image-1304455659.cos.ap-nanjing.myqcloud.com/images/"+ fp_id + ".jpg");
        return map;
    }

    @PostMapping("/download")
    public Map<String, Object> pictureBuilderDownload(@RequestBody Map<String, Object> data, @RequestHeader String token) throws IOException {
        Map<String, Object> map = new HashMap<>();
        String fp_id = (String) data.get("fp_id");
        String id =(String) redisUtil.get(token);
        String projectName = (String) data.get("name");
        int userId = Integer.parseInt(id.substring(5));
        Map<String,Object> temp = pictureBuilder.down(fp_id, "obj", userId, projectName);
        map.put("data",temp);
        map.put("code", 0);
        map.put("msg", "下载成功");
        return map;
    }

    @PostMapping("/CreateMoudleVideo")
    public Map<String, Object> pictureBuilderVideo(@RequestHeader String token, @RequestParam MultipartFile video, @RequestParam String projectName) throws IOException, InterruptedException {
        Map<String, Object> map = new HashMap<>();
        String fullname = "upload";
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("linux") != -1) fullname = "/www/wwwroot/" + fullname;
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        String fp_id = pictureBuilder.upVideo(video, projectName, userId, "5", "1", "0.7");

        map.put("code", 0);
        map.put("msg", "上传成功");
        map.put("fp_id", fp_id);

        return map;
    }


    @PostMapping("/CreateModuleVideoCover")
    public Map<String, Object> pictureBuilderVideoCover(@RequestBody Map<String, Object> data) throws IOException {
        Map<String, Object> map = new HashMap<>();
        String fp_id = (String) data.get("fp_id");
        String image = (String) data.get("image");
        File picture = File2Base64.Base64ToFile(image);
        pictureBuilder.upVideoCover(picture, fp_id);

        map.put("code", 0);
        map.put("msg", "上传封面成功");
        map.put("fp_id", fp_id);
        return map;
    }
}
