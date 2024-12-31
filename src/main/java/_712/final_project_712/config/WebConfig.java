package _712.final_project_712.config;

import _712.final_project_712.interceptor.JwtInterceptor;
import _712.final_project_712.interceptor.SupplierTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Autowired
    private SupplierTokenInterceptor supplierTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/file/**", "/favorite/**", "/order/**", "/review/**")
                .excludePathPatterns(
                        "/file/avatar/*/view/**",
                        "/kaptcha/**",
                        "/user/login",
                        "/user/register",
                        "/email/captcha/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/product/**",
                        "/review/list",
                        "/review/*/detail"
                );

        registry.addInterceptor(supplierTokenInterceptor)
                .addPathPatterns("/supplier/**");
    }
}