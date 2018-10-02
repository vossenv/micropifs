package com.dm.microcam;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class MicroController {

    @RequestMapping(value = {"/simple"}, method = RequestMethod.GET)
    public Object getFrame(){
        return "/files/frame.jpg";
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
//    private boolean validateImage(byte[] data){
//
//        return true;
//
//    }

}
