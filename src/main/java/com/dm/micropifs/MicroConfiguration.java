package com.dm.micropifs;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MicroConfiguration implements WebMvcConfigurer {

    @Value("${local.resource.path}")
    private String localResourcePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        localResourcePath = localResourcePath.replaceAll("\"","");
        localResourcePath = (!localResourcePath.endsWith("/")) ? localResourcePath + "/" : localResourcePath;


        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        registry
                .addResourceHandler("/files/**")
                .addResourceLocations(localResourcePath);
    }

    public String getLocalResourcePath() {
        return localResourcePath;
    }

    public void setLocalResourcePath(String localResourcePath) {
        this.localResourcePath = localResourcePath;
    }
}
