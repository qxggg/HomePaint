package com.homepainter.service;

import com.homepainter.util.File2Base64;

public class Test {

    public static void main(String[] args) {
        String fullname = "";
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("linux") != -1) {;
            fullname = "/www/wwwroot/module";
        }else{
            fullname = "."+fullname;
        }
        String a = File2Base64.GET_Image2Base64("https://img1.baidu.com/it/u=1509442605,3952579348%26fm=253%26fmt=auto%26app=138%26f=JPEG?w=667%26h=500");
        File2Base64.GETFile_Image2Base64("https://img1.baidu.com/it/u=1509442605,3952579348%26fm=253%26fmt=auto%26app=138%26f=JPEG?w=667%26h=500", fullname);

    }
}
