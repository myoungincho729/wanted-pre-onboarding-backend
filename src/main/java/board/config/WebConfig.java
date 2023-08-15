package board.config;

import board.interceptor.JwtInterceptor;
import board.jwt.JwtUtils;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final JwtUtils jwtUtils;

    public WebConfig(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor(jwtUtils))
                .addPathPatterns("/article/create", "/article/update/*", "/article/delete/*");
    }
}
