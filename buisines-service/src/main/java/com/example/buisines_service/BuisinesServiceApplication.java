package com.example.buisines_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BuisinesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuisinesServiceApplication.class, args);
    }

}
