package com.homepainter.mapper;

import com.homepainter.pojo.RandomConsume;
import io.lettuce.core.ScriptOutputType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TestBehavior {

    @Autowired
    RandomConsumeMapper randomConsumeMapper;

    @Test
    public void insert(){
        List<Integer> insert = new ArrayList<>();
        for (int i = 0; i < 2000; ++i) insert.add(0);
//        randomConsumeMapper.insertConsume(new RandomConsume(1,insert.toString()));
        String a = insert.toString();
        String[] strArr = a.split(", ");
        strArr[0] = strArr[0].substring(1);
        strArr[1999] = strArr[1999].substring(0, strArr[1999].length() - 1);


        int[] intArr = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            intArr[i] = Integer.parseInt(strArr[i]);
        }

        System.out.println(intArr[101]);
    }
}
