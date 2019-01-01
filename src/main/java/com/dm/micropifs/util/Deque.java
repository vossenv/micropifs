package com.dm.micropifs.util;

import java.util.LinkedList;


public class Deque<Object> extends LinkedList<Object> {

    private int maxsize;

    public Deque(int maxsize){
        this.maxsize = maxsize;
    }

    @Override
    public boolean add(Object object) {
        boolean result;
        if(this.size() < this.maxsize)
            result = super.add(object);
        else
        {
            super.removeFirst();
            result = super.add(object);
        }
        return result;
    }

}
