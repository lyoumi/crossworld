package com.rpg.web.crossworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;

@SpringBootApplication
public class CrossworldApplication extends WebMvcAutoConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(CrossworldApplication.class, args);
    }

}
