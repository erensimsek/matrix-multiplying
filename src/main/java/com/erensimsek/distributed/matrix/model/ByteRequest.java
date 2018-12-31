package com.erensimsek.distributed.matrix.model;

/**
 * Created by esimsek on 3/10/2017.
 */
public class ByteRequest {
    private int from;
    private int to;
    private boolean isLastRequest;

    public ByteRequest(int from, int to, boolean isLastRequest) {
        this.from = from;
        this.to = to;
        this.isLastRequest = isLastRequest;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public boolean isLastRequest() {
        return isLastRequest;
    }

    public void setLastRequest(boolean lastRequest) {
        isLastRequest = lastRequest;
    }
}
