package com.binar.apigateway.config;

import com.binar.apigateway.filter.AuthenticationPreFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {

    @Value("${service.client.userService.url}")
    private String userClient;
    @Value("${service.client.orderService.url}")
    private String orderClient;
    @Value("${service.client.filmService.url}")
    private String filmClient;
    @Value("${service.client.notificationService.url}")
    private String notificationClient;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder, AuthenticationPreFilter authFilter){
        return builder.routes()
                .route("user-service", routes -> routes.path("/user/**", "/role/**")
                        .filters(f ->
                                f.rewritePath("/user/(?<segment>/?.*)", "/user/$\\{segment}")
                                        .rewritePath("/role/(?<segment>/?.*)", "/role/$\\{segment}"))
                        .uri(userClient))

                .route("order-service", routes -> routes.path("/booking/**", "/cinema-hall/**", "/invoice/**", "/schedule/**", "/seat/**")
                        .filters(f ->
                                f.rewritePath("/booking/(?<segment>/?.*)", "/booking/$\\{segment}")
                                        .rewritePath("/cinema-hall/(?<segment>/?.*)", "/cinema-hall/$\\{segment}")
                                        .rewritePath("/invoice/(?<segment>/?.*)", "/invoice/$\\{segment}")
                                        .rewritePath("/schedule/(?<segment>/?.*)", "/schedule/$\\{segment}")
                                        .rewritePath("/seat/(?<segment>/?.*)", "/seat/$\\{segment}"))
                        .uri(orderClient))

                .route("film-service", route -> route.path("/film/**")
                        .filters(f ->
                                f.rewritePath("/film/(?<segment>/?.*)", "/film/$\\{segment}"))
                        .uri(filmClient))

                .route("notification-service", routes -> routes.path("/notification/**")
                        .filters(f ->
                                f.rewritePath("/notification/(?<segment>/?.*)", "/notification/$\\{segment}"))
                        .uri(notificationClient))
                .build();
    }
}
