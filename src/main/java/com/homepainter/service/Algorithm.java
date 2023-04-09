package com.homepainter.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.mapper.GoodsMapper;
import com.homepainter.pojo.Goods;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.homepainter.controller.AlgorithmController.recommendId;
import static com.homepainter.controller.AlgorithmController.recommendType;

@Service
public class Algorithm {

    @Autowired
    GoodsMapper goodsMapper;
    public float sendComment() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://82.157.66.39:8083/snowNLP");
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        JSONObject jsonObject = JSON.parseObject(result);
        BigDecimal score = (BigDecimal) jsonObject.get("comment");
        float a = score.setScale(5, RoundingMode.HALF_UP).floatValue();
        return a;
    }

    public List<Goods> goodsRecommend(int userId) throws IOException {
        recommendId = userId;
        recommendType = "goods";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://82.157.66.39:8083/goodsRecommendation");
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        JSONObject jsonObject = JSON.parseObject(result);
        List<Integer> goodsList = (List<Integer>) jsonObject.get("goodsId");
        List<Goods> goods = new ArrayList<>();
        for (int s : goodsList)
            goods.add(goodsMapper.getGoodsById(s));
        return goods;
    }


    public List<Integer> styleRecommend(int userId) throws IOException {
        recommendId = userId;
        recommendType = "style";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://82.157.66.39:8083/styleRecommendation");
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        JSONObject jsonObject = JSON.parseObject(result);
        List<Integer> styleList = (List<Integer>) jsonObject.get("styleId");
        return styleList;
    }


}
