package com.homepainter.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.apache.http.ContentTooLongException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.stereotype.Service;

import javax.swing.text.AbstractDocument;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
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

    public String createProject(String projectName, String token, String telephone) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://www.gongchuangshijie.com:81/BoYa//fileFp/createProject.do?projectName=" + telephone + projectName);
        httpGet.setHeader("token", token);
        httpGet.setHeader("requestType-Content", "Model" +
                "ingStand");
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        JSONObject jsonObject = JSON.parseObject(result);
        String fp_id = jsonObject.getString("obj");
        System.out.println(fp_id);
        return fp_id;
    }

    public String getUrl(String token, String fp_id, String photoInfo, String handleType, String type) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://www.gongchuangshijie.com:81/BoYa/files/generatePresignedUrl?fileName=source_" + photoInfo + ".zip&fp_id=" + fp_id + "&handle_type=" + handleType + "&type=" + type);
        httpGet.setHeader("token", token);
        httpGet.setHeader("requestType-Content", "ModelingStand");
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        JSONObject jsonObject = JSON.parseObject(result);
        String url = jsonObject.getString("obj");
        System.out.println(url);
        return url;
    }

    public String post(String token, String url, MediaType mediaType){

//        RequestBody requestBody = RequestBody.create(mediaType, )
        return null;
    }


    public void unlocked(String token, String fp_id) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://www.gongchuangshijie.com:86/api/ExportModel/PostExportModel");
        httpPost.setHeader("token", token);
        httpPost.setHeader("requestType-Content", "ModelingStand");
        httpPost.setHeader("Content-Type", "application/json");
        String rawBody = "{\"fp_id\":" + "\"" + fp_id + "\"}" ;
        HttpEntity entity1 = new StringEntity(rawBody);
        httpPost.setEntity(entity1);
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        JSONObject jsonObject = JSON.parseObject(result);
        String url = jsonObject.getString("message");
        System.out.println(url);
    }

    public void download(String fp_id, String format, String telephone) throws IOException {
       FileDownloader.get_zip(fp_id, format, telephone);
    }
    public static void main(String[] args) throws IOException {
        PictureBuilderController pictureBuilderController = new PictureBuilderController();
//        pictureBuilderController.createProject("131411", pictureBuilderController.getToken(), "19819605657");
 //       pictureBuilderController.getUrl(pictureBuilderController.getToken(),"838EF1D5ADD1309B19279FDF848DE57E", "5000.0000000", "1", "5");
//    pictureBuilderController.unlocked(pictureBuilderController.getToken(), "838EF1D5ADD1309B19279FDF848DE57E");
    pictureBuilderController.download(pictureBuilderController.getToken(), "60BDBBB56889116C41E9465FD716A6AF", "obj");
    }

}
