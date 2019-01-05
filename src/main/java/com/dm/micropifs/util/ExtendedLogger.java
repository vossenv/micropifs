package com.dm.micropifs.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ExtendedLogger {

    private String getFileString(MultipartFile file) {

        try {

            String fileSize = new BigDecimal(file.getSize()).movePointLeft(6).setScale(3, RoundingMode.HALF_UP).toString() + " MB";
            String fileName = file.getOriginalFilename();

            return "{ filename: " + fileName + ", size: " + fileSize + " }";

        } catch (Exception e) {
            return  "Failed to retrieve details...";
        }

    }

    final public String getRequestString(HttpServletRequest request) {

        String requestURI = request.getRequestURI();
        String requestType = request.getMethod();
        String remoteIP = request.getRemoteAddr();

        return requestType + " request from " + remoteIP + " on " + requestURI;
    }

    final public String getRequestError(HttpServletRequest request, Exception e){
        return getRequestString(request) +  ": Exception encoundered during request: " + e.getMessage();
    }

    final public String getRequestError(MultipartFile file, HttpServletRequest request, Exception e){

        return getRequestString(request) +  ": Exception encoundered during request: " + e.getMessage() + " --- File details: " + getFileString(file);
    }

    final public String getRequestProcess(String msg, MultipartFile file, HttpServletRequest request){

        return getRequestString(request) +  ": " + msg + ": " + getFileString(file);
    }


}
