package com.dm.micropifs;


import com.dm.micropifs.fileio.DataStore;
import com.dm.micropifs.model.PiImage;
import com.dm.micropifs.util.Deque;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MicroController {

    private final DataStore ds;
    private static List<PiImage> store = new Deque<>(5);
    private int count = 0;

    @Inject
    public MicroController(DataStore ds) {
        this.ds = ds;
    }

    @ResponseBody
    @RequestMapping(value = {"/status"}, method = RequestMethod.GET)
    public Object status() {
        return "true";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object index() {
        return "/static/index.html";
    }

    @RequestMapping(value = "/next", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Object> getNext() {
        PiImage next = store.get(0);
        return ResponseEntity
                .ok()
                .headers(next.getHeaders())
                .contentType(MediaType.IMAGE_JPEG)
                .body(next.getImage());
    }

    @ResponseBody
    @RequestMapping(value = {"/receive"}, method = RequestMethod.POST)
    public String uploadFile(@RequestParam MultipartFile file, HttpServletRequest request) throws Exception {
        store.add(new PiImage(request,file));
        this.count += 1;
        System.out.println(this.count);
        return "Success --> total count: " + this.count;
    }

    @ResponseBody
    @RequestMapping(value = {"/store"}, method = RequestMethod.POST)
    public String storeFile(@RequestParam MultipartFile file) throws Exception{
        return ds.store(file);
    }

}

