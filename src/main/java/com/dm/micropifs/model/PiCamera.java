package com.dm.micropifs.model;

import com.dm.micropifs.util.Deque;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PiCamera {

    private List<PiImage> store;
    private int bufferSize;
    private String camID;
    private int total = 0;
    private long lastTime = System.currentTimeMillis();
    private final static Logger audit = LogManager.getLogger("DataStore.Audit");

    public PiCamera(int bufferSize, String camID, PiImage init) {
        this.bufferSize = bufferSize;
        this.camID = camID;
        this.store = new Deque<>(bufferSize);
        this.store.add(init);
    }

    public int addImage(PiImage image){
        this.store.add(image);
        count();
        return this.total;
    }

    private void count(){
        double rateInterval = 1000.0;
        this.total += 1;
        if ((this.total % rateInterval) == 0){
            long currentime = System.currentTimeMillis();
            double elapsed = currentime - this.lastTime;

            BigDecimal rate = new BigDecimal(String.valueOf(rateInterval))
                    .divide(new BigDecimal(String.valueOf(elapsed*0.001)),10,RoundingMode.HALF_UP)
                    .setScale(2,RoundingMode.HALF_UP);

            this.lastTime = currentime;
            audit.trace(this.camID + ": Frame rate: " + rate.toString() + " /s");
        }
    }

    public PiImage getNext() {
        return this.store.get(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PiCamera piCamera = (PiCamera) o;

        if (bufferSize != piCamera.bufferSize) return false;
        if (store != null ? !store.equals(piCamera.store) : piCamera.store != null) return false;
        return camID != null ? camID.equals(piCamera.camID) : piCamera.camID == null;
    }

    @Override
    public int hashCode() {
        int result = store != null ? store.hashCode() : 0;
        result = 31 * result + bufferSize;
        result = 31 * result + (camID != null ? camID.hashCode() : 0);
        return result;
    }
}
