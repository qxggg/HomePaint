package com.homepainter;

import com.homepainter.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HomePainterApplication {
    public static void main(String[] args) {
        SpringApplication.run(HomePainterApplication.class, args);
    }

}
