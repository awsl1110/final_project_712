package _712.final_project_712.config;

import _712.final_project_712.interceptor.JwtInterceptor;
import _712.final_project_712.interceptor.SupplierTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Autowired
    private SupplierTokenInterceptor supplierTokenInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 添加 Knife4j 资源处理
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        // 处理其他静态资源
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource requestedResource = location.createRelative(resourcePath);
                        // 排除 Knife4j 相关路径
                        if (resourcePath.startsWith("doc.html") ||
                            resourcePath.startsWith("webjars") ||
                            resourcePath.startsWith("v3/api-docs") ||
                            resourcePath.startsWith("swagger-ui")) {
                            return null;
                        }
                        return requestedResource.exists() && requestedResource.isReadable() ? requestedResource
                                : new ClassPathResource("/static/index.html");
                    }
                });
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 将非API请求转发到index.html
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/dashboard/**").setViewName("forward:/index.html");
        registry.addViewController("/cart/**").setViewName("forward:/index.html");
        registry.addViewController("/login/**").setViewName("forward:/index.html");
        registry.addViewController("/register/**").setViewName("forward:/index.html");
        registry.addViewController("/home/**").setViewName("forward:/index.html");
        registry.addViewController("/user/**").setViewName("forward:/index.html");
        registry.addViewController("/product/**").setViewName("forward:/index.html");
        registry.addViewController("/order/**").setViewName("forward:/index.html");
        registry.addViewController("/activity/**").setViewName("forward:/index.html");
        registry.addViewController("/supplier/**").setViewName("forward:/index.html");
    }

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
                        "/doc.html",
                        "/webjars/**",
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