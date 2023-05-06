package com.homepainter.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.IOException;

@SpringBootTest
public class BuilderController {

    @Autowired
    Algorithm algorithm;

    @Test
    public void test() throws IOException {
        algorithm.sendComment();
    }

}
