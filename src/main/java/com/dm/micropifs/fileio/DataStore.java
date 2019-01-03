package com.dm.micropifs.fileio;

import com.dm.micropifs.MicroConfiguration;
import com.dm.micropifs.model.PiCamera;
import com.dm.micropifs.model.PiImage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class DataStore {

    private int bufferSize;
    private final MicroConfiguration mc;
    private final String sep = File.separator;
    private Map <String, PiCamera> cameraMap = new HashMap<>();

    @Inject
    public DataStore(MicroConfiguration microConfiguration) {
        this.mc = microConfiguration;
        this.bufferSize = mc.getCamBufferSize();
    }

    public Object updateCam(HttpServletRequest request, MultipartFile file, String camID) throws Exception {

        if (cameraMap.containsKey(camID)){
            return cameraMap.get(camID).addImage(new PiImage(request,file));
        } else {
            cameraMap.put(camID, new PiCamera(bufferSize,camID, new PiImage(request,file)));
            return 1;
        }
    }

    public PiImage getNext(String camID) {
        return cameraMap.get(camID).getNext();
    }

    public String store(HttpServletRequest request, MultipartFile file) throws Exception {

        String path = request.getHeader("File-Destination");
        String type = request.getHeader("Path-Type");
        String prepend = type != null && type.equals("absolute") ? "" : mc.getLocalStoragePath();
        path = fixPath(path == null || path.isEmpty() ? mc.getLocalStoragePath() : prepend + sep + path);

        new File(path).mkdirs();
        String fullpath = path + sep + file.getOriginalFilename();
        file.transferTo(new File(fullpath));
        return "File succesfully stored: " + fullpath;
    }

    public static String fixPath(String path) {
        path = path
                .replace("\\", File.separator)
                .replace("/", File.separator)
                .replace("\"", "")
                .replace("'", "");

        return String.join(File.separator, splitPath(path));
    }

    private static List<String> splitPath(String path) {
        List<String> dirs = new LinkedList<>();
        String pattern = Pattern.quote(System.getProperty("file.separator"));
        for (String f : path.split(pattern)) if (!f.isEmpty()) dirs.add(f);
        return dirs;
    }

}
