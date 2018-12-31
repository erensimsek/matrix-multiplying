package com.erensimsek.distributed.matrix.io.udp;

import com.erensimsek.distributed.matrix.model.SubMatrixRequest;

import java.io.*;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by esimsek on 2/24/2017.
 */
public class UDPMatrixSender {

    private int senderPort;
    private UDPUtilityService uDPUtilityService = new UDPUtilityService();

    private final static Logger LOGGER = Logger.getLogger(UDPMatrixSender.class.getName());

    public UDPMatrixSender(int serverSenderPort) {
        this.senderPort = serverSenderPort;
    }

    public void sendMatrix(SubMatrixRequest subMatrixRequest, InetSocketAddress receiverSocketAddress, String managerIp, boolean messageFromWorker) {
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = messageFromWorker ? new DatagramSocket() : new DatagramSocket(senderPort);

            byte[] tumByte = uDPUtilityService.getBytesFromObject(subMatrixRequest);

            uDPUtilityService.sendBytesLenght(receiverSocketAddress, datagramSocket, tumByte.length, managerIp, senderPort);

            uDPUtilityService.sendBulkBytes(receiverSocketAddress, datagramSocket, tumByte);

            LOGGER.log(Level.INFO, "Matrix sent");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception: " + e.getMessage(), e);
        } finally {
            if (datagramSocket != null)
                datagramSocket.close();
        }
    }
}
