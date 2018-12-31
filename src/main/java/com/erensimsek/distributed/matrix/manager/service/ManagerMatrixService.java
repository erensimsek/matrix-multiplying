package com.erensimsek.distributed.matrix.manager.service;

import com.erensimsek.distributed.matrix.Configuration;
import com.erensimsek.distributed.matrix.io.MatrixReaderWriter;
import com.erensimsek.distributed.matrix.model.Matrix;
import com.erensimsek.distributed.matrix.model.Worker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by esimsek on 2/24/2017.
 */
public class ManagerMatrixService {

    private final static int[] serverPortList = {4540, 4541, 4542, 4543, 4544, 4545, 4546, 4547, 4548, 4549, 4550,
            4551, 4552, 4553, 4554, 4555, 4556, 4557, 4558, 4559, 4560};

    private final static Logger LOGGER = Logger.getLogger(ManagerMatrixService.class.getName());


    public int[][] distrubitedMatrixMultiply(ArrayList<ArrayList<Integer>> a,
                                             ArrayList<ArrayList<Integer>> b,
                                             String inputFileName) throws IOException {

        List workers = readWorkersFromFile(Configuration.hostsFileName);

        Matrix matrix = new Matrix(a, b);

        int[][] result = divideAndRetrieveMatrixToSubMatrix(matrix, workers);

        MatrixReaderWriter.printMatrixToFile(result, inputFileName.replace(".in", ".out"));

        return result;
    }

    private int[][] divideAndRetrieveMatrixToSubMatrix(Matrix matrix, List<Worker> workers) {

        ArrayList<Future<int[][]>> sb = new ArrayList<Future<int[][]>>();
        ExecutorService executor = Executors.newFixedThreadPool(workers.size());
        int part = matrix.getMatrixA().size() / workers.size();
        if (part < 1) {
            part = 1;
        }
        int workerIndex = 0;
        for (int i = 0; i < matrix.getMatrixA().size(); i += part) {
            Callable<int[][]> worker = new MultiplierManagerThread(matrix.getMatrixA(), matrix.getMatrixB(), i,
                    i + part, workers.get(workerIndex), serverPortList[workerIndex]);
            Future<int[][]> submit = executor.submit(worker);
            sb.add(submit);
            workerIndex++;
        }

        int[][] C = new int[matrix.getMatrixA().size()][matrix.getMatrixA().size()];
        int start = 0;
        int CF[][];
        for (Future<int[][]> future : sb) {
            try {
                CF = future.get();
                for (int i = start; i < start + part; i += 1) {
                    C[i] = CF[i];
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            } catch (ExecutionException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
            start += part;
        }
        executor.shutdown();

        return C;
    }

    private List<Worker> readWorkersFromFile(String hostsFileName) {

        List<Worker> workerList = new ArrayList<Worker>();

        String thisLine;

        try {
            BufferedReader br = new BufferedReader(new FileReader(hostsFileName));

            while ((thisLine = br.readLine()) != null) {
                if (thisLine.trim().equals("")) {
                    break;
                } else {
                    String[] lineArray = thisLine.split(":");
                    Worker worker = new Worker(InetAddress.getByName(lineArray[0]), Integer.parseInt(lineArray[1]), true);
                    workerList.add(worker);
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "host.txt File not found " + hostsFileName + " Error: " + e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "File format is not correct! " + hostsFileName + " Error: " + e.getMessage(), e);
        }
        return workerList;
    }
}
