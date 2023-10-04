package com.example.ushort;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication()
public class UshortApplication {

    public static void main(String[] args) {
        SpringApplication.run(UshortApplication.class, args);
    }

}
