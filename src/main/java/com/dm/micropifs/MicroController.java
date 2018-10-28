package com.dm.micropifs;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Arrays;

@Controller
public class MicroController {

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

        ClassPathResource imgFile = new ClassPathResource("/static/frame.jpg");


        while (true) {
            byte[] bytesA = StreamUtils.copyToByteArray(imgFile.getInputStream());
            Thread.sleep(50);
            byte[] bytesB = StreamUtils.copyToByteArray(imgFile.getInputStream());

            if (validateImage(bytesA, bytesB)) {
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(bytesB);
            }

        }
    }


    private boolean validateImage(byte[] dataA, byte[] dataB){

       return Arrays.equals(dataA, dataB);

    }

}
//    @RequestMapping(value = "/fail", method = RequestMethod.GET, produces = MediaType.IMAGE_GIF_VALUE)
//    public ResponseEntity<InputStreamResource> getImageIRS() throws Exception {
//
//        ClassPathResource imgFile = new ClassPathResource("/static/frame.gif");
//
//        while (true){
//            byte[] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
//            if (validateImage(bytes)){
//                return ResponseEntity
//                        .ok()
//                        .contentType(MediaType.IMAGE_GIF)
//                        .body(new InputStreamResource(imgFile.getInputStream()));
//            }
//        }
//
//
//    }
//

