package com.homepainter.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.homepainter.controller.DTO.GoodsPlus;
import com.homepainter.controller.DTO.InsertGoods;
import com.homepainter.pojo.Collect;
import com.homepainter.pojo.Goods;
import com.homepainter.pojo.Goods_image;
import com.homepainter.service.*;
import com.homepainter.util.RedisUtil;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

import static com.homepainter.controller.AlgorithmController.commentInfo;
import static com.homepainter.service.Search_Service.kmpSearch;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    UserService userService;

    @Autowired
    TranslateService translateService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    Algorithm algorithm;

    @Autowired
    BehaveService behaveService;

    public static int goodsInsertCount = 1001;

    @GetMapping("/get_list")
    public Map<String, Object> getAllList(){
        Map<String, Object> map = new HashMap<>();
        List<Goods> goods = goodsService.getAllGoods();
        List<GoodsPlus> goodsPluses = new ArrayList<>();
        for (Goods good : goods){
            GoodsPlus goodsPlus = new GoodsPlus();
            goodsPlus.setGoodsId(good.getGoodsId());
            goodsPlus.setCategory(good.getCategory());
            goodsPlus.setAppraise(good.getAppraise());
            goodsPlus.setDetail(good.getDetail());
            goodsPlus.setMaterial(good.getMaterial());
            if (!good.getImageUrl().isEmpty()) goodsPlus.setImageURL(good.getImageUrl().get(0).getImageUrl());
            goodsPlus.setStorage(good.getStorage());
            goodsPlus.setPrice(good.getPrice());
            goodsPlus.setStyle(good.getStyle());
            goodsPlus.setSubtitle(good.getSubtitle());
            goodsPlus.setModalId(good.getModalId());
            goodsPlus.setTheme(good.getTheme());
            goodsPlus.setTitle(good.getTitle());
            goodsPlus.setSuperCategory(good.getSuperCategory());
            goodsPlus.setMaterial(good.getMaterial());
            goodsPluses.add(goodsPlus);
        }

        map.put("data", goodsPluses);
        map.put("code", 0);
        map.put("msg", "查询商品成功！");
        return map;
    }

    @PostMapping("/post")
    public Map<String, Object> getGoodsById(@RequestBody Map<String, Object> data, @RequestHeader String token){
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));

        if (data.get("goods_id") == null) map.put("data", goodsService.getAllGoods());

        else {
            Goods goods = goodsService.getGoodsById((int) data.get("goods_id"));
            map.put("data", goods);
            List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from collect where enumId = 'goods' and userId = " + userId + " and collectId = " + goods.getGoodsId());
            if (maps.isEmpty()) map.put("isCollect", false);
            else map.put("isCollect", true);
        }
        map.put("code", 0);
        map.put("msg", "查询商品成功！");
        return map;
    }
    @PostMapping("get_list")
    public Map<String, Object> getGoodsByContent(@RequestBody Map<String, Object> data, @RequestHeader String token) throws ClientException {
        Map<String, Object> map = new HashMap<>();

        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        if (data.get("search_content") == null) map.put("data", goodsService.getAllGoods());
        else {
            List<JSONObject> list= kmpSearch((String) data.get("search_content"));
            for (JSONObject j : list){
                String a = (String) j.get("goodsId");
                int goodsId = Integer.parseInt(a);
                System.out.println(goodsId);
                behaveService.updateAddStyle(userId, goodsId, "randomSearchClick", 1);
                System.out.println(a);
            }
            map.put("data", list);
        }

        map.put("code", 0);
        map.put("msg", "查询商品成功！");
        return map;
    }

    @PostMapping("/translate")
    public List<JSONObject> translate(@RequestBody List<JSONObject> jsonObjects) throws Exception {
        return translateService.translateJson(jsonObjects);
    }

    @PostMapping("/insertLocal")
    public Map<String, Object> insertGoods(@RequestBody List<InsertGoods> goods){
        Map<String, Object> map = new HashMap<>();

        for (InsertGoods good : goods) {
            List<Goods_image> list = new ArrayList<>();
            Goods_image goodsImage = new Goods_image(goodsInsertCount, good.getModel_id());
            list.add(goodsImage);
            if (goodsService.insertGoods(new Goods(goodsInsertCount++, "暂无标题", 1000, "暂无详情", 1000.00, good.getSuper_category(), good.getCategory(), "暂无副标题", good.getStyle(), good.getTheme(), good.getMaterial(), good.getModel_id(), list)) == 0) {
                map.put("code", 1);
                map.put("msg", "插入失败");
            }
        }
        map.put("code", 0);
        map.put("msg", "插入成功");
        for (InsertGoods good : goods)
            System.out.println(good);
      return map;
    }

    @GetMapping("/swiperList")
    public Map<String, Object> getHot(){
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "查询成功");
        map.put("data", goodsService.selectHotByType("swiperList"));
        return map;
    }

    @PostMapping("/shoucang")
    public Map<String, Object> collect(@RequestBody Map<String, Object> data,  @RequestHeader String token){
        Map<String, Object> map = new HashMap<>();
        int collectId = (int) data.get("id");
        String types = (String) data.get("types");
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        Date date = new Date();
        if (goodsService.insertCollect(new Collect(userId, types, collectId, date)) == 1){
            map.put("code", 0);
            map.put("msg", "插入成功!");
        }
        else{
            map.put("code", 1);
            map.put("msg", "插入失败");
        }
        return map;
    }

    @GetMapping("/shoucang/list")
    public Map<String, Object> getCollectList( @RequestHeader String token){
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        Map<String, Object> map = new HashMap<>();
        map.put("goods", goodsService.getCollectById(userId, "goods"));
        map.put("tieba", goodsService.getCollectById(userId, "tieba"));
        map.put("code", 0);
        map.put("msg", "查询成功！");
        return map;
    }

//    @GetMapping("/shoucang/list")
//    public Map<String,Object> shoucang_list(){
//        Map<String,Object> res  =new HashMap<>();
//        Map<String,Object> data  =new HashMap<>();
//        List<Map<String, Object>> goods = jdbcTemplate.queryForList("select * from collect");
//        List<Map<String, Object>> tiezi = jdbcTemplate.queryForList("select * from tieba");
//        data.put("goods",goods);
//        data.put("tiezi",tiezi);
//
//        res.put("data",data);
//        res.put("code",0);
//        res.put("msg","成功！");
//        return res;
//    }
    @GetMapping("/shoucang")
    public Map<String, Object> getCollect(){
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "查询成功");
        map.put("data", goodsService.getAllCollect());
        return map;
    }
    @PostMapping("/unshoucang")
    public Map<String, Object> UnCollect(@RequestBody Map<String, Object> data,  @RequestHeader String token){
        Map<String, Object> map = new HashMap<>();
        int collectId = (int) data.get("id");
        String types = (String) data.get("types");
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        if (goodsService.deleteCollect(userId, types, collectId) == 1){
            map.put("code", 0);
            map.put("msg", "删除成功");
        }
        else {
            map.put("code", 1);
            map.put("msg", "删除失败");
        }
        return map;
    }

    @PostMapping("/Look_time")
    public Map<String, Object> view(@RequestBody Map<String, Object> data,  @RequestHeader String token){
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        int goodsId = (int)data.get("id");
        int viewTime = (int)data.get("time");
        goodsService.insertView(userId, goodsId, viewTime);
        return map;
    }

    @PostMapping("evaluate")
    public Map<String, Object> insertAppraise(@RequestBody Map<String, Object> data, @RequestHeader String token) throws IOException {
        Map<String, Object> map = new HashMap<>();
        String id =(String) redisUtil.get(token);
        int userId = Integer.parseInt(id.substring(5));
        int goods_id = (int) data.get("goods_id");
        String appraise = (String) data.get("comment");
        String sql = "insert into goods_appraise values(?, ?, ?, ?, ?)";
        Date date = new Date();
        if (jdbcTemplate.update(sql, null, goods_id, appraise, userId, date) == 1){
            map.put("code", 0);
            map.put("msg", "插入成功");
            commentInfo = appraise;
            float a = algorithm.sendComment();
            behaveService.updateGoods(userId ,goods_id, "randomComment", a);
            behaveService.updateStyle(userId, goods_id, "randomComment", a);
            System.out.println(a);
        }
        else {
            map.put("code", 1);
            map.put("msg", "插入失败");
        }
        return map;
    }


}
