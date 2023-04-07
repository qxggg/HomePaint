package com.homepainter.controller;

import com.homepainter.pojo.Address;
import com.homepainter.pojo.Order;
import com.homepainter.service.OrderService;
import com.homepainter.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    RedisUtil redisUtil;

    @PostMapping
    public Map<String, Object> test(@RequestBody Map<String, Object> data){
        Map<String, Object> map = new HashMap<>();
        String a = (String) data.get("token");
        System.out.println("token");
        map.put("code", 2);
        return map;
    }

    @GetMapping("/address_list")
    public Map<String, Object> getAllAddress(@RequestHeader String token){
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        System.out.println(token);
        int userId = Integer.parseInt(id.substring(5));
        map.put("data", orderService.getAddressById(userId));
        map.put("code", 0);
        map.put("msg", "查询地址成功！");
        return map;
    }

    @PostMapping("/add_address")
    public Map<String, Object> insertAddress(@RequestBody Map<String, Object> data, @RequestHeader String token){
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        String province = (String) data.get("address_city");
        String address = (String) data.get("address");
        String phone = (String) data.get("phone");
        String nickname = (String) data.get("nickname");
        boolean is_default = (boolean) data.get("isdefault");
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        Address ad = new Address(province, address, phone, nickname, userId, is_default);
        if (orderService.insertAddress(ad) == 1){
            map.put("code", 0);
            map.put("msg", "新建地址成功！");
        }
        return map;
    }

    @PostMapping("/update_address")
    public Map<String, Object> updateAddress(@RequestBody Map<String, Object> data, @RequestHeader String token) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        int addressId = (int) data.get("address_id");
        String province = (String) data.get("address_city");
        String city = (String) data.get("city");
        String county = (String) data.get("county");
        String address = (String) data.get("address");
        String phone = (String) data.get("phone");
        String nickname = (String) data.get("nickname");
        boolean isDefault = (boolean) data.get("isdefault");
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        Address ad = new Address(addressId, province, address, phone, nickname, userId, isDefault);
        if (orderService.updateAddress(ad) == 1){
            map.put("code", 0);
            map.put("msg", "更新地址成功！");
        }
        return map;
    }

    @PostMapping("/delete")
    public Map<String, Object> deleteAddress(@RequestBody Map<String, Object> data){
        int addressId = (int) data.get("address_id");
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        if (orderService.deleteAddress(addressId) == 1){
            map.put("code", 0);
            map.put("msg", "删除地址成功！");
        }
        return map;
    }

    @PostMapping("/Post_list")
    public Map<String, Object> getOrderList(@RequestHeader String token){
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        map.put("data", orderService.getOrderById(userId));
        map.put("code", 0);
        map.put("msg", "查询订单列表成功！");
        return map;
    }

    @PostMapping("/Post_detail")
    public Map<String, Object> getOrderDetail(@RequestBody Map<String, Object> data){
        Map<String, Object> map = new HashMap<>();
        map.put("data", orderService.getOrderDetail((int) data.get("order_id")));
        map.put("code", 0);
        map.put("msg", "查询订单列表成功！");
        return map;
    }

    @PostMapping("/add")
    public Map<String, Object> addOrder(@RequestBody Map<String, Object> data, @RequestHeader String token){
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        int addressId = (int) data.get("address_id");
        int count = (int) data.get("count");
        int goods_id = (int) data.get("goods_id");
        String yunfeiS = (String) data.get("yunfei");
        String AllPriceS = (String) data.get("AllPrice");
        double yunfei = Double.parseDouble(yunfeiS);
        double AllPrice = Double.parseDouble(AllPriceS);
        Date date = new Date();
        if (orderService.insertOrder(new Order(date, userId, addressId, goods_id, count, yunfei, AllPrice, "待支付")) == 1){
            map.put("code", 0);
            map.put("msg", "下单成功");
        }
        else{
            map.put("code", 1);
            map.put("msg", "下单失败");
        }
        return map;
    }

}
