package com.homepainter.ForTest;


import com.homepainter.service.GetGoods;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class test {

    @Autowired
    GetGoods getGoods;
    @Test
    public void test(){
        getGoods.GetGoods_ByStyle("现代");
    }



}
