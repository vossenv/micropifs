package com.dm.micropifs.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;

public class PiImage {

    private String source;
    private String ip = "unknown";
    private HttpHeaders headers = new HttpHeaders();
    private byte[] image;

    public PiImage(byte[] image, HttpHeaders headers, String source) {
        this.image = image;
        this.headers = headers;
        this.source = source;
    }

    public PiImage(HttpServletRequest request, MultipartFile file, String source) throws IOException {

        this.image = file.getBytes();
        this.source = source;

        try {
            (new ObjectMapper()
                    .readValue(request.getHeader("Metadata").replaceAll("'", "\""), HashMap.class))
                    .forEach((k, v) -> this.headers.add(k.toString(), v.toString()));
            this.headers.add("Camera-Name", source);

        } catch (Exception e) {
            this.headers.add("Metadata-Error", "Error or no data present: " + e.getMessage());
        }
    }

    public HttpHeaders getHeaders() {
        return headers;
    }
    public byte[] getImage() {
        return image;
    }
    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }
    public String getSource() {return source;}
    public void setSource(String source) {this.source = source;}
    public String getIp() {return ip;}
    public void setIp(String ip) {this.ip = ip;}
}
