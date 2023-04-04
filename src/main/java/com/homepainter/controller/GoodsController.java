package com.homepainter.controller;

import com.alibaba.fastjson.JSONObject;
import com.homepainter.controller.DTO.InsertGoods;
import com.homepainter.pojo.Goods;
import com.homepainter.pojo.Goods_image;
import com.homepainter.service.GoodsService;
import com.homepainter.service.TranslateService;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    TranslateService translateService;

    public static int goodsInsertCount = 1001;

    @GetMapping("/get_list")
    public Map<String, Object> getAllList(){
        Map<String, Object> map = new HashMap<>();
        map.put("data", goodsService.getAllGoods());
        map.put("code", 0);
        map.put("msg", "查询商品成功！");
        return map;
    }

    @GetMapping("/get")
    public Map<String, Object> getGoodsById(@RequestBody Map<String, Object> data){
        Map<String, Object> map = new HashMap<>();
        if (data.get("goods_id") == null) map.put("data", goodsService.getAllGoods());
        else map.put("data", goodsService.getGoodsById((int) data.get("goods_id")));
        map.put("code", 0);
        map.put("msg", "查询商品成功！");
        return map;
    }
    @PostMapping("get_list")
    public Map<String, Object> getGoodsByContent(@RequestBody Map<String, Object> data){
        Map<String, Object> map = new HashMap<>();
        if (data.get("search_content") == null) map.put("data", goodsService.getAllGoods());
        else map.put("data", goodsService.getGoodsByContent((String) data.get("search_content")));
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


}
