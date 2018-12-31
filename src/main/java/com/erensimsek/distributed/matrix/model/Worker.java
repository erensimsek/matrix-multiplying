package com.erensimsek.distributed.matrix.model;

import java.net.InetAddress;

/**
 * Created by esimsek on 3/3/2017.
 */
public class Worker {

    private InetAddress ip;
    private int port;
    private volatile boolean isLive;

    public Worker(InetAddress ip, int port, boolean isLive) {
        this.ip = ip;
        this.port = port;
        this.isLive = isLive;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Worker worker = (Worker) o;

        if (port != worker.port) return false;
        return ip != null ? ip.equals(worker.ip) : worker.ip == null;
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + port;
        return result;
    }
}
