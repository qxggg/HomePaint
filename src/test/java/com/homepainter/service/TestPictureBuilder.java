package com.homepainter.service;

import com.homepainter.pojo.UserFurniture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@SpringBootTest
public class TestPictureBuilder {

    @Autowired
    PictureBuilder pictureBuilder;

    @Test
    public void testpictureBuilder() throws IOException {
        String pathname = "C:\\Users\\25697\\Desktop\\第十六届全国大学生软件创新大赛\\HomePaint\\download\\Model.zip";
        File file = new File(pathname);
        File picture = new File("C:\\Users\\25697\\Desktop\\第十六届全国大学生软件创新大赛\\HomePaint\\download\\model_m2.jpg");
        pictureBuilder.up(file, picture, "齐辛格", 1, "5", "1", "5000.00000");
//        Date date = new Date();
//        pictureBuilder.insert(new UserFurniture(1, "123", "12", date));
    }
}
