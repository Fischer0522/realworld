package com.fischer.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Value("${web.upload-path}")
    private String uploadFolder;

    @Bean
    public MyInterceptor myInterceptor(){
        return new MyInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor())
                 .addPathPatterns("/**")
                .excludePathPatterns("/users/login")
                .excludePathPatterns("/users/register")
                .excludePathPatterns("/articles/exact")
                .excludePathPatterns("/articles/fuzzy")
                .excludePathPatterns("/img/**")
                .excludePathPatterns("/articles/{slug}/comments")
                .excludePathPatterns("/tags")
                .excludePathPatterns("/email")
                .excludePathPatterns("/profiles/{username}");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations("file:"+uploadFolder);
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
