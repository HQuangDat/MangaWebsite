package com.example.MangaWebsite;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/Truyen/**")
                .addResourceLocations("file:E:/GCWT2/");
        registry.addResourceHandler("/User/**")
                .addResourceLocations("file:E:/GCWT2_User/");
    }

}
