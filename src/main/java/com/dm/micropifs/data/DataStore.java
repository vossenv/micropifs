package com.dm.micropifs.data;

import com.dm.micropifs.MicroConfiguration;
import com.dm.micropifs.model.PiCamera;
import com.dm.micropifs.model.PiImage;
import com.dm.micropifs.util.ExtendedLogger;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
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

    private Boolean isPathSMB() {
        return this.mc.getLocalStoragePath().startsWith("smb://");
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
        String remoteIP = MicroConfiguration.getClientIpAddress(request);
        mc.getIpAddressMap().put(camID, remoteIP);
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
        path = this.mc.getLocalStoragePath() + fixPath(path == null ? "" : path);
        String fullpath = path + sep + file.getOriginalFilename();

        if (isPathSMB()) {
            fullpath = storeSMB(file, path);
        } else {

            new File(path).mkdirs();
            file.transferTo(new File(fullpath));
        }

        this.totalData = this.totalData.add(el.getFileSize(file.getSize()));
        audit.info(el.getRequestProcess("Video upload to ", file, request) + " ---> total data = " + this.totalData.setScale(3, RoundingMode.HALF_UP).toString() + " MB");

        return "File succesfully stored: " + fullpath;
    }

    public String storeSMB(MultipartFile file, String storagePath) throws IOException {

        storagePath = storagePath.replace("\\", "/");
        NtlmPasswordAuthentication auth =
                new NtlmPasswordAuthentication("", this.mc.getSmbUsername(), this.mc.getSmbPassword());
        makeSmbDirs(storagePath, auth);
        storagePath += "/" + file.getOriginalFilename();
        SmbFile smbFile = new SmbFile(storagePath, auth);
        SmbFileOutputStream smbfos = new SmbFileOutputStream(smbFile);
        smbfos.write(file.getBytes());
        return storagePath;
    }

    private void makeSmbDirs(String path, NtlmPasswordAuthentication auth) throws SmbException, MalformedURLException {
        try {
            SmbFile smbFile = new SmbFile(path, auth);
            smbFile.mkdirs();
        } catch (SmbException e) {
            if (!e.getMessage().contains("already exists")) {
                throw e;
            }
        }
    }

    public String fixPath(String path) {

        if (null == path || path.isEmpty()) return "";

        String pathOut = path
                .replace("/", sep)
                .replace("\"", "")
                .replace("'", "");

        pathOut = pathOut.replace("\\", sep);
        pathOut = sep + String.join(sep, splitPath(pathOut));
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


