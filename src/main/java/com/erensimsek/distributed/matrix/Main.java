package com.erensimsek.distributed.matrix;

import com.erensimsek.distributed.matrix.io.MatrixReaderWriter;
import com.erensimsek.distributed.matrix.io.udp.UDPMatrixReceiver;
import com.erensimsek.distributed.matrix.manager.service.ManagerMatrixService;
import com.erensimsek.distributed.matrix.worker.service.WorkerMultiplierService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        if (args != null && args.length >= 1 && args[0].equals("Worker")) {
            String port = null, timeout=null;
            while (true) {
                try {
                    if (args.length < 3) {
                        System.out.println("Worker is runnig. Please type port :");
                        Scanner input = new Scanner(System.in);
                        port = input.nextLine();
                        timeout = input.nextLine();
                    } else {
                        port = args[1];
                        timeout = args[2];
                    }
                    Configuration.TIMEOUT = Integer.parseInt(timeout);

                    WorkerMultiplierService workerMultiplierService = new WorkerMultiplierService();
                    workerMultiplierService.waitGetdMultiplySendMatrix(Integer.parseInt(port));


                } catch (Exception e) {
                    System.out.println("An error occured. Error: " + e.getMessage() + " " + e);
                }
            }
        } else {
            boolean cikis = false;
            while (cikis == false) {
                try {
                    System.out.print("Manager is runnig. Please type filename contains 2D Matrix (Type 1 for exit):");
                    Scanner input = new Scanner(System.in);
                    String inputFileName = input.nextLine();
                    String timeout = input.nextLine();
                    if (input != null && input.equals("1")) {
                        cikis = true;
                    } else {

                        Configuration.TIMEOUT = Integer.parseInt(timeout);

                        List<ArrayList<ArrayList<Integer>>> matrices = MatrixReaderWriter.readMatrixFromFile(inputFileName);
                        ArrayList<ArrayList<Integer>> AList = matrices.get(0);
                        ArrayList<ArrayList<Integer>> BList = matrices.get(1);

                        ManagerMatrixService managerMatrixService = new ManagerMatrixService();
                        managerMatrixService.distrubitedMatrixMultiply(AList, BList, inputFileName);

                    }

                } catch (Exception e) {
                    System.out.println("An error occured. Error: " + e.getMessage() + " " + e);
                }
            }
        }

    }
}
