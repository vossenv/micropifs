package com.dm.micropifs.model;

import com.dm.micropifs.util.Deque;

import java.util.List;

public class PiCamera {

    private List<PiImage> store;
    private int bufferSize;
    private String camID;
    private int total = 0;

    public PiCamera(int bufferSize, String camID) {
        this.bufferSize = bufferSize;
        this.camID = camID;
        this.store = new Deque<>(bufferSize);
    }

    public PiCamera(int bufferSize, String camID, PiImage init) {
        this.bufferSize = bufferSize;
        this.camID = camID;
        this.store = new Deque<>(bufferSize);
        this.store.add(init);
    }

    public int addImage(PiImage image){
        this.store.add(image);
        this.total += 1;
        return this.total;
    }

    public PiImage getNext() {
        return this.store.get(0);
    }

    public List<PiImage> getStore() {
        return store;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public String getCamID() {
        return camID;
    }

    public int getTotal() {
        return total;
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
