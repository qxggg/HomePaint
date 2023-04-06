package com.homepainter.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.mapper.UserFurnitureMapper;
import com.homepainter.pojo.UserFurniture;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
public class PictureBuilder {

    @Autowired
    UserFurnitureMapper userFurnitureMapper;

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

    public String createProject(String projectName, String token, int telephone) throws IOException {
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


    public void upload(String token, String url, String fp_id, File binaryFile) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpPost httpPost = new HttpPost(url);
//        httpPost.setHeader("token", token);
//        httpPost.setHeader("requestType-Content", "ModelingStand");
//        httpPost.setHeader("Content-Type", "application/json");
        HttpPut httpPut = new HttpPut(url);
        httpPut.setHeader("token", token);
        httpPut.setHeader("Content-Type", "application/x-zip-compressed");
        httpPut.setHeader("x-oss-meta-author", fp_id);
        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addPart("binaryFile", new FileBody(binaryFile, ContentType.APPLICATION_OCTET_STREAM, binaryFile.getName()))
                .build();
        httpPut.setEntity(reqEntity);
        CloseableHttpResponse response = httpClient.execute(httpPut);
    }

    public void cover(String token, String fp_id, File file) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://www.gongchuangshijie.com:81/BoYa/files/uploadCover");
        httpPost.setHeader("token", token);
        httpPost.setHeader("requestType-Content", "ModelingStand");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("fp_id", fp_id);
        FileBody fileBody = new FileBody(file);
        builder.addPart("coverFile", fileBody);
        httpPost.setEntity(builder.build());
        HttpResponse response = httpClient.execute(httpPost);
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


    public void down(String fp_id, String format, int telephone) throws IOException {
        PictureBuilder pictureBuilderController = new PictureBuilder();
        unlocked(pictureBuilderController.getToken(), fp_id);
        FileDownloader.get_zip(fp_id, format, telephone);

    }

    public void up(File zip, File picture, String projectName, int telephone, String handleType, String type, String photoInfo) throws IOException {
        PictureBuilder pictureBuilderController = new PictureBuilder();
        String token = pictureBuilderController.getToken();
        String fp_id = pictureBuilderController.createProject(projectName, token, telephone);
        String url = pictureBuilderController.getUrl(token, fp_id, photoInfo, handleType, type);
        pictureBuilderController.upload(token, url, fp_id, zip);
        pictureBuilderController.cover(token, fp_id, picture);
        Date now = new Date();
        userFurnitureMapper.insertUserFurniture(new UserFurniture(telephone, fp_id, projectName, now));
    };

    public int insert(UserFurniture userFurniture){
        return userFurnitureMapper.insertUserFurniture(userFurniture);
    }


}

