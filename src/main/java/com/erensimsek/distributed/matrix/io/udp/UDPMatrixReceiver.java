package com.erensimsek.distributed.matrix.io.udp;

import com.erensimsek.distributed.matrix.model.Manager;
import com.erensimsek.distributed.matrix.model.SubMatrixRequest;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Created by esimsek on 2/24/2017.
 */
public class UDPMatrixReceiver {
    private UDPUtilityService udpUtilityService = new UDPUtilityService();

    private final static Logger LOGGER = Logger.getLogger(UDPMatrixReceiver.class.getName());

    public SubMatrixRequest waitAndGetSubmatrixRequest(int port) {
        SubMatrixRequest receivedMatrix = null;
        try {

            DatagramSocket datagramSocket = new DatagramSocket(port);

            LOGGER.log(Level.INFO, "Listening : " + port);

            Manager manager = udpUtilityService.getManagerInfo(datagramSocket);

            int allMatrixByteLenght = manager.getHaveToGetTotallyByteLenght();
            byte[] matrixBytes = new byte[allMatrixByteLenght];

            matrixBytes = udpUtilityService.receiveBulkBytes(manager.getInetSocketAddress() , datagramSocket, allMatrixByteLenght, matrixBytes);

            receivedMatrix = udpUtilityService.getSubMatrixRequestFromBytes(matrixBytes);

            LOGGER.log(Level.INFO, "Matrix is received successfully.");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IOException: " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "ClassNotFoundException: " + e.getMessage(), e);
        }
        return receivedMatrix;

    }
}
