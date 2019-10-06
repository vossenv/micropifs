package com.dm.micropifs.websource;

import com.dm.micropifs.data.DataStore;
import com.dm.micropifs.model.PiImage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AlarmRunner {

    @Inject
    private DataStore ds;

    @Inject
    private ApplicationContext context;

    private int sleeptime = 250;
    private Map<String, String> lloydCams =  Stream.of(new String[][]{
                    {"2048", "living_room"},
                    {"2049", "garage"},
            }).collect(Collectors.toMap(d -> d[0], d -> d[1]));

    @PostConstruct
    void startRun() {
        startCam();
    }

    private void startCam() {

        lloydCams.forEach((c, n) -> {
            new Thread(() -> {

                AlarmConnector ac = context.getBean(AlarmConnector.class);
                Logger logger = LogManager.getLogger(AlarmRunner.class + " - " + c);
                logger.info("Initializing thread for alarm api for id: " + c);

                while (true) {
                    try {
                        ds.updateCam(new PiImage(ac.getImage(c), new HttpHeaders(), c), n);
                        Thread.sleep(this.sleeptime);
                    } catch (Exception e) {
                        logger.error("Failed to get new alarm.com image: " + e.getMessage());
                    }
                }
            }).start();
        });
    }

}
