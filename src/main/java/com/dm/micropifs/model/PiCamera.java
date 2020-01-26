package com.dm.micropifs.model;

import com.dm.micropifs.util.Deque;

import java.util.List;
import java.util.Objects;

public class PiCamera {

    private List<PiImage> store;
    private int bufferSize;
    private String camID;
    private int total = 0;
    private long lastTime = System.currentTimeMillis();

    public PiCamera(int bufferSize, String camID, PiImage init) {
        this(bufferSize, camID, "unknown", init);
    }

    public PiCamera(int bufferSize, String camID, String lastIP, PiImage init){
        this.bufferSize = bufferSize;
        this.camID = camID;
        this.store = new Deque<>(bufferSize);
        this.store.add(init);
    }

    public int addImage(PiImage image) {
        store.add(image);
        total += 1;
        lastTime = System.currentTimeMillis();
        return total;
    }

    public long getLastTime() {return lastTime;}
    public PiImage getNext() {return store.get(0);}
    public String getCamID() {return camID;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PiCamera piCamera = (PiCamera) o;

        if (bufferSize != piCamera.bufferSize) return false;
        if (total != piCamera.total) return false;
        if (lastTime != piCamera.lastTime) return false;
        if (!Objects.equals(store, piCamera.store)) return false;
        return Objects.equals(camID, piCamera.camID);
    }

    @Override
    public int hashCode() {
        int result = store != null ? store.hashCode() : 0;
        result = 31 * result + bufferSize;
        result = 31 * result + (camID != null ? camID.hashCode() : 0);
        result = 31 * result + total;
        result = 31 * result + (int) (lastTime ^ (lastTime >>> 32));
        return result;
    }
}
