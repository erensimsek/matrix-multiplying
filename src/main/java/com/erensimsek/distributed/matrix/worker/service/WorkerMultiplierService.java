package com.erensimsek.distributed.matrix.worker.service;

import com.erensimsek.distributed.matrix.io.udp.*;
import com.erensimsek.distributed.matrix.model.SubMatrixRequest;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by esimsek on 2/24/2017.
 */
public class WorkerMultiplierService {

    private final static Logger LOGGER = Logger.getLogger(WorkerMultiplierService.class.getName());

    public void waitGetdMultiplySendMatrix(int port) throws Exception {


        UDPMatrixReceiver receiver = new UDPMatrixReceiver();
        SubMatrixRequest alinanMatrix = receiver.waitAndGetSubmatrixRequest(port);
        LOGGER.log(Level.INFO, "Matris alindi carpma yapiliyor: ");

        int[][] carpimSonucu = WorkerMultiplierService.parallelMult(alinanMatrix.getMatrixA(), alinanMatrix.getMatrixB());
        alinanMatrix.setResult(carpimSonucu);

        LOGGER.log(Level.INFO, "Matris alindi carpma tamamlandi: ");

        InetAddress hostIP = null;
        try{
            hostIP = InetAddress.getLocalHost();
        }catch (UnknownHostException e){
            LOGGER.log(Level.INFO, e.getMessage(),e);
        }
        UDPMatrixSender sender = new UDPMatrixSender(port);
        sender.sendMatrix(alinanMatrix,
                new InetSocketAddress(alinanMatrix.getServerHostname(), alinanMatrix.getServerPort()), hostIP.getHostAddress(),true);
    }

    private static int[][] parallelMult(ArrayList<ArrayList<Integer>> A,
                                        ArrayList<ArrayList<Integer>> B) throws Exception {

        int threadNumber = Runtime.getRuntime().availableProcessors();
        return parallelMult(A, B, threadNumber);
    }

    private static int[][] parallelMult(ArrayList<ArrayList<Integer>> A,
                                        ArrayList<ArrayList<Integer>> B, int threadNumber) throws Exception {
        int[][] C = new int[A.size()][B.get(0).size()];
        ExecutorService executor = Executors.newFixedThreadPool(threadNumber);
        List<Future<int[][]>> list = new ArrayList<Future<int[][]>>();

        int part = A.size() / threadNumber;
        if (part < 1) {
            part = 1;
        }
        for (int i = 0; i < A.size(); i += part) {
            System.err.println(i);
            Callable<int[][]> worker = new MultiplierWorkerThread(A, B, i, i + part);
            Future<int[][]> submit = executor.submit(worker);
            list.add(submit);
        }

        // now retrieve the result
        int start = 0;
        int CF[][];
        for (Future<int[][]> future : list) {
            CF = future.get();
            int startPlusPart = start + part;
            startPlusPart = startPlusPart <= CF.length ? startPlusPart : CF.length;
            for (int i = start; i < startPlusPart; i += 1) {
                C[i] = CF[i];
            }
            start += part;
        }
        executor.shutdown();

        return C;
    }

}
