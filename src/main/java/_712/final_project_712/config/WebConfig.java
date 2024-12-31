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
                .addPathPatterns("/file/**", "/favorite/**")
                .excludePathPatterns(
                        "/file/avatar/*/view/**",
                        "/kaptcha/**",
                        "/user/login",
                        "/user/register",
                        "/email/captcha/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/product/**"
                );
    }
}