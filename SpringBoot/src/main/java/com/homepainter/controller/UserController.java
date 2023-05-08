package com.homepainter.controller;

import com.homepainter.pojo.User;
import com.homepainter.service.BehaveService;
import com.homepainter.service.UltraGCN;
import com.homepainter.service.UserService;
import com.homepainter.util.RedisUtil;
import com.homepainter.util.TokenUtil;
import com.homepainter.util.getStyleUtils;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private BehaveService behaveService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    UltraGCN ultraGCN;

    @GetMapping("/error")
    public Map<String, Object> error(){
        Map<String, Object> map = new HashMap<>();
        map.put("code", "3");
        map.put("msg", "token过期，请重新登录！");
        return map;
    }

    @GetMapping("/test")
    public Map<String, Object> hello (@RequestBody Map<String, Object> data){
        Map<String, Object> map = new HashMap<>();
        map.put("data", "接口联通成功");
        return map;
    }
    @PostMapping("/login")
    public Map<String, Object> loginByPass(@RequestBody Map<String, Object> data){
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        String telephone = (String) data.get("telephone");
        String password = (String) data.get("password");
        if (StringUtils.isEmptyOrWhitespaceOnly(telephone) || StringUtils.isEmptyOrWhitespaceOnly(password)){
            map.put("msg", "手机号或密码为空！");
            return map;
        }
        if (userService.getPassByTelephone(telephone).equals(password)){
            String token = TokenUtil.generateToken(new User(telephone, password));
            map.put("code", 0);
            map.put("token", token);
            map.put("msg", "登陆成功！");
            redisUtil.set(token, "token" + userService.getIdByTel(telephone));
            redisUtil.expire(token, 1540000);
        }

        else map.put("msg", "手机号或密码错误");
        return map;
    }

    @PostMapping("/smsLogin")
    public Map<String, Object> loginBySMS(@RequestBody Map<String, Object> data){
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        String telephone = (String) data.get("telephone");
        String verifyCode = (String) data.get("verifyCode");
        if (!redisUtil.get("tel" + telephone).equals(verifyCode)) {map.put("msg", "验证码错误！"); return map;}
        else{
            map.put("code", 0);
            map.put("msg", "登陆成功！");
            String token = TokenUtil.generateToken(new User(telephone));
            map.put("token", token);
            redisUtil.set(token, "token" + userService.getIdByTel(telephone));
            redisUtil.expire(token, 1540000);
        }
        return map;
    }

    @PostMapping("/loginSendCode")
    public Map<String, Object> sendCode(@RequestBody Map<String, Object> data){
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        if (userService.ifExistsTel((String) data.get("telephone"))) {map.put("code", 1); map.put("msg", "该手机号未注册！"); return map;}
        String a = (String) data.get("telephone");
        if (a.equals("12345678901")) {map.put("code", 1); map.put("msg", "该号是测试用号！验证码为888888"); return map;}
        userService.sendCode((String) data.get("telephone"));
        map.put("code", 0);
        map.put("msg", "发送成功");

        return map;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, Object> data){
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        String telephone = (String) data.get("telephone");
        String verifyCode = (String) data.get("verifyCode");
        String username = (String) data.get("username");
        String password = (String) data.get("password");
        List<String> styles = (List<String>) data.get("style");
        boolean HaveHouse = (boolean) data.get("HaveHouse");
        String sql = "insert into style values(?, ?)";
        if (verifyCode.isEmpty()){
            map.put("code", 1);
            map.put("msg", "请先发送验证码！");
            return map;
        }
        String avatar = "https://pic3.zhimg.com/v2-4c19b7311c007e3aa94ca67a340ac4b6_b.jpg";

        System.out.println(telephone);
        if (redisUtil.hasKey("tel" + telephone)&&redisUtil.get("tel" + telephone).equals(verifyCode)) {
            // 验证码正确
            User user = new User(username, password, telephone, avatar, HaveHouse);
            if (userService.insertUser(user) == true) {
                map.put("code", 0);
                map.put("msg", "注册成功！");
                int id = userService.getIdByTel(telephone);
                behaveService.initStyle(id);
                behaveService.initGoods(id);
                for (String style : styles)
                    jdbcTemplate.update(sql, id, getStyleUtils.getId(style));
                String token = TokenUtil.generateToken(new User(telephone));
                map.put("token", token);
                redisUtil.set(token, "token" + userService.getIdByTel(telephone));
                redisUtil.expire(token, 1540000);

                // 添加到待训练列表
                ultraGCN.AddUser(id);
            } else map.put("msg", "网络错误！");
        }
        else map.put("msg", "验证码错误！");
        return map;
    }

    @PostMapping("/registerSendCode")
    public Map<String, Object> registerSendCode(@RequestBody Map<String, Object> data){
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        if (!userService.ifExistsTel((String) data.get("telephone"))){ map.put("msg", "该手机已注册！"); map.put("code", 1); return map;}
        else {
            userService.sendCode((String) data.get("telephone"));
            map.put("code", 0);
            map.put("msg", "发送成功");
        }
        return map;
    }

}
