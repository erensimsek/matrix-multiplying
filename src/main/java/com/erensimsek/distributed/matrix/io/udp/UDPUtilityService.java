package com.erensimsek.distributed.matrix.io.udp;

import com.erensimsek.distributed.matrix.Configuration;
import com.erensimsek.distributed.matrix.model.ByteRequest;
import com.erensimsek.distributed.matrix.model.Manager;
import com.erensimsek.distributed.matrix.model.SubMatrixRequest;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by esimsek on 3/10/2017.
 */
public class UDPUtilityService {

    private final static Logger LOGGER = Logger.getLogger(UDPUtilityService.class.getName());

    public SubMatrixRequest getSubMatrixRequestFromBytes(byte[] allMatrix) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inByteInputStream = new ByteArrayInputStream(allMatrix);
        ObjectInputStream objectInputStream = new ObjectInputStream(inByteInputStream);
        inByteInputStream.close();
        objectInputStream.close();
        SubMatrixRequest receivedMatrix = (SubMatrixRequest) objectInputStream.readObject();
        return receivedMatrix;
    }

    public byte[] getBytesFromObject(SubMatrixRequest subMatrixRequest) throws IOException {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOutputStream);
        out.writeObject(subMatrixRequest);
        out.flush();
        out.close();
        byteOutputStream.close();
        return byteOutputStream.toByteArray();
    }


    public ByteRequest waitAndGetByteRequest(DatagramSocket datagramSocket) throws IOException {
        String ilkMessage = new String(receiveBytes(datagramSocket)).trim();
        int from = Integer.parseInt(ilkMessage.substring(ilkMessage.indexOf("@") + 1, ilkMessage.indexOf(":")));
        int to = Integer.parseInt(ilkMessage.substring(ilkMessage.indexOf(":") + 1, ilkMessage.length()));
        boolean isLastRequest;
        if (ilkMessage.substring(0, ilkMessage.indexOf("@")).equals("GET")) {
            isLastRequest = false;
        } else if (ilkMessage.substring(0, ilkMessage.indexOf("@")).equals("END")) {
            isLastRequest = true;
        } else {
            throw new IOException("Wrong answer!");
        }
        return new ByteRequest(from, to, isLastRequest);
    }

    private byte[] receiveBytes(DatagramSocket datagramSocket) throws IOException {
        DatagramPacket datagramPacket;
        datagramPacket = new DatagramPacket(new byte[Configuration.PAKET_BYTE_SIZE], Configuration.PAKET_BYTE_SIZE);
        //datagramSocket.setSoTimeout(Configuration.TIMEOUT);
        datagramSocket.receive(datagramPacket);
        byte[] bytes = datagramPacket.getData();
        return bytes;
    }


    public void sendBytesLenght(InetSocketAddress recieverSocketAddress, DatagramSocket datagramSocket, int length, String managerIp, int managerPort) throws IOException {
        String messageFromManager1 = "" + length + "@" + managerIp + ":" + managerPort;
        sendBytes(datagramSocket, recieverSocketAddress, messageFromManager1.getBytes());
    }

    private void sendBytes(DatagramSocket datagramSocket, InetSocketAddress serverHost, byte[] bytes) throws IOException {
        datagramSocket.send(new DatagramPacket(bytes, bytes.length, serverHost));
    }

    public void sendBulkBytes(InetSocketAddress serverHost, DatagramSocket datagramSocket, byte[] tumByte) throws IOException {
        ByteRequest br;
        LOGGER.log(Level.INFO, "Start to sending matrix");
        do {
            br = waitAndGetByteRequest(datagramSocket);
            sendBytes(datagramSocket, serverHost, Arrays.copyOfRange(tumByte, br.getFrom(), br.getTo()));
        } while (!br.isLastRequest());

    }

    public Manager getManagerInfo(DatagramSocket datagramSocket) throws IOException {
        String ilkMessage = new String(receiveBytes(datagramSocket)).trim();
        int lenght = Integer.parseInt(ilkMessage.substring(0, ilkMessage.indexOf("@")));
        int port = Integer.parseInt(ilkMessage.substring(ilkMessage.indexOf(":") + 1, ilkMessage.length()));
        String ip = ilkMessage.substring(ilkMessage.indexOf("@") ^ +1, ilkMessage.indexOf(":"));
        Manager manager = new Manager(new InetSocketAddress(ip, port), port, lenght);
        return manager;
    }

    public byte[] receiveBulkBytes(InetSocketAddress myAddress, DatagramSocket datagramSocket, int allBytesLenght, byte[] desBytes) throws IOException {

        LOGGER.log(Level.INFO, "Getting bulk bytes");
        DatagramPacket datagramPacket = new DatagramPacket(new byte[Configuration.PAKET_BYTE_SIZE], Configuration.PAKET_BYTE_SIZE);
        int destPos = 0;
        int alinacakByteSayisi;
        while (allBytesLenght > 0) {

            alinacakByteSayisi = allBytesLenght >= Configuration.PAKET_BYTE_SIZE ? Configuration.PAKET_BYTE_SIZE : allBytesLenght;

            sendGetOrEndMessageforGetBytesRequest(myAddress, datagramSocket, destPos, alinacakByteSayisi, alinacakByteSayisi>=allBytesLenght);

            datagramSocket.receive(datagramPacket);

            System.arraycopy(datagramPacket.getData(), 0, desBytes, destPos, alinacakByteSayisi);

            destPos += alinacakByteSayisi;

            allBytesLenght = allBytesLenght - alinacakByteSayisi;
        }
        return desBytes;
    }

    public void sendGetOrEndMessageforGetBytesRequest(InetSocketAddress myAddress, DatagramSocket datagramSocket, int from, int to, boolean endMassage) throws IOException {
        String messageFromManager1 = endMassage?"END@" + from + ":" + to:"GET@" + from + ":" + to;
        sendBytes(datagramSocket, myAddress, messageFromManager1.getBytes());
    }
}
