package com.dm.microcam;


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

        String dir = this.localResourcePath.replaceAll("\"","");
        dir = (!dir.endsWith("/")) ? dir + "/" : dir;
        dir = (dir.startsWith("classpath")) ? dir : "file:" + dir;


        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        registry
                .addResourceHandler("/files/**")
                .addResourceLocations(dir);
    }

}
