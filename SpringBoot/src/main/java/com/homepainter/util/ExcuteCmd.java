package com.homepainter.util;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;



public class ExcuteCmd {

    /*
        shname 需要带后缀.sh
     */
    public static boolean excuteCmd(String shname){
        String os = System.getProperty("os.name").toLowerCase();
        if(os.indexOf("linux") !=-1){
            System.out.println("start CMD");
            // 执行命令
            Runtime run = Runtime.getRuntime();
            Process process = null;
            String result = "";
            try {
                process = run.exec("sh "+shname);


                process.waitFor(5,TimeUnit.SECONDS);
                result = streamToStr(process.getInputStream());
                System.out.println(result);
                process.destroy();


                if(result.length()!=0)  return true;
                else   return false;
            }catch (Exception e){
                e.printStackTrace();
                System.out.println(e);
                return false;
            }
        }
        return false;
    }

    public static String streamToStr(InputStream inputStream) {
        if (inputStream != null) {
            try (BufferedInputStream reader = new BufferedInputStream(inputStream)) {
                return IOUtils.toString(reader, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
