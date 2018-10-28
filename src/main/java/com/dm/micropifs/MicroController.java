package com.dm.micropifs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

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

    @RequestMapping(value = {"/status"}, method = RequestMethod.GET)
    @ResponseBody
    public Object status(){
        return "true";
    }


    @RequestMapping(value = "/frame", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage() throws Exception {

        while (true) {

            byte [] imageA = getImage(localResourcePath + "frame.jpg");
            Thread.sleep(50);
            byte [] imageB = getImage(localResourcePath + "frame.jpg");

            if (validateImage(imageA, imageB)) {
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(imageA);
            }

        }
    }


    private byte[] getImage (String path) throws IOException {

        File jpegFile = new File(path);
        BufferedImage image = ImageIO.read(jpegFile);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos );
        return baos.toByteArray();
    }


    private boolean validateImage(byte[] dataA, byte[] dataB){

       return Arrays.equals(dataA, dataB);

    }

}

