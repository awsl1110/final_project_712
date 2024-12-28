package _712.final_project_712.config;

import _712.final_project_712.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                    "/api/file/avatar/*/**",  // 排除头像访问路径
                    "/api/email/captcha/**",  // 排除验证码相关接口
                    "/swagger-ui/**",         // 排除Swagger相关路径
                    "/v3/api-docs/**"
                );
    }
} 