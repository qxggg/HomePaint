package com.homepainter.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import java.io.*;

public class ReadJson {
    /**
        读入JSON数据，输入文件名，将文件妨碍resource目录下即可
        返回String,还需要用JSON parse一下
        filename需要添加.JSON后缀
     */
    public static String get_json (String filename) {
        String txt = "";
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            //Resource[] resources = resolver.getResources("templaets/layout/email.html");
            String property = System.getProperty("user.dir")+ File.separator;
            System.out.println(property);

            String filepath = "";

            Resource resource = null;
            InputStream stream = null;
            String os = System.getProperty("os.name").toLowerCase();
            if(os.indexOf("linux")!=-1){
                filepath = FileUtils.Read_FOLDER_linux+filename;
            }else{
                filepath = FileUtils.Read_FOLDER_win+filename;
            }

            resource = resolver.getResource(filepath);
            stream = resource.getInputStream();


            StringBuilder buffer = new StringBuilder();
            byte[] bytes = new byte[1024];

            try {
                for (int n; (n = stream.read(bytes)) != -1; ) {
                    buffer.append(new String(bytes, 0, n));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            txt = buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return txt;
    }

    /**
        filename需要添加.JSON后缀
        dir从/www/wwwroot/开始，后面需要添加 /
     */
    public static String readJson(String filename,String dir){
        String res = "";
        String filepath = "";
        String os = System.getProperty("os.name").toLowerCase();
        if(os.indexOf("linux")!=-1){
            filepath = FileUtils.Read_FOLDER_linux;
        }else{
            filepath = FileUtils.Read_FOLDER_win;
        }

        filepath += dir + filename;

        File file = new File(filepath); // 指定文件路径和名称
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8"); // 指定编码格式
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                res += line;
            }
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }
}
