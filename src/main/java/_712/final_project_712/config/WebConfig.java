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
                .addPathPatterns(
                        "/api/file/**", 
                        "/api/favorite/**", 
                        "/api/order/**", 
                        "/api/review/**",
                        "/api/address/**",
                        "/api/pickup/**",
                        "/api/return-orders/**",
                        "/api/sales/**",
                        "/api/user/profile/**",
                        "/api/activity/participate/**"
                )
                .excludePathPatterns(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/api/product/**",
                        "/api/review/list",
                        "/api/review/*/detail",
                        "/api/file/avatar/*/view/**",
                        "/api/kaptcha/**",
                        "/api/user/login",
                        "/api/user/register",
                        "/api/email/captcha/**",
                        "/api/activity/ongoing",
                        "/api/activity/upcoming",
                        "/api/activity/{id}"
                );
        registry.addInterceptor(supplierTokenInterceptor)
                .addPathPatterns("/supplier/**");
    }
}