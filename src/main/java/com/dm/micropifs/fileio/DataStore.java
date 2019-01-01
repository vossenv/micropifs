package com.dm.micropifs.fileio;

import com.dm.micropifs.MicroConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class DataStore {

    private final MicroConfiguration mc;
    private final String sep = File.separator;

    @Inject
    public DataStore(MicroConfiguration microConfiguration) {
        this.mc = microConfiguration;
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
