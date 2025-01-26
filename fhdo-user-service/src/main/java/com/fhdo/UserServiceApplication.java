package com.fhdo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.fhdo")
public class UserServiceApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}