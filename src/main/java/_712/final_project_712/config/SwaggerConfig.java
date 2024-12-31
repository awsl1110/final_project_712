package _712.final_project_712.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("用户相关")
                .pathsToMatch("/user/**", "/kaptcha/**", "/email/**", "/file/avatar/**", "/user-coupons/**")
                .build();
    }

    @Bean
    public GroupedOpenApi productApi() {
        return GroupedOpenApi.builder()
                .group("商品相关")
                .pathsToMatch("/product/**", "/favorite/**", "/review/**", "/order/**", "/cart/**")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        StringBuilder desc = new StringBuilder();
        desc.append("后台管理系统接口文档\n\n");
        desc.append("接口分组说明：\n");
        desc.append("1. 用户相关：用户认证（登录、注册）、个人信息管理、地址管理、头像管理、验证码、优惠券等接口\n");
        desc.append("2. 商品相关：商品管理、收藏、订单、评价、购物车等接口\n");
        
        return new OpenAPI()
                .info(new Info()
                        .title("后台管理系统 - 接口文档")
                        .description(desc.toString())
                        .version("V1.0")
                        .contact(new Contact().name("712"))
                )
                .components(new Components()
                        .addSecuritySchemes(HttpHeaders.AUTHORIZATION,
                                new SecurityScheme()
                                        .name(HttpHeaders.AUTHORIZATION)
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .scheme("Bearer")
                                        .bearerFormat("JWT")
                        )
               );
    }

    @Bean
    public GlobalOpenApiCustomizer globalOpenApiCustomizer() {
        return openApi -> {
            // 全局添加鉴权参数
            if (openApi.getPaths() != null) {
                openApi.getPaths().forEach((s, pathItem) -> {
                    // 登录接口/验证码/注册等不需要添加鉴权参数
                    if (s.startsWith("/kaptcha") ||
                            s.equals("/user/login") ||
                            s.equals("/user/register") ||
                            s.equals("/email/captcha/send") ||
                            s.equals("/review/list") ||
                            s.startsWith("/product/list") ||
                            s.startsWith("/product/detail")) {
                        return;
                    }
                    // 接口添加鉴权参数
                    pathItem.readOperations()
                            .forEach(operation ->
                                    operation.addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION))
                            );
                });
            }
        };
    }
}
