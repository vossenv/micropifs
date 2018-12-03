package com.dm.micropifs.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;

public class PiImage {

    private HttpHeaders headers = new HttpHeaders();
    private byte[] image;


    public PiImage(HttpServletRequest request, MultipartFile file) throws IOException {

        this.image = file.getBytes();

        try {
            (new ObjectMapper()
                    .readValue(request.getHeader("Metadata").replaceAll("'", "\""), HashMap.class))
                    .forEach((k,v) -> this.headers.add(k.toString(), v.toString()));

        } catch (Exception e){
            this.headers.add("Metadata-Error","Error or no data present: " + e.getMessage());
        }
    }


    public HttpHeaders getHeaders() {
        return headers;
    }

    public byte[] getImage() {
        return image;
    }
}
