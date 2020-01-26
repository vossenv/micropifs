package com.dm.micropifs;

import com.dm.micropifs.data.DataStore;
import com.dm.micropifs.model.PiImage;
import com.dm.micropifs.util.ExtendedLogger;
import org.apache.catalina.core.ApplicationPart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class MicroController {

    private final ExtendedLogger el;
    private final DataStore ds;
    private final MicroConfiguration mc;
    private final static Logger logger = LogManager.getLogger(MicrocamPifs.class);
    private final static Logger audit = LogManager.getLogger("DataStore.Audit");

    @Inject
    public MicroController(ExtendedLogger el, DataStore ds, MicroConfiguration mc) {
        this.el = el;
        this.ds = ds;
        this.mc = mc;
    }

    @ResponseBody
    @RequestMapping(value = {"/status"}, method = RequestMethod.GET)
    public String status(HttpServletRequest request) {
        logger.info(el.getRequestString(request) + ": Status returned true");
        return "version 1.0";
    }

    @ResponseBody
    @RequestMapping(value = {"/check"}, method = RequestMethod.GET)
    public String check(HttpServletRequest request) {
        logger.info(el.getRequestString(request) + ": Status returned true");
        return "true";
    }

    @ResponseBody
    @RequestMapping(value = {"/iplist"}, method = RequestMethod.GET)
    public Object iplist() {
        return mc.getIpAddressMap();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object index() {
        return "/static/index.html";
    }

    @ResponseBody
    @RequestMapping(value = {"/camlist"}, method = RequestMethod.GET)
    public Object getCamList() {
        return ds.getCameraList();
    }

    @ResponseBody
    @RequestMapping(value = {"/store"}, method = RequestMethod.POST)
    public Object storeFile(@RequestParam MultipartFile file, HttpServletRequest request) {
        try {
            return ds.store(request, file);
        } catch (Exception e) {
            audit.error(el.getRequestError(file, request, e));
            return e.getMessage();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/cameras/{camID}/update", method = RequestMethod.POST, produces = "application/json")
    public Object updateCameraDeque(@RequestParam MultipartFile file, HttpServletRequest request, @PathVariable("camID") String camID) {
        try {
            camID = camID.toLowerCase();
            return "Success --> total count for '" + camID + "' is " + ds.updateCam(request, file, camID).toString();
        } catch (Exception e) {
            logger.error("Controller --" + el.getRequestError(file, request, e));
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/cameras/next", method = RequestMethod.GET)
    public ResponseEntity<Object> getNextCameraImages(HttpServletRequest request, @RequestParam(required = false) List<String> cams) {
        try {

            List<PiImage> results = new ArrayList<>();
            Set<String> cameraList;

            if (null != cams && (!cams.isEmpty())) {
                cameraList = cams.stream().map(String::trim).collect(Collectors.toSet());
            } else {
                cameraList = ds.getCameraList();
            }

            cameraList.forEach(c -> {
                try {
                    PiImage p = ds.getNext(c);
                    p.setIp(mc.getCamIp(c));
                    results.add(p);
                } catch (NullPointerException e) { /* Do noting - this image was not found*/}
            });

            return ResponseEntity
                    .ok()
                    .header("Image-Count", String.valueOf(cameraList.size()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(results);
        } catch (Exception e) {
            return generateExceptionResponse(request, e);
        }
    }

    @RequestMapping(value = "/cameras/{camID}/next", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Object> getNextCameraImage(@PathVariable("camID") String camID, HttpServletRequest request) {
        try {
            PiImage next = ds.getNext(camID.toLowerCase());
            return ResponseEntity
                    .ok()
                    .headers(next.getHeaders())
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(next.getImage());
        } catch (Exception e) {
            return generateExceptionResponse(request, e);
        }
    }

    private ResponseEntity generateExceptionResponse(HttpServletRequest request, Exception e) {
        logger.error(el.getRequestError(request, e));
        String type = e.getClass().getCanonicalName();
        String msg = type + ": " + (type.contains("NullPointer")
                ? "No content stored for requested source... "
                : e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.TEXT_PLAIN)
                .body(msg);

    }
}

