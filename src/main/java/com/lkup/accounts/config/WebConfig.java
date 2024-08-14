package com.lkup.accounts.config;

import com.lkup.accounts.context.RequestHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String[] EXCLUDE_API = {"/api/v1/auth/**", "/swagger-ui/**", "/api-docs/**",  "/v3/api-docs/**", "/swagger-resources/**", "/swagger-resources", "/gtp-config-ui/**" ,"/swagger-ui.html"};

    private final RequestHandlerInterceptor requestHandlerInterceptor;

    public WebConfig(RequestHandlerInterceptor requestHandlerInterceptor) {
        this.requestHandlerInterceptor = requestHandlerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestHandlerInterceptor).addPathPatterns("/api/**").excludePathPatterns(EXCLUDE_API);
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
