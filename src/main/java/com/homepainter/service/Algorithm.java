package com.homepainter.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class Algorithm {

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

}
