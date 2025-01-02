package _712.final_project_712.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi defaultApi() {
        return GroupedOpenApi.builder()
                .group("全部")
                .packagesToScan("_712.final_project_712.controller")
                .build();
    }
    @Bean
    public GroupedOpenApi userApi() {
        String[] paths = {"/api/user/**", "/api/kaptcha/**", "/api/email/**", 
                "/api/file/avatar/**", "/api/user-coupons/**", "/api/address/**", 
                "/api/user/profile/**"};
        return GroupedOpenApi.builder()
                .group("用户相关")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi productApi() {
        String[] paths = {"/api/product/**", "/api/favorites/**", "/api/review/**",
                "/api/cart/**", "/api/supplier/**"};
        return GroupedOpenApi.builder()
                .group("商品相关")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi activityApi() {
        String[] paths = {"/api/activity/**"};
        return GroupedOpenApi.builder()
                .group("活动相关")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi orderApi() {
        String[] paths = {"/api/order/**", "/api/return-orders/**", "/api/pickup/**"};
        return GroupedOpenApi.builder()
                .group("订单相关")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi statisticsApi() {
        String[] paths = {"/api/sales/**"};
        return GroupedOpenApi.builder()
                .group("统计相关")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("后台管理系统 - 接口文档")
                        .version("v1.0")
                        .description("后台管理系统接口文档")
                        .contact(new Contact().name("712")))
                .components(new Components()
                        .addSecuritySchemes(HttpHeaders.AUTHORIZATION,
                                new SecurityScheme()
                                        .name(HttpHeaders.AUTHORIZATION)
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .scheme("Bearer")
                                        .bearerFormat("JWT")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION));
    }
}
