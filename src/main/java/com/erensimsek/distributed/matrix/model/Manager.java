package com.erensimsek.distributed.matrix.model;

import java.net.InetSocketAddress;

/**
 * Created by esimsek on 3/10/2017.
 */
public class Manager {
    private InetSocketAddress ınetSocketAddress;
    private int port;
    private int haveToGetTotallyByteLenght;

    public Manager(InetSocketAddress ınetSocketAddress, int port, int haveToGetTotallyByteLenght) {
        this.ınetSocketAddress = ınetSocketAddress;
        this.port = port;
        this.haveToGetTotallyByteLenght = haveToGetTotallyByteLenght;
    }

    public InetSocketAddress getInetSocketAddress() {
        return ınetSocketAddress;
    }

    public void setInetSocketAddress(InetSocketAddress ınetSocketAddress) {
        this.ınetSocketAddress = ınetSocketAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getHaveToGetTotallyByteLenght() {
        return haveToGetTotallyByteLenght;
    }

    public void setHaveToGetTotallyByteLenght(int haveToGetTotallyByteLenght) {
        this.haveToGetTotallyByteLenght = haveToGetTotallyByteLenght;
    }
}
