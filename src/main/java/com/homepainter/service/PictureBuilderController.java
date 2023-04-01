package com.homepainter.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PictureBuilderController {

    public String getToken() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://www.gongchuangshijie.com:81/BoYa/userInfo/userLogin.do");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("loginName", "Dvh6iHKc9isFcR06Sy/LG8ZEeC0U9jf2YgsvFrBE5PppnEiyD1HfF4Mx35EtJhG/Id13ZFWt3OpmKmssvcD9BCMiXlDgy3L5ppxO2NdSxhsj3WcB85t33vDMcakXfp0iIPEWZBMdeg5ZRV97b0GjRiT4PhjJYv7CR3H7RMFhSZA+V6qhy0z8drMwe54GRdt+IvZ2THIB/RyUG6zoVRebMhbxcY7X8fyDhdr1TkA1SSepZUIExGLXretqo6yTVf+VsDgWlN1Y8GVIUAG9RMng9k+KAvT8HHTLTH2gRH5VI7wwVpJ/FrqUWILJDwDndkubRtIdVj9zt9iy+bqRUm1V6Q==");
        builder.addTextBody("loginPass", "bI3jDvxKxfUIccvFjuxpWya9aOpqQ+3tbbVzWCeY2d9HAK0PjiyAjvSHL+Qnc2bRCuN3HTVWHGR6e1i8A8woLQ15JltepcgBN/coyYV97N1VKknoSSTEDQ5qbOFrjdE34X5+P7VEylqSSZ/8FQCC9yOeFAEhU7tIs4S2huk6vXFeb/WBlP7IGidYhZJ0D3+7++Ek0r4mFFOUVHicI/T0KwLkXFvpAuqz8qtJLT9EiBnvJ0+YKABDcQqE184cEhbBN7eeFT6rZA5LavUDH+LhlLy65M9kxLER7jwXuteQkqIfMzOtZy7Ile0SJi+VpLFn6lpVDliwfBguji67+kQl1Q==");

        httpPost.setEntity(builder.build());
        httpPost.setHeader("requestType-Content", "ModelingStand");
        HttpResponse response = httpClient.execute(httpPost);

        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        JSONObject jsonObject = JSON.parseObject(result);
        String token = jsonObject.getString("token");
        System.out.println(token);
        httpClient.close();
        return token;
    }

}
