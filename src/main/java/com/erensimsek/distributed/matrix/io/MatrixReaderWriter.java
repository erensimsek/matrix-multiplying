package com.erensimsek.distributed.matrix.io;

import com.erensimsek.distributed.matrix.manager.service.ManagerMatrixService;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by esimsek on 2/24/2017.
 */
public class MatrixReaderWriter {

    private static final double MEG = (Math.pow(1024, 2));
    private final static Logger LOGGER = Logger.getLogger(ManagerMatrixService.class.getName());

    public static List<ArrayList<ArrayList<Integer>>> readMatrixFromFile(String filename) {
        ArrayList<ArrayList<Integer>> A = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> B = new ArrayList<ArrayList<Integer>>();

        String thisLine;

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            // Begin reading A
            while ((thisLine = br.readLine()) != null) {
                if (thisLine.trim().equals("")) {
                    break;
                } else {
                    ArrayList<Integer> line = new ArrayList<Integer>();
                    String[] lineArray = thisLine.split("\t");
                    for (String number : lineArray) {
                        line.add(Integer.parseInt(number));
                    }
                    A.add(line);
                }
            }

            // Begin reading B
            while ((thisLine = br.readLine()) != null) {
                ArrayList<Integer> line = new ArrayList<Integer>();
                String[] lineArray = thisLine.split("\t");
                for (String number : lineArray) {
                    line.add(Integer.parseInt(number));
                }
                B.add(line);
            }
            br.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        List<ArrayList<ArrayList<Integer>>> res = new LinkedList<ArrayList<ArrayList<Integer>>>();
        res.add(A);
        res.add(B);
        return res;
    }

    public static void printMatrixToFile(int[][] matrix,String filename) throws IOException {
        writeBuffered(matrix, 4 * (int) MEG, filename);
    }

    private static void writeBuffered(int[][] matrix, int bufSize, String filename) throws IOException {
        LOGGER.log(Level.INFO, "Result is writing to file :" + filename);

        FileWriter writer = new FileWriter(new File(filename));
        BufferedWriter bufferedWriter = new BufferedWriter(writer, bufSize);

        write(matrix, bufferedWriter);

    }

    private static void write(int[][] matrix, Writer writer) throws IOException {
        long start = System.currentTimeMillis();
        for (int[] line : matrix) {
            int i = 0;
            StringBuilder sb = new StringBuilder(matrix.length);
            for (int number : line) {
                if (i != 0) {
                    sb.append("\t");
                } else {
                    i++;
                }
                sb.append(number);
            }
            sb.append("\n");
            writer.write(String.valueOf(sb));
        }
        writer.flush();
        writer.close();
        long end = System.currentTimeMillis();
        System.out.println("Writing is succes to file in "+(end - start) / 1000f + " seconds");
    }

    public static void printMatrixToConsole(int[][] matrix) {
        for (int[] line : matrix) {
            int i = 0;
            StringBuilder sb = new StringBuilder(matrix.length);
            for (int number : line) {
                if (i != 0) {
                    sb.append("\t");
                } else {
                    i++;
                }
                sb.append(number);
            }
            System.out.println(sb.toString());
        }
    }
}
