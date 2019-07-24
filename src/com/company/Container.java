package com.company;

import java.util.ArrayList;

public class Container {
    private int idC;
    private ArrayList token;

    public Container (int idC) {
        this.idC=idC;
        token = new ArrayList();
    }
    //estrae il primo token del container
    public synchronized  int getToken() {
        int r= (int) token.remove(0);
        System.out.println("Took "+r+" from the container #"+(idC+1)+" : "+token.size());
        return r;
    }
    //inserisce nel container un token in coda
    public synchronized void putToken(int v) {
        token.add(v);
        System.out.println("Put "+v+" into the container #"+(idC+1)+" : "+token.size());
    }

    public synchronized boolean emptyToken() {
        return token.isEmpty();
    }
}
