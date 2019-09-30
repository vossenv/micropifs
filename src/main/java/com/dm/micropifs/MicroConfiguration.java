package com.dm.micropifs;


import com.dm.micropifs.data.DataStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;


@Configuration
public class MicroConfiguration implements WebMvcConfigurer {

    private final Logger logger = LogManager.getLogger();

    @Value("${local.storage.path}")
    private String localStoragePath;

    @Value("${camera.buffer.size}")
    private int camBufferSize;

    @Value("${camera.timeout.seconds}")
    private int cameTimeout;

    @Value("${camera.check.seconds}")
    private int monitorCheckRate;

    @PostConstruct
    void setStoragePath() {

        if (localStoragePath.equals("default"))
            localStoragePath = new ApplicationHome(MicrocamPifs.class).getDir().getAbsolutePath();
        localStoragePath = DataStore.fixPath(localStoragePath);

        logger.info("Local file storage path: " + localStoragePath);
        logger.info("Camera buffer side: " + camBufferSize);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classPath:/static/");
    }

    public String getLocalStoragePath() {
        return localStoragePath;
    }

    public int getCamBufferSize() {
        return camBufferSize;
    }

    public int getCameTimeout() {return cameTimeout;}

    public int getMonitorCheckRate() {return monitorCheckRate;}
}

