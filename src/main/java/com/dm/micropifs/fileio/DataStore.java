package com.dm.micropifs.fileio;

import com.dm.micropifs.MicroConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.File;

@Service
public class DataStore {

    private final MicroConfiguration mc;

    @Inject
    public DataStore(MicroConfiguration microConfiguration) {
        this.mc = microConfiguration;
    }

    public String store(MultipartFile file) throws Exception{

        String fullpath = mc.getLocalStoragePath() + file.getOriginalFilename();
        file.transferTo(new File(fullpath));
        return "File succesfully stored: " + fullpath;
    }


}
