package com.homepainter.controller;

import com.homepainter.pojo.User;
import com.homepainter.service.UserService;
import com.homepainter.util.File2Base64;
import com.homepainter.util.RedisUtil;
import com.qcloud.cos.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.homepainter.service.Upload_File.putObject;

@RestController
@RequestMapping("userdetail")
public class UserDetailController {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserService userService;

    @PostMapping("change_avatar")
    public Map<String, Object> insertAvatar(@RequestBody Map<String, Object> data, @RequestHeader String token) throws IOException {
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        String base64 = (String) data.get("image");
        File f = File2Base64.Base64ToFile(base64);
        double a = System.currentTimeMillis();
        PutObjectResult putObjectResult = putObject(a + ".jpg", f,"avatar/");
        String imageUrl = "https://image-1304455659.cos.ap-nanjing.myqcloud.com/avatar/" + a + ".jpg";
        String sql = "update user set avatar = ? where userId = ?";
        if (jdbcTemplate.update(sql, imageUrl, userId) == 1){
            map.put("code", 0);
            map.put("msg", "修改成功");
        }
        else{
            map.put("code", 1);
            map.put("msg", "修改失败");
        }

        return map;
    }

    @GetMapping("get")
    public Map<String, Object> get(@RequestHeader String token){
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        User user = userService.getAllById(userId);
        user.setPassword(null);
        map.put("code", 0);
        map.put("msg", "查询成功");
        map.put("data", user);
        return map;
    }
}
