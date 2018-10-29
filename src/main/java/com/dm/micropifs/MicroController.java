package com.dm.micropifs;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MicroController {

    @Value("${local.resource.path}")
    String localResourcePath;

    @PostConstruct
    public void SetPath(){
        localResourcePath = localResourcePath.replaceAll("\"","");
        localResourcePath = (!localResourcePath.endsWith("/")) ? localResourcePath + "/" : localResourcePath;
    }

    @RequestMapping(value = {"/frame_simple"}, method = RequestMethod.GET)
    public Object getFrame(){
        return "/files/frame.jpg";
    }

    @RequestMapping(value = {"/log"}, method = RequestMethod.GET)
    public Object getLog(){
        return "/files/camlog.txt";
    }

    @RequestMapping(value = {"/status"}, method = RequestMethod.GET)
    @ResponseBody
    public Object status(){
        return "true";
    }

    @RequestMapping(value = "/frame", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getFrameData() throws Exception{

        byte [] image = getCurrentFrame();
        HttpHeaders headers = new HttpHeaders();

        try {
            buildHeaderList(getCurrentLog()).forEach(headers::add);
        } catch (Exception e){
            headers.add("Data-Error", e.getMessage());
        }

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

    private Map<String,String> buildHeaderList(String raw){

        Map<String,String> headers = new HashMap<>();

        String nl = raw.contains("\r\n") ? "\r\n" : "\n";
        String [] lines = raw.split(nl);

        for (String l : lines) {

            String [] pair = l.split(":");
            String k = pair[0].replaceAll("[\\s_]","-");
            String v = (pair.length > 1) ? l.substring(k.length()+1,l.length()-1) : "no data";

            headers.put(k.trim(), v.trim());
        }
        return headers;
    }

    private byte [] getCurrentFrame() throws Exception{

        for (int i = 0; i < 100; i++){
            byte[] img = readFrame();
            Thread.sleep(10);
            if (validateData(img, readFrame())) { return img; }
        }

        throw new IOException("Error reading image");
    }


    private String getCurrentLog() throws Exception {


        for (int i = 0; i < 100; i++){
            byte[] data = readLog();
            Thread.sleep(10);
            if (validateData(data, readLog())){ return new String(data); }
        }

        throw new IOException("Error reading logfile");

    }

    private byte[] readLog() throws Exception {

        File f = new File(localResourcePath + "camlog.txt");
        return Files.readAllBytes(f.toPath());

    }

    private byte[] readFrame() throws Exception {

        File jpegFile = new File(localResourcePath + "frame.jpg");
        BufferedImage image = ImageIO.read(jpegFile);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos );
        return baos.toByteArray();
    }

    private boolean validateData(byte[] dataA, byte[] dataB){
       return Arrays.equals(dataA, dataB);
    }

}

