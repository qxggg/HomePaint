package com.homepainter.controller;

import com.homepainter.util.FileUtils;
import com.qcloud.cos.model.PutObjectResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.homepainter.service.Upload_File.putObject;
import static com.homepainter.util.File2Base64.Base64ToFile;
import static com.homepainter.util.FileUtils.isExit;
import static com.homepainter.util.ImageCropper.crop;
import static com.homepainter.util.ImageCropper.cropImageToPolygon;
import static com.homepainter.util.ReadFile.readfile;

@RequestMapping("/picture")
@RestController
public class PictureController {

    /**
     * 裁剪图片，可以不传 x,y(自动补全100)
     * @param name
     * @param xString
     * @param yString
     * @return
     */
    @GetMapping("/WH/{name}")
    public void getpicture(@PathVariable("name") String name,
                             @RequestParam(value="width",required = false) String xString,
                             @RequestParam(value = "height",required = false) String yString,
                             HttpServletResponse response) throws IOException {
        // 查看是否为空
        double x = 100,y = 100;
        if(xString==null){
            x = 1000;
        }else{
            x = Double.parseDouble(xString);
        }
        if(yString==null){
            y = 1000;
        }else{
            y = Double.parseDouble(yString);
        }

        if(!isExit(name,"PictureData/")){
            response.setContentType("text/plain");
            String responseText = "Image not exit";
            PrintWriter writer = response.getWriter();
            writer.print(responseText);
            writer.flush();
            return;
        }

        // 获取新的名字
        String newFilename = "";
        if(x==100&&y==100){
            newFilename = name;
        }else{
            newFilename = name.substring(0,name.length()-4)+"_"+x+"_"+y +".png";

            // 如果没有则裁剪
            if(!isExit(newFilename,"PictureData/"))
                newFilename = crop(name,x,y,newFilename);
        }

        // 读取图片，返回结果
        readfile(newFilename,"PictureData/",response);
    }


    /**
     * 多边形裁剪
     * @param name
     * @param response
     * @throws IOException
     */
    @PostMapping("/Polygon/{name}")
    public String PolygonCrop(@PathVariable("name") String name,
                           @RequestBody Map<String,Object> input,
                           HttpServletResponse response) throws IOException {
        List<Double> xList = (List<Double>) input.get("x");
        List<Double> yList = (List<Double>) input.get("y");

        int [] xpoints = new int[xList.size()];
        int [] ypoints = new int[yList.size()];

        Double temp = xList.get(0);

        for(int i=0;i<xList.size();i++){
            xpoints[i] = (int) Math.round( xList.get(i) );
            ypoints[i] = (int) Math.round( yList.get(i) );
        }

        try {
            return cropImageToPolygon(xpoints,ypoints,name);
        }catch (Exception e){
            return e.toString();
        }

    }

    /**
     * 打开图片
     * @param name
     * @param response
     * @throws IOException
     */
    @GetMapping("/open/{name}")
    public void OpenImage(@PathVariable("name") String name,
                          HttpServletResponse response) throws IOException {


        // 文件不存在
        if(!isExit(name,"PictureData/")){
            response.setContentType("text/plain");
            String responseText = "Image not exit";
            PrintWriter writer = response.getWriter();
            writer.print(responseText);
            writer.flush();
            return;
        }

        String filepath = "";
        String os = System.getProperty("os.name").toLowerCase();
        if(os.indexOf("linux")!=-1){
            filepath = FileUtils.Read_FOLDER_linux;
        }else{
            filepath = FileUtils.Read_FOLDER_win;
        }
        filepath += "PictureData/";

        Path imagePath = Paths.get(filepath, name);
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.setContentType(Files.probeContentType(imagePath));
        Files.copy(imagePath, response.getOutputStream());
    }

    @PostMapping ("/upload")
    public Map<String,Object> upload(@RequestBody Map<String,Object> input) throws IOException {
        Map<String,Object> res = new HashMap<>();
        try{
            // 图片base64转换并上传
            File file_res = Base64ToFile(input.get("image").toString());
            String filename =  System.currentTimeMillis() + "upload.jpg";
            PutObjectResult putObjectResult = putObject(filename, file_res, "picture/");
            String Imageurl = "https://image-1304455659.cos.ap-nanjing.myqcloud.com/picture/" + filename;
        }catch (Exception e){
            res.put("msg",e.toString());
            res.put("code",16);
            return res;
        }
        // 图片base64转换并上传


        res.put("msg","上传成功");
        res.put("code",0);
        return res;
    }


}
