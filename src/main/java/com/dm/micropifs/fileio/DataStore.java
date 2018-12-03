package com.dm.micropifs.fileio;

import com.dm.micropifs.MicroConfiguration;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

@Service
public class DataStore {

    private final MicroConfiguration mc;

    @Inject
    public DataStore(MicroConfiguration microConfiguration) {
        this.mc = microConfiguration;
    }

    public byte [] getCurrentFrame() throws Exception{

        for (int i = 0; i < 100; i++){
            byte[] img = readFrame();
            Thread.sleep(10);
            if (validateData(img, readFrame())) { return img; }
        }

        throw new IOException("Error reading image");
    }


    public String getCurrentLog() throws Exception {

        for (int i = 0; i < 100; i++){
            byte[] data = readLog();
            Thread.sleep(10);
            if (validateData(data, readLog())){ return new String(data); }
        }

        throw new IOException("Error reading logfile");

    }

    private byte[] readLog() throws Exception {

        File f = new File(mc.getLocalResourcePath() + "camlog.txt");
        return Files.readAllBytes(f.toPath());

    }

    private byte[] readFrame() throws Exception {

        File jpegFile = new File(mc.getLocalResourcePath() + "frame.jpg");
        BufferedImage image = ImageIO.read(jpegFile);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos );
        return baos.toByteArray();
    }

    private boolean validateData(byte[] dataA, byte[] dataB){
        return Arrays.equals(dataA, dataB);
    }


}
