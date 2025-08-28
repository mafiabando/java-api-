package com.server.territoryapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan(basePackages = "com.server.territoryapi")
public class TerritoryApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TerritoryApiApplication.class, args);
    }

    @Component
    public class EndpointLogger {

        @Autowired
        private RequestMappingHandlerMapping requestMappingHandlerMapping;

        @EventListener(ContextRefreshedEvent.class)
        public void logAllEndpoints() {
            System.out.println("🔍 === all registered routes ===");
            requestMappingHandlerMapping.getHandlerMethods().forEach((key, value) -> {
                System.out.println("📌 " + key + " -> " + value.getMethod().getDeclaringClass().getSimpleName() + "." + value.getMethod().getName());
            });
            System.out.println("🔍 ====================================");
        }
    }
}