package com.erensimsek.distributed.matrix.worker.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by esimsek on 2/15/2017.
 */
class MultiplierWorkerThread implements Callable<int[][]> {

    public int[][] C;
    ArrayList<ArrayList<Integer>> A;
    ArrayList<ArrayList<Integer>> B;
    int start;
    int end;

    public MultiplierWorkerThread(ArrayList<ArrayList<Integer>> a,
                                  ArrayList<ArrayList<Integer>> b, int s, int e) {
        A = a;
        B = b;
        C = new int[a.size()][b.get(0).size()];
        start = s;
        end = e <= a.size() ? e : a.size();
    }

    public int[][] call() {
        for (int i = start; i < end; i++) {
            for (int k = 0; k < B.size(); k++) {
                for (int j = 0; j < B.get(0).size(); j++) {
                    C[i][j] += A.get(i).get(k) * B.get(k).get(j);
                }
            }
        }
        return C;
    }
}
