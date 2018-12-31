package com.erensimsek.distributed.matrix.manager.service;

import com.erensimsek.distributed.matrix.io.udp.*;
import com.erensimsek.distributed.matrix.model.SubMatrixRequest;
import com.erensimsek.distributed.matrix.model.Worker;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by esimsek on 2/15/2017.
 */
class MultiplierManagerThread implements Callable<int[][]> {

    private final static Logger LOGGER = Logger.getLogger(MultiplierManagerThread.class.getName());

    public int[][] C;
    ArrayList<ArrayList<Integer>> A;
    ArrayList<ArrayList<Integer>> B;
    int start;
    int end;
    int serverReservedPortForWorker;
    Worker worker;


    public MultiplierManagerThread(ArrayList<ArrayList<Integer>> a,
                                   ArrayList<ArrayList<Integer>> b, int s, int e, Worker worker, int serverReservedPortForWorker) {
        this.A = a;
        this.B = b;
        this.C = new int[a.size()][b.get(0).size()];
        this.start = s;
        this.end = e;
        this.serverReservedPortForWorker = serverReservedPortForWorker;
        this.worker = worker;
    }

    public int[][] call() {

        //Matrixi gonder ve sonucu alip don
        ArrayList<ArrayList<Integer>> a = new ArrayList<ArrayList<Integer>>();

        for (int i=start; i<end; i++){
            a.add(new ArrayList<Integer>(A.get(i)));
        }

        SubMatrixRequest subMatrixRequest = new SubMatrixRequest(a, B, start, end);
        InetAddress hostIP = null;
        try{
            hostIP = InetAddress.getLocalHost();
        }catch (UnknownHostException e){
            LOGGER.log(Level.INFO, "UnknownHostException:"+start +" end:"+end);
        }
        subMatrixRequest.setServerHostname(hostIP);
        subMatrixRequest.setServerPort(serverReservedPortForWorker);

        UDPMatrixSender sender = new UDPMatrixSender(serverReservedPortForWorker);
        sender.sendMatrix(subMatrixRequest,new InetSocketAddress(worker.getIp(),worker.getPort()),hostIP.getHostAddress(),false);

        UDPMatrixReceiver receiver = new UDPMatrixReceiver();
        subMatrixRequest = receiver.waitAndGetSubmatrixRequest(serverReservedPortForWorker);


        int[][] result = subMatrixRequest.getResult();
        int sonucIndex=0;
        for (int i=start; i < end; i++){
            C[i] = result[sonucIndex];
            sonucIndex++;
        }

        return C;
    }
}
