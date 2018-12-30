package com.dm.micropifs;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MicroConfiguration implements WebMvcConfigurer {

    @Value("${local.resource.path}")
    private String localResourcePath;

    @Value("${local.storage.path}")
    private String localStoragePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        localResourcePath = localResourcePath.replaceAll("\"","");
        localResourcePath = (!localResourcePath.endsWith("/")) ? localResourcePath + "/" : localResourcePath;


        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

    }

    public String getLocalResourcePath() {
        return localResourcePath;
    }

    public String getLocalStoragePath() {
        return localStoragePath;
    }
}

