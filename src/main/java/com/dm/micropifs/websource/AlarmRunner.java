package com.dm.micropifs.websource;

import com.dm.micropifs.MicrocamPifs;
import com.dm.micropifs.fileio.DataStore;
import com.dm.micropifs.model.PiImage;
import com.dm.micropifs.util.ExtendedLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@Component
public class AlarmRunner {

    @Inject private AlarmConnector ac;
    @Inject private DataStore ds;

    private int sleeptime = 500;
    private List<String> lloydCams = Arrays.asList("2048","2049");

    @PostConstruct
    void startRun(){
        startCam();
    }

    private void startCam(){

        lloydCams.forEach(c -> {
            new Thread(() -> {
                Logger logger = LogManager.getLogger(AlarmRunner.class + " - " + c);
                logger.info("Initializing thread for alarm api for id: " + c);

                while (true) {
                    try {
                        ds.updateCam(new PiImage(ac.getImage(c), new HttpHeaders()), c);
                        Thread.sleep(this.sleeptime);
                    }
                    catch (Exception e){
                        logger.error("Failed to get new alarm.com image: " + e.getMessage());
                    }
                }}).start();
        });
    }

}
