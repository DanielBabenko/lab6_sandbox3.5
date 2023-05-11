package manager;

import command.commands.Invoker;
import object.LabWork;
import parser.Root;
import parser.parserToJson.ParserToJson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.List;

public class Server {

    private final static int PORT = 59056;
    DatagramSocket serverSocket;
    private Root root;
    private  String fileName;

    private InetAddress senderAddress;
    private int senderPort;

    private BufferedReader reader;

    public Server() throws IOException {
        this.serverSocket = new DatagramSocket(PORT);
    }

    public void sentToClient(Object data) throws IOException {
        byte[] sendingDataBuffer;

        //  sent client result
        sendingDataBuffer = data.toString().getBytes();


        // create a new udp packet
        DatagramPacket outputPacket = new DatagramPacket(
                sendingDataBuffer, sendingDataBuffer.length,
                getSenderAddress(), getSenderPort());

        // send packet to client
        serverSocket.send(outputPacket);

    }

    public Root getRoot() {
        return root;
    }


    public String dataFromClient() throws IOException {
        byte[] receivingDataBuffer = new byte[1024];
        DatagramPacket inputPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
        boolean flag = false;
        System.out.println("waiting for a client to connect: ");
        while (!flag) {
            // give information from client
            serverSocket.receive(inputPacket);

            String receivedData = new String(inputPacket.getData()).trim();

            if (!receivedData.isEmpty()) {

                if (receivedData.equals("exit")){System.exit(0);}

                setSenderAddress(inputPacket.getAddress());
                setSenderPort(inputPacket.getPort());


                System.out.println("Sent from client: " + receivedData);
                return receivedData;
            }
        }

        return "";
    }

    public LabWork getObjectFromClient() throws IOException, ClassNotFoundException {
        System.out.println("waiting for a client to connect: ");
        byte[] receivingDataBuffer = new byte[1024];
        DatagramPacket inputPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
        // give information from client
        serverSocket.receive(inputPacket);


        setSenderAddress(inputPacket.getAddress());
        setSenderPort(inputPacket.getPort());


        return  SerializationManager.deserializeObject(inputPacket.getData());

    }

    public DatagramSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(DatagramSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public InetAddress getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(InetAddress senderAddress) {
        this.senderAddress = senderAddress;
    }

    public static int getPORT() {
        return PORT;
    }

    public void setSenderPort(int senderPort) {
        this.senderPort = senderPort;
    }

    public int getSenderPort() {
        return senderPort;
    }
}