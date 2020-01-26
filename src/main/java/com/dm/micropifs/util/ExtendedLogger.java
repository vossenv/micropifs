package com.dm.micropifs.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ExtendedLogger {


    private String getFileString(MultipartFile file) {

        try {

            String fileSize = getFileSize(file.getSize()).toString() + " MB";
            String fileName = file.getOriginalFilename();

            return "{ filename: " + fileName + ", size: " + fileSize + " }";

        } catch (Exception e) {
            return "Failed to retrieve details...";
        }
    }

    private String getFileString(HttpServletRequest request) {
        try {

            Part p = request.getPart("file");
            String fileSize = getFileSize(p.getSize()) + " MB";
            String fileName = p.getSubmittedFileName();

            return " --- File details: { filename: " + fileName + ", size: " + fileSize + " }";

        } catch (Exception e) {
            return "";
        }
    }

    public BigDecimal getFileSize(long size) {
        return new BigDecimal(size).movePointLeft(6).setScale(3, RoundingMode.HALF_UP);
    }

    final public String getRequestString(HttpServletRequest request) {

        String requestURI = request.getRequestURI();
        String requestType = request.getMethod();
        String remoteIP = request.getRemoteAddr();

        return requestType + " request from " + remoteIP + " on " + requestURI;
    }

    final public String getRequestError(HttpServletRequest request, Exception e) {
        return getRequestString(request) + ": Exception encoundered during request: " + e.getMessage() + getFileString(request);
    }

    final public String getRequestError(MultipartFile file, HttpServletRequest request, Exception e) {
        return getRequestString(request) + ": Exception encoundered during request: " + e.getMessage() + getFileString(file);
    }

    final public String getRequestProcess(String msg, MultipartFile file, HttpServletRequest request) {
        return getRequestString(request) + ": " + msg + ": " + getFileString(file);
    }


}
