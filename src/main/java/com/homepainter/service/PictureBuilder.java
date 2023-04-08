package com.homepainter.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.homepainter.mapper.UserFurnitureMapper;
import com.homepainter.pojo.UserFurniture;
import com.homepainter.util.File2Base64;
import com.qcloud.cos.model.PutObjectResult;
import org.apache.commons.io.FileUtils;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.homepainter.service.Upload_File.putObject;

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

    public List<HashMap<String, Object>> getList(int userId) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://www.gongchuangshijie.com:81/BoYa/fileFp/findFPByUser.do?pageCurrent=1&pageMaxPer=100000000");
        httpGet.setHeader("token", getToken());
        httpGet.setHeader("requestType-Content", "Model" +
                "ingStand");
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        JSONObject jsonObject = JSON.parseObject(result);
        List<Map<String, Object>> map = (List<Map<String, Object>>) jsonObject.get("list");

        List<UserFurniture> userFurnitures = userFurnitureMapper.getById(userId);

        List<HashMap<String, Object>> show = new ArrayList<>();
        for (Map<String, Object> m : map){
            for (UserFurniture userFurniture : userFurnitures){
                if (m.get("fp_id").equals(userFurniture.getFpId())) {
                    HashMap<String, Object> tmp = new HashMap<>();
                    tmp.put("userFurniture", userFurniture);
                    tmp.put("status", m.get("description"));
                    tmp.put("imageUrl", m.get("thumb"));
                    show.add(tmp);
                }
            }
        }
        return show;
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
        String rawBody = "{\"fp_id\":" + "\"" + fp_id + "\"}";
        HttpEntity entity1 = new StringEntity(rawBody);
        httpPost.setEntity(entity1);
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        JSONObject jsonObject = JSON.parseObject(result);
        String url = jsonObject.getString("message");
        System.out.println(url);
    }


    public Map<String,Object> down(String fp_id, String format, int telephone, String projectName) throws IOException {
        PictureBuilder pictureBuilderController = new PictureBuilder();
        unlocked(pictureBuilderController.getToken(), fp_id);
        Map<String,Object> res = FileDownloader.get_zip(fp_id, format, telephone);

        return res;
    }


    public String jpgHandler(List<String> httpurl, String fp_id) throws IOException {
        int count = 0;
        String fullname = "upload";

        String os = System.getProperty("os.name").toLowerCase();

        if (os.indexOf("linux") != -1) fullname = "/www/wwwroot/module" + fullname;



        Path path = Paths.get(fullname + "/" + fp_id);
        Path pathCreate = Files.createDirectories(path);


        List<String> files = new ArrayList<>();
        for (String s : httpurl) {
            File2Base64.GETFile_Image2Base64(s, fullname + "/"  + fp_id + "/temp" + count + ".jpg");
            files.add(fullname + "/"  + fp_id + "/temp" + count + ".jpg");
            count++;
        }
        FileOutputStream fos = new FileOutputStream(fullname + "/" + fp_id + "/" + fp_id + ".zip");
        // 创建一个ZipOutputStream对象，用于将文件写入到zip文件中
        ZipOutputStream zos = new ZipOutputStream(fos);

        for (String file : files) {
            FileInputStream fis = new FileInputStream(file);
            // 创建一个ZipEntry对象，用于表示zip文件中的一个条目
            ZipEntry entry = new ZipEntry(file);
            // 将该条目添加到zip文件中
            zos.putNextEntry(entry);
            // 将待打包的文件内容写入到zip文件中
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            // 关闭该条目
            zos.closeEntry();
            // 关闭输入流
            fis.close();
        }
        return fullname + "/"  + fp_id + "/temp0"  + ".jpg";
    }


    public void zipFile(String sourceFile, String zipFile) {
        try {
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            FileInputStream fis = new FileInputStream(sourceFile);
            ZipEntry zipEntry = new ZipEntry(sourceFile);
            zos.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }
            zos.closeEntry();
            fis.close();
            zos.close();
            fos.close();
            System.out.println("File compressed successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException {
        PictureBuilder pictureBuilder = new PictureBuilder();

    }

    public static void deleteFolder(File file) {
        File[] files = file.listFiles();//将file子目录及子文件放进文件数组
        if (files != null) {//如果包含文件进行删除操作
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {//删除子文件
                    files[i].delete();
                } else if (files[i].isDirectory()) {//通过递归方法删除子目录的文件
                   deleteFolder(files[i]);
                }
                files[i].delete();//删除子目录
            }
        }
    }

    public String up(List<String> httpUrl, String projectName, int telephone, String handleType, String type, String photoInfo) throws IOException, InterruptedException {
        PictureBuilder pictureBuilderController = new PictureBuilder();
        String token = pictureBuilderController.getToken();
        String fp_id = pictureBuilderController.createProject(projectName, token, telephone);
        String picturePath = jpgHandler(httpUrl, fp_id);
        File picture = new File(picturePath);
        String fullname = "upload";

        String os = System.getProperty("os.name").toLowerCase();

        if (os.indexOf("linux") != -1) fullname = "/www/wwwroot/module" + fullname;

        String filepath = fullname + "/" + fp_id + "/" + fp_id + ".zip";
        File zip = new File(filepath);
        String url = pictureBuilderController.getUrl(token, fp_id, photoInfo, handleType, type);
        pictureBuilderController.upload(token, url, fp_id, zip);
        pictureBuilderController.cover(token, fp_id, picture);
//        Date now = new Date();
//        userFurnitureMapper.insertUserFurniture(new UserFurniture(telephone, fp_id, projectName, now));

        PutObjectResult putObjectResult = putObject(fp_id + ".jpg", picture,"images/");

        File delete = new File(fullname);
        PictureBuilder.deleteFolder(delete);
        Date now = new Date();
        userFurnitureMapper.insertUserFurniture(new UserFurniture(telephone, fp_id, projectName, now));
        return fp_id;
    }

    ;

    public String upVideo(File video, File picture, String projectName, int telephone, String handleType, String type, String photoInfo) throws IOException, InterruptedException {
        PictureBuilder pictureBuilderController = new PictureBuilder();
        String token = pictureBuilderController.getToken();
        String fp_id = pictureBuilderController.createProject(projectName, token, telephone);


        String filepath = "upload/" + fp_id + "/" + fp_id + ".zip";

        File zip = new File(filepath);

        String url = pictureBuilderController.getUrl(token, fp_id, photoInfo, handleType, type);
        pictureBuilderController.upload(token, url, fp_id, zip);
        pictureBuilderController.cover(token, fp_id, picture);
//        Date now = new Date();
//        userFurnitureMapper.insertUserFurniture(new UserFurniture(telephone, fp_id, projectName, now));
        return fp_id;
    }


    public int insert(UserFurniture userFurniture){
        return userFurnitureMapper.insertUserFurniture(userFurniture);
    }


}

