package com.dm.micropifs.data;

import com.dm.micropifs.model.PiCamera;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class CameraMonitor implements Runnable {

    private final static Logger audit = LogManager.getLogger("CamMonitor.Audit");
    private Map<String, PiCamera> cameraMap;
    private int camTimeout;
    private int monitorCheckRate;


    public CameraMonitor(Map<String, PiCamera> cameraMap, int camTimeout, int monitorCheckRate) {
        this.cameraMap = cameraMap;
        this.camTimeout = camTimeout * 1000;
        this.monitorCheckRate = monitorCheckRate * 1000;

        audit.info("TEST");
    }

    @Override
    public void run() {
        while (true) {
            sleep(monitorCheckRate);
            cameraMap.forEach((k, v) -> {
                long delta = System.currentTimeMillis() - v.getLastTime();
                if (delta > camTimeout) {
                    cameraMap.remove(k);
                    audit.info("Camera " + v.getCamID() + " stopped " +
                            "responding after " + delta + " ms");
                }
            });
        }
    }

    private void sleep(int milli) {
        try {
            Thread.sleep(milli);
        } catch (InterruptedException e) {
        }
    }
}
