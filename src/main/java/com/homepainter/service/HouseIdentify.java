package com.homepainter.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.apigateway.sdk.utils.Client;
import com.cloud.apigateway.sdk.utils.Request;
import com.homepainter.util.Constant;
import com.homepainter.util.File2Base64;
import com.homepainter.util.SSLCipherSuiteUtil;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;

public interface HouseIdentify {
    static final Logger LOGGER = LoggerFactory.getLogger(HouseIdentify.class);

    public static String houseIdentify(String url){
        String res = "";
        Request request = new Request();
        try {
            request.setKey("0b54b951a8bf4cff8e0def9dcae62196");
            request.setSecret("a845bf8174c64297a838c0e3a51e24a5");
            request.setMethod("POST");
            request.setUrl("https://c65e8fb0e61448fc8bacac6d37321956.apig.cn-east-3.huaweicloudapis.com/api/roomdetector/");
            request.addHeader("Content-Type", "application/json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", url);
            jsonObject.put("action", "img2floorplan");
            request.setBody(jsonObject.toJSONString());

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        try {
            // Sign the request.
            okhttp3.Request signedRequest = Client.signOkhttp(request, Constant.SIGNATURE_ALGORITHM_SDK_HMAC_SHA256);
            // creat okhttpClient.
            OkHttpClient client = SSLCipherSuiteUtil.createOkHttpClient(Constant.INTERNATIONAL_PROTOCOL);
            // Send the request.
            Response response = client.newCall(signedRequest).execute();
            // Print the status line of the response.
            LOGGER.info("status: " + response.code());
            // Print the header fields of the response.
            Headers resHeaders = response.headers();

            for (String h : resHeaders.names()) {
                LOGGER.info(h + ": " + resHeaders.get(h));
            }

            // Print the body of the response.
            ResponseBody resEntity = response.body();
//            LOGGER.info(resEntity.string());

            res = resEntity.string();
            System.out.println("haha" + res );
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return res;
    }

    public static void main(String[] args) throws Exception {
        // Create a new request.
        Request request = new Request();
        try {
            request.setKey("0b54b951a8bf4cff8e0def9dcae62196");
            request.setSecret("a845bf8174c64297a838c0e3a51e24a5");
            request.setMethod("POST");
            request.setUrl("https://c65e8fb0e61448fc8bacac6d37321956.apig.cn-east-3.huaweicloudapis.com/api/roomdetector/");
            request.addHeader("Content-Type", "application/json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", "https://7463-tcb-6dkhphdodd5e2a-7dedu72135999-1309304321.tcb.qcloud.la/Snipaste_2023-03-29_21-42-38.png");
            jsonObject.put("action", "img2floorplan");
            request.setBody(jsonObject.toJSONString());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return;
        }
        try {
            // Sign the request.
            okhttp3.Request signedRequest = Client.signOkhttp(request, Constant.SIGNATURE_ALGORITHM_SDK_HMAC_SHA256);
            // creat okhttpClient.
            OkHttpClient client = SSLCipherSuiteUtil.createOkHttpClient(Constant.INTERNATIONAL_PROTOCOL);
            // Send the request.
            Response response = client.newCall(signedRequest).execute();
            // Print the status line of the response.
            LOGGER.info("status: " + response.code());
            // Print the header fields of the response.
            Headers resHeaders = response.headers();

            for (String h : resHeaders.names()) {
                LOGGER.info(h + ": " + resHeaders.get(h));
            }
            System.out.println("123");
            // Print the body of the response.
            ResponseBody resEntity = response.body();
//            LOGGER.info(resEntity.string());
            System.out.println("jajaja");
            System.out.println(resEntity.string());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

    }
}
