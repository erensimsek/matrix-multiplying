package com.erensimsek.distributed.matrix.model;

import java.util.ArrayList;

/**
 * Created by esimsek on 2/24/2017.
 */
public class Matrix {

    private ArrayList<ArrayList<Integer>> matrixA;
    private ArrayList<ArrayList<Integer>> matrixB;
    private ArrayList<ArrayList<Integer>> matrixResult;

    public Matrix(ArrayList<ArrayList<Integer>> matrixA, ArrayList<ArrayList<Integer>> matrixB) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
    }
    public Matrix(ArrayList<ArrayList<Integer>> matrixResult) {
        this.matrixResult = matrixResult;
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

    public ArrayList<ArrayList<Integer>> getMatrixResult() {
        return matrixResult;
    }

    public void setMatrixResult(ArrayList<ArrayList<Integer>> matrixResult) {
        this.matrixResult = matrixResult;
    }
}
