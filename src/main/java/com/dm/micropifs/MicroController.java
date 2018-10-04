package com.dm.micropifs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MicroController {

    @RequestMapping(value = {"/frame"}, method = RequestMethod.GET)
    public Object getFrame(){

        return "/files/frame.jpg";

    }

    @RequestMapping(value = {"/status"}, method = RequestMethod.GET)
    @ResponseBody
    public Object status(){

        return "true";
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
