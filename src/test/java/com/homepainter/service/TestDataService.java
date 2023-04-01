package com.homepainter.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestDataService {

    @Autowired
    DataControlService dataControlService;

    @Test
    public void testData(){
        dataControlService.dataService();
    }
}
