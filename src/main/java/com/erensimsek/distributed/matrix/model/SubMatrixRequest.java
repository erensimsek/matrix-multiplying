package com.erensimsek.distributed.matrix.model;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by esimsek on 2/24/2017.
 */
public class SubMatrixRequest implements Serializable {

    private ArrayList<ArrayList<Integer>> matrixA;
    private ArrayList<ArrayList<Integer>> matrixB;
    private int start;
    private int end;
    private int[][] result;
    private int serverPort;
    private InetAddress serverHostname;

    public SubMatrixRequest(ArrayList<ArrayList<Integer>> matrixA,ArrayList<ArrayList<Integer>> matrixB, int start, int end) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.start = start;
        this.end = end;
        this.result = new int [matrixA.size()][matrixA.size()];
    }

    public ArrayList<ArrayList<Integer>> getMatrixA() {
        return matrixA;
    }

    public void setMatrixA(ArrayList<ArrayList<Integer>> matrixA) {
        this.matrixA = matrixA;
    }

    public ArrayList<ArrayList<Integer>> getMatrixB() {
        return matrixB;
    }

    public void setMatrixB(ArrayList<ArrayList<Integer>> matrixB) {
        this.matrixB = matrixB;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int[][] getResult() {
        return result;
    }

    public void setResult(int[][] result) {
        this.result = result;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public InetAddress getServerHostname() {
        return serverHostname;
    }

    public void setServerHostname(InetAddress serverHostname) {
        this.serverHostname = serverHostname;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubMatrixRequest)) return false;

        SubMatrixRequest that = (SubMatrixRequest) o;

        if (getMatrixA() != null ? !getMatrixA().equals(that.getMatrixA()) : that.getMatrixA() != null) return false;
        return !(getMatrixB() != null ? !getMatrixB().equals(that.getMatrixB()) : that.getMatrixB() != null);

    }

    @Override
    public int hashCode() {
        int result = getMatrixA() != null ? getMatrixA().hashCode() : 0;
        result = 31 * result + (getMatrixB() != null ? getMatrixB().hashCode() : 0);
        return result;
    }
}
