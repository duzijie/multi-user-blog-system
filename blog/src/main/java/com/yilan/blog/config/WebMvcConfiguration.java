package com.yilan.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/img/avatar/**").addResourceLocations("file:/opt/img/avatar/");
//        registry.addResourceHandler("/img/blog/**").addResourceLocations("file:/opt/img/blog/");
      registry.addResourceHandler("/img/avatar/**").addResourceLocations("file:D:/img/avatar/");
      registry.addResourceHandler("/img/blog/**").addResourceLocations("file:D:/img/blog/");
    }
}
