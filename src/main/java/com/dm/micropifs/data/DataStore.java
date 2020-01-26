package com.dm.micropifs.data;

import com.dm.micropifs.MicroConfiguration;
import com.dm.micropifs.model.PiCamera;
import com.dm.micropifs.model.PiImage;
import com.dm.micropifs.util.ExtendedLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
public class DataStore {

    private int bufferSize;
    private final ExtendedLogger el;
    private final MicroConfiguration mc;
    private final String sep = File.separator;
    private Map<String, PiCamera> cameraMap = new ConcurrentHashMap<>();
    private final static Logger audit = LogManager.getLogger("DataStore.Audit");
    private BigDecimal totalData = new BigDecimal(0);
    private CameraMonitor cm;


    @Inject
    public DataStore(ExtendedLogger el, MicroConfiguration microConfiguration) {
        this.el = el;
        this.mc = microConfiguration;
        this.bufferSize = mc.getCamBufferSize();
        this.cm = new CameraMonitor(cameraMap, mc.getCameTimeout(), mc.getMonitorCheckRate());
        Thread th = new Thread(this.cm);
        th.start();
    }

    public void updateCam(PiImage image, String camID) {
        if (cameraMap.containsKey(camID)) {
            cameraMap.get(camID).addImage(image);
        } else {
            mc.getIpAddressMap().put(camID, "LLoyd Remote");
            audit.info("New device discovered! Creating entry from internal API for " + camID + " Map size: " + (cameraMap.size() + 1));
            cameraMap.put(camID, new PiCamera(bufferSize, camID, image));
        }
    }

    public Object updateCam(HttpServletRequest request, MultipartFile file, String camID) throws Exception {
        mc.getIpAddressMap().put(camID, request.getRemoteAddr());
        if (cameraMap.containsKey(camID)) {
            return cameraMap.get(camID).addImage(new PiImage(request, file, camID));
        } else {
            audit.info(el.getRequestProcess("New device discovered! Creating entry for " + camID, file, request) + " Map size: " + (cameraMap.size() + 1));
            cameraMap.put(camID, new PiCamera(bufferSize, camID, new PiImage(request, file, camID)));
            return 1;
        }
    }

    public PiImage getNext(String camID) {
        return cameraMap.get(camID).getNext();
    }

    public Set<String> getCameraList() {
        return this.cameraMap.keySet();
    }

    public String store(HttpServletRequest request, MultipartFile file) throws Exception {

        String path = request.getHeader("File-Destination");
        String type = request.getHeader("Path-Type");
        String prepend = type != null && type.equals("absolute") ? "" : mc.getLocalStoragePath();
        path = fixPath(path == null || path.isEmpty() ? mc.getLocalStoragePath() : prepend + sep + path);

        new File(path).mkdirs();
        String fullpath = path + sep + file.getOriginalFilename();

        this.totalData = this.totalData.add(el.getFileSize(file.getSize()));
        audit.info(el.getRequestProcess("Video upload to " + prepend, file, request) + " ---> total data = " + this.totalData.setScale(3, RoundingMode.HALF_UP).toString() + " MB");

        file.transferTo(new File(fullpath));
        return "File succesfully stored: " + fullpath;
    }

    public static String fixPath(String path) {
        String pathOut = path
                .replace("\\", File.separator)
                .replace("/", File.separator)
                .replace("\"", "")
                .replace("'", "");

        pathOut = String.join(File.separator, splitPath(pathOut));
        audit.trace("Correcting path: " + path + " to " + pathOut);
        return pathOut;
    }

    private static List<String> splitPath(String path) {
        List<String> dirs = new LinkedList<>();
        String pattern = Pattern.quote(System.getProperty("file.separator"));
        for (String f : path.split(pattern)) if (!f.isEmpty()) dirs.add(f);
        return dirs;
    }



}


