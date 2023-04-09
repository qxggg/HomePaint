package com.homepainter.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alinlp.model.v20200629.GetPosChEcomRequest;
import com.aliyuncs.alinlp.model.v20200629.GetPosChEcomResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;

import java.util.ArrayList;
import java.util.List;

import static com.homepainter.util.ReadJson.get_json;

public class Search_Service {

    public static List<JSONObject> kmpSearch(String args) throws ClientException {
        // 测试运行
        GetPosChEcomResponse response = fenci(args);
        // 获取分词结果
        String fenci_result = response.getData();

        // 获取结果数组
        JSONArray jsonArray = JSON.parseArray(JSON.parseObject(fenci_result).getString("result"));
        // 结果集
        List<JSONObject> data = new ArrayList<>();
        for(int i=0;i<jsonArray.size();i++){
            if(data.size()>=20){
                break;
            }
            // 单个结果集转Object
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            // 获取搜索结果
            List<JSONObject> temp = searchByname(jsonObject.getString("word"));
            // 筛选是否已有
            for(int j=0;j<temp.size();j++){
                boolean is_have = false;
                JSONObject temp_j = temp.get(j);
                for(int k=0;k<data.size();k++){
                    JSONObject temp_k = data.get(k);
                    if(temp_k.get("goodsId").toString().equals(temp_j.get("goodsId").toString())){
                        is_have = true;
                        break;
                    }
                }
                if(!is_have){
                    data.add(temp_j);
                }
            }
        }
        // 查看结果集
        for(int i=0;i<data.size();i++){
//            System.out.println(data.get(i).getString("modalId"));
            data.get(i).put("imageURL","https://image-1304455659.cos.ap-nanjing.myqcloud.com/3D-FUTURE-model-part1/"+data.get(i).getString("modalId")+"/image.jpg");
        }
        return data;
    }

    public static GetPosChEcomResponse fenci(String text) throws ClientException {
        DefaultProfile defaultProfile = DefaultProfile.getProfile(
                "cn-hangzhou",
                "LTAI5t7SkD8SohPvVLoPbUeo",
                "DoMnPYROzBzSileNuVDK6BMA4lZYoD");
        IAcsClient client = new DefaultAcsClient(defaultProfile);
        //构造请求参数，其中GetPosChEcom是算法的actionName, 请查找对应的《API基础信息参考》文档并替换为您需要的算法的ActionName，示例详见下方文档中的：更换API请求
        GetPosChEcomRequest request = new GetPosChEcomRequest();
        //固定值，无需更改
        request.setSysEndpoint("alinlp.cn-hangzhou.aliyuncs.com");
        //固定值，无需更改
        request.setServiceCode("alinlp");
        //请求参数, 具体请参考《API基础信息文档》进行替换与填写
        request.setText(text);
        request.setTokenizerId("MAINSE");
        long start = System.currentTimeMillis();
        //获取请求结果，注意这里的GetPosChEcom也需要替换
        GetPosChEcomResponse response = client.getAcsResponse(request);
        System.out.println(response.hashCode());
        System.out.println(response.getRequestId() + "\n" + response.getData() + "\n" + "cost:" + (System.currentTimeMillis()- start));
        return response;
    }

    public static List<JSONObject> searchByname(String content){

        List<JSONObject>  data = new ArrayList<>();
        String json = get_json("goods.json");
        JSONObject Alljson = JSON.parseObject(json);
        JSONArray jsonArray =  JSON.parseArray(Alljson.getString("RECORDS"));

        for(int i=0;i< jsonArray.size();i++){
            if(data.size()>=20){
                break;
            }
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            List<Integer> list = kmp(content,jsonObject.getString("title").toString());
            List<Integer> list1 = new ArrayList<>();
            List<Integer> list2 = new ArrayList<>();
            List<Integer> list3 = new ArrayList<>();
            List<Integer> list4 = new ArrayList<>();

            if(jsonObject.getString("category").toString().length()!=0)
                list1 = kmp(content,jsonObject.getString("category").toString());
            if(jsonObject.getString("superCategory").toString().length()!=0)
                list2 = kmp(content,jsonObject.getString("superCategory").toString());
            if(jsonObject.getString("theme").toString().length()!=0)
                list3 = kmp(content,jsonObject.getString("theme").toString());
            if(jsonObject.getString("material").toString().length()!=0)
                list4 = kmp(content,jsonObject.getString("material").toString());
            if(list.size()!=0||list1.size()!=0||list2.size()!=0||list3.size()!=0||list4.size()!=0){
                // System.out.println(list);
                data.add(jsonObject);
            }

        }
        return data;
    }

    public static int[] computePrefixFunction(String pattern) {
        int[] prefix = new int[pattern.length()];
        int j = 0;
        for (int i = 1; i < pattern.length(); i++) {
            while (j > 0 && pattern.charAt(j) != pattern.charAt(i)) {
                j = prefix[j - 1];
            }
            if (pattern.charAt(j) == pattern.charAt(i)) {
                j++;
            }
            prefix[i] = j;
        }
        return prefix;
    }

    public static List<Integer> kmp(String text, String pattern) {
        List<Integer> matches = new ArrayList<Integer>();
        if (pattern.length() == 0) {
            matches.add(0);
            return matches;
        }
        int[] prefix = computePrefixFunction(pattern);
        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && pattern.charAt(j) != text.charAt(i)) j = prefix[j - 1];
            if (pattern.charAt(j) == text.charAt(i)) j++;
            if (j == pattern.length()) {
                matches.add(i - pattern.length() + 1);
                j = prefix[j - 1];
            }
        }
        return matches;
    }

}
