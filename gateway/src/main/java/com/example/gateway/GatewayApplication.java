package com.example.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.example.common","com.example.gateway"})
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public CommandLineRunner printRoutes(RouteLocator routeLocator) {
        return args -> {
            System.out.println("=== 打印所有路由 ===");
            routeLocator.getRoutes().subscribe(route -> {
                System.out.println("Route ID: " + route.getId());
                System.out.println("  URI: " + route.getUri());
                System.out.println("  Predicate: " + route.getPredicate());
            });
        };
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        System.out.println("=== RouteLocator 被加载了！===");

        return builder.routes()
                .route("auth", r -> r
                        .path("/auth/**")
                        .uri("lb://auth"))
                .build();
    }
}
