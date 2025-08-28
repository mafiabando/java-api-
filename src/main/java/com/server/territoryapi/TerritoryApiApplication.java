package com.server.territoryapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.server.territoryapi")
public class TerritoryApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TerritoryApiApplication.class, args);
    }
}
