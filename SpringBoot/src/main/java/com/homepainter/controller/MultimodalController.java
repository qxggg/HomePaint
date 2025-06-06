package com.homepainter.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.pojo.DTO.Imageinfos_withurl;
import com.homepainter.pojo.Goods;
import com.homepainter.service.GoodsService;
import com.tencentcloudapi.tiia.v20190529.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.homepainter.service.DetectMLabelPro.DetectMLabelServie;
import static com.homepainter.service.DetectProductBeta.Send_DetectProduct;
import static com.homepainter.service.DetectProductBeta.Send_DetectProductBeta;
import static com.homepainter.service.DrawImageService.Draw_Upload_Image;
import static com.homepainter.service.Multimodal.search_image;
import static com.homepainter.util.object2map.Obj2Map;

@RestController
public class MultimodalController {

    @Autowired
    GoodsService goodsService;

    /*
        多模态检索API
     */
    @PostMapping("/MultiModal")
    public Map<String, Object> MultiModal(@RequestBody Map<String, Object> data) throws IOException {
        // 结果数据集
        Map<String, Object> res = new HashMap<>();
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

        // 图片匹配API


        List<HashMap<String, Object>> list = new ArrayList<>();
        List<Imageinfos_withurl> images_meta_result = search_image(image, use_imageurl);
        for(int i=0;i<images_meta_result.size();i++) {
            HashMap<String, Object> map = new HashMap<>();
            System.out.println(images_meta_result.get(i).getEntityId());
            map.put("images_meta_result", images_meta_result.get(i));
            Goods goods = goodsService.getGoodsByModal(images_meta_result.get(i).getEntityId());
            map.put("goods", goods);
            map.put("imageURL", goods.getImageUrl().get(0).getImageUrl());
            list.add(map);
        }
        if(images_meta_result == null)    return res;
        res.put("Image_Mate", list);

        // 执行通用图像标签API
        DetectLabelProResponse detectLabelProResponse = DetectMLabelServie(image, use_imageurl);
        if(detectLabelProResponse == null)    return res;
        res.put("All_Flag",detectLabelProResponse);

        // 商业图片API
        DetectProductBetaResponse product = Send_DetectProductBeta(image, use_imageurl);
        if(product == null)   return res;
        res.put("product",product);

        // 图片识别API
        DetectProductResponse furniture_meta = Send_DetectProduct(image,use_imageurl);
        if(product == null)   return res;
        res.put("furniture_meta",furniture_meta);
        // 图片处理
        Map<String,Object> temp_res = Draw_Upload_Image(image,furniture_meta,use_imageurl);
        if(!temp_res.get("code").toString().equals("0"))    return res;

        res.put("meta_reault", temp_res);
        res.put("code", 0);
        res.put("msg", "多模态检索成功");

        return res;
    }
}
