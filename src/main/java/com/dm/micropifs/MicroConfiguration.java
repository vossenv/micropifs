package com.dm.micropifs;


import com.dm.micropifs.data.DataStore;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class MicroConfiguration implements WebMvcConfigurer {

    private final Logger logger = LogManager.getLogger();

    @Value("${local.storage.path}")
    private String localStoragePath;

    private String smbUsername;

    private String smbPassword;

    @Value("${camera.buffer.size}")
    private int camBufferSize;

    @Value("${camera.timeout.seconds}")
    private int cameTimeout;

    @Value("${camera.check.seconds}")
    private int monitorCheckRate;

    private Map<String, String> ipAddressMap = new HashMap<>();

    @Inject
    private Environment env;

    @PostConstruct
    void setStoragePath() {

        if (localStoragePath.equals("default"))
            localStoragePath = new ApplicationHome(MicrocamPifs.class).getDir().getAbsolutePath();
        // localStoragePath = DataStore.fixPath(localStoragePath);




        logger.info("Local file storage path: " + localStoragePath);
        logger.info("Camera buffer side: " + camBufferSize);


        this.smbUsername = env.getProperty("smb.user.name");
        this.smbPassword = env.getProperty("smb.user.password");
        this.smbUsername = this.smbUsername != null ? this.smbUsername : System.getenv("SMB_USERNAME");
        this.smbPassword = this.smbPassword != null ? this.smbPassword : System.getenv("SMB_PASSWORD");
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

    public int getCameTimeout() {
        return cameTimeout;
    }

    public int getMonitorCheckRate() {
        return monitorCheckRate;
    }

    public Map<String, String> getIpAddressMap() {
        return ipAddressMap;
    }

    public String getCamId(String camIp) {

        for (String k : ipAddressMap.keySet()) {
            if (ipAddressMap.get(k).equals(camIp)) {
                return k;
            }
        }
        return "unknown";
    }

    public String getCamIp(String camId) {

        if (ipAddressMap.containsKey(camId)) {
            return ipAddressMap.get(camId);
        }
        return "unknown";
    }

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR" };

    public static String getClientIpAddress(HttpServletRequest request) {
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    public String getSmbPassword() {
        return smbPassword;
    }

    public void setSmbPassword(String smbPassword) {
        this.smbPassword = smbPassword;
    }

    public String getSmbUsername() {
        return smbUsername;
    }

    public void setSmbUsername(String smbUsername) {
        this.smbUsername = smbUsername;
    }
}

