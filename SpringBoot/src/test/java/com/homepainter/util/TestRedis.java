package com.homepainter.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.pojo.Tieba;
import com.homepainter.service.CommunityService;
import com.homepainter.service.GetGoods;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

@SpringBootTest
public class TestRedis {

    @Autowired
    CommunityService communityService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    GetGoods getGoods;

    @Test
    public void testRedis(){
        List<Integer> insert = new ArrayList<>();
        for (int i = 0; i < 2000; ++i) insert.add(0);
        Map<String, Object> map = new HashMap<>();
        map.put("RandomConsume", insert);
        map.put("RandomView", insert);
        redisUtil.set("4Behave", map);
        map = (Map<String, Object>) redisUtil.get("4Behave");
        System.out.println(map);
    }

    public static double generate() {
        double[] numbers = {29.9, 39.9, 52, 66.6, 46.9, 99.9, 70.9, 15, 168.0};
        Random random = new Random();
        int index = random.nextInt(numbers.length);
        return numbers[index];
    }
    @Test
    public void insertFloor(){

        String sql = "insert into floors values(?, ?, ?)";
        for (int i = 0; i < 1016; ++i){
            double number = generate();
            jdbcTemplate.update(sql, i, "https://image-1304455659.cos.ap-nanjing.myqcloud.com/DiBan/" + i + ".jpg", number);
        }
    }

    @Test
    public void insertWallPaint(){
        String sql = "insert into wallpaint values(?, ?, ?)";
        for (int i = 0; i < 801; ++i){
            double number = generate();
            jdbcTemplate.update(sql, i, "https://image-1304455659.cos.ap-nanjing.myqcloud.com/BiZhi/" + i + ".jpg", number);
        }
    }

    @Test
    public void insertModalId(){
        Set<String> keys = (Set<String>) redisUtil.getAllKeys();
        for (String key : keys) {
            String a = key.substring(0, 5);
            String b = key.substring(5, key.length());
            if (a.equals("GOODS")) {
                JSONObject j = (JSONObject) redisUtil.get(key);
                j.put("imageURL", "https://image-1304455659.cos.ap-nanjing.myqcloud.com/3D-FUTURE-model-part1/" + b + "/image.jpg");
                redisUtil.set(key, j);
            }
        }
    }

    @Test
    public void selectFloor(){
//        String sql = "select * from floors";
//        List<Map<String, Object>> floorList = jdbcTemplate.queryForList(sql);
//        redisUtil.set("floorList", floorList);
//
//        String sql2 = "select * from wallpaint";
//        List<Map<String, Object>> wallpaintList = jdbcTemplate.queryForList(sql2);;
//        redisUtil.set("wallpaintList", wallpaintList);
//        System.out.println(redisUtil.get("wallpaintList"));
//
//        System.out.println(getGoods.changeFloorStyle("东南亚"));

        Map<String, Object> map = new HashMap();
        List<Tieba> tiebas = communityService.getTiebaList();
        JSONArray array = new JSONArray();

        for (Tieba tieba : tiebas) {
            tieba.getUser().setPassword(null);
            int id = tieba.getTiebaId();
            String sql = "select * from tiebagoods inner join" +
                    " goods on tiebagoods.goodsId = goods.goodsId where tiebagoods.tiebaId = " + id;

            JSONObject j = new JSONObject();
            j.put("tiebaId", tieba.getTiebaId());
            j.put("user", tieba.getUser());
            j.put("tiebaFlages", tieba.getTiebaFlags());
            j.put("tiebaImage", tieba.getTiebaImage());
            j.put("title", tieba.getTitle());
            j.put("time", tieba.getTime());
            j.put("content", tieba.getContent());
            j.put("favorites", tieba.getFavorites());
            j.put("collect", tieba.getCollect());
            j.put("goodsInfo",  jdbcTemplate.queryForList(sql));


            array.add(j);

        }

        System.out.println(array);
        redisUtil.set("communityInfo", array.toJSONString());
    }
}
