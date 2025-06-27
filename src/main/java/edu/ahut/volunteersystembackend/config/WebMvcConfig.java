package edu.ahut.volunteersystembackend.config;

import edu.ahut.volunteersystembackend.middleware.AdminMiddleware;
import edu.ahut.volunteersystembackend.middleware.AuthMiddleware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthMiddleware authMiddleware;
    private final AdminMiddleware adminMiddleware;

    @Autowired
    public WebMvcConfig(AuthMiddleware authMiddleware, AdminMiddleware adminMiddleware) {
        this.authMiddleware = authMiddleware;
        this.adminMiddleware = adminMiddleware;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册普通用户认证拦截器
        registry.addInterceptor(authMiddleware)
                .addPathPatterns("/user/**", "/task/**", "/admin/**", "/message/**")  // 需要认证的路径
                .excludePathPatterns("/api/login", "/api/register");  // 排除不需要认证的路径

        // 注册管理员认证拦截器
        registry.addInterceptor(adminMiddleware)
                .addPathPatterns("/task/**", "/admin/**");
    }
}