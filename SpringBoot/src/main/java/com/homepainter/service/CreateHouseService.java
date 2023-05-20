package com.homepainter.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.controller.HouseDataController;
import com.homepainter.util.RedisUtil;
import com.homepainter.util.RuleUtils;
import org.dom4j.rule.Rule;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CreateHouseService {

    @Autowired
    GetGoods getGoods;


    public JSONArray createHouse(int userId, double x, double y, JSONArray rooms){
        List<String> styles = getGoods.getStyleList(userId);
        JSONObject idx = new JSONObject();
        JSONObject room = new JSONObject();
        idx.put("x", x);
        idx.put("y", y);
        for (int i = 0; i < rooms.size(); ++i){
            room = rooms.getJSONObject(i);
            JSONArray wallPoint = room.getJSONArray("point");
            if (RuleUtils.isPeopleInHouse(idx, wallPoint)) break;
        }
        String name = room.getString("name");
        if (name.equals("客厅")) return createLivingRoom(styles);
        else if (name.equals("卧室")) return createLivingRoom(styles);
        else if (name.equals("卫生间")) return createBathroom(styles);
        else if (name.equals("洗漱间")) return createWashRoom(styles);
        else if (name.equals("阳台")) return createBalcony(styles);
        else if (name.equals("厨房")) return createKitchen(styles);
        else return createBedroom(styles);
    }

    public static int generateRandomNumber(int n) {
        Random random = new Random();
        return random.nextInt(n + 1);
    }

    public JSONArray recommend(int userId, String style) {
        return null;
    }


    public JSONArray createBalcony(List<String> style){
        JSONArray sum = new JSONArray();
        find(sum, style, "货架");
        find(sum, style, "吸顶灯");
        find(sum, style, "吊灯");
        return sumLength(sum);
    }

    public JSONArray createLivingRoom(List<String> style){
        JSONArray sum = new JSONArray();
        find(sum, style, "三座/多座沙发");
        find(sum, style, "L形沙发");
        find(sum, style, "圆形茶几");
        find(sum, style, "咖啡桌");
        find(sum, style, "餐桌");
        find(sum, style, "餐椅");
        find(sum, style, "圆形茶几");
        find(sum, style, "吸顶灯");
        find(sum, style, "吊灯");
        return sumLength(sum);
    }

    @NotNull
    private JSONArray sumLength(JSONArray sum) {
        if (sum.size() < 20){
            List<Map<String, Object>> tmp = getGoods.GetGoods_ByStyle_Category("现代", "吊灯");
            for (int i = sum.size(), j = 0; i < 20; ++i, ++j)
                sum.add(tmp.get(j));
        }
        else if (sum.size() == 20);
        else {
            for (int i = 20; i < sum.size(); ++i)
                sum.remove(i);
        }
        return sum;
    }

    public JSONArray createBathroom(List<String> style){
        JSONArray sum = new JSONArray();
        find(sum, style, "三座/多座沙发");
        find(sum, style, "吸顶灯");
        find(sum, style, "圆形茶几");
        find(sum, style, "圆形茶几");
        find(sum, style, "圆形茶几");
        find(sum, style, "圆形茶几");
        find(sum, style, "圆形茶几");
        find(sum, style, "圆形茶几");
        find(sum, style, "圆形茶几");
        find(sum, style, "圆形茶几");
        find(sum, style, "吸顶灯");
        find(sum, style, "吊灯");
        return sumLength(sum);
    }

    public JSONArray createWashRoom(List<String> style){
        JSONArray sum = new JSONArray();
        find(sum, style, "梳妆台");
        find(sum, style, "梳妆椅");
        find(sum, style, "梳妆椅");
        find(sum, style, "梳妆椅");
        find(sum, style, "吸顶灯");
        find(sum, style, "吊灯");
        return sumLength(sum);
    }

    public JSONArray createKitchen(List<String> style){
        JSONArray sum = new JSONArray();
        find(sum, style, "餐具柜/侧柜/控制台桌");
        find(sum, style, "咖啡桌");
        find(sum, style, "酒吧凳");
        find(sum, style, "酒柜");
        find(sum, style, "餐桌");
        find(sum, style, "餐椅");
        find(sum, style, "吸顶灯");
        find(sum, style, "吊灯");
        return sumLength(sum);
    }

    public JSONArray createBedroom(List<String> style){
        JSONArray sum = new JSONArray();
        find(sum, style, "单人床");
        find(sum, style, "床头柜");
        find(sum, style, "特大床");
        find(sum, style, "双层床");
        find(sum, style, "书桌");
        find(sum, style, "书柜/珠宝衣橱");
        find(sum, style, "脚凳/沙发床/床头凳/凳子");
        find(sum, style, "衣柜");
        return sumLength(sum);
    }




    public void find(JSONArray sum, List<String> styles, String category) {
        JSONArray jsonArray = new JSONArray();
        JSONArray styleList = new JSONArray();
        for (String style : styles) {
            String json = JSON.toJSONString(getGoods.GetGoods_ByStyle(style));
            styleList = JSONArray.parseArray(json);
            for (int j = 0; j < styleList.size(); ++j) {
                JSONObject jj = styleList.getJSONObject(j);
                if (jj.getString("category").equals(category))
                    jsonArray.add(jj);
            }
        }

        if (jsonArray.size() == 0) return;
        if (jsonArray.size() == 1) sum.add(jsonArray.get(0));
        else {
            for (int i = 0; i < 2; ++i) {
                int random = generateRandomNumber(jsonArray.size() - 1);
                sum.add(jsonArray.get(random));
            }
        }
    }

}
//        JSONArray jsonArray = new JSONArray();
//        for (String style : styles) {
//            String json = JSON.toJSONString(getGoods.GetGoods_ByStyle_Category(style, category));
//            JSONArray styleList = JSONArray.parseArray(json);
//            for (int j = 0; j < styleList.size(); ++j)
//                jsonArray.add(styleList.get(j));
//        }
//        if (jsonArray.size() == 0) return;
//        if (jsonArray.size() == 1) sum.add(jsonArray.get(0));
//        else {
//            for (int i = 0; i < 2; ++i) {
//                int random = generateRandomNumber(jsonArray.size() - 1);
//                sum.add(jsonArray.get(random));
//            }
//        }




//    public JSONObject createMainRoom(JSONObject room, JSONArray remove, List<HashMap<String, Object>> goods, String style, String category, float up){
//        JSONArray wall = RuleUtils.roomHandler(room, remove);
//        String json = JSON.toJSONString(getGoods.GetGoods_ByStyle_LianXiang_Category(style, category));
//        JSONArray jsonArray = JSONArray.parseArray(json);
//        int random = generateRandomNumber(jsonArray.size() / 5);
//        for (int i = random; i < jsonArray.size(); ++i){
//            JSONObject good = jsonArray.getJSONObject(i);
//            JSONArray index = good.getJSONArray("vertexs");
//            int goodsId = Integer.parseInt(good.getString("goodsId"));
//            JSONObject center = good.getJSONObject("Center");
//
//
//            for (int j = 1; j < wall.size(); ++j){
//                index = good.getJSONArray("vertexs");
//                JSONObject w = wall.getJSONObject(j);
//                if (w.get("category").equals(2) || w.get("category").equals(1) || w.get("category").equals(3)) continue;
//                RuleUtils.insertWallGoods(index, w, center);
//                if (RuleUtils.inputGoods(goods, wall, up, index)){
//                    w.put("category", 3);
//                }
//            }
//        }
//        return null;
//    }
//}

