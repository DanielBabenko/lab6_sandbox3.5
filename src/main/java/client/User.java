package client;

import client.object.LabWork;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.PortUnreachableException;
import java.text.ParseException;
import java.util.*;

public class User {
    private String host;
    private int port;

    private static DatagramSocket socket;

    private DatagramPacket sendingPacket;

    private DatagramPacket receivingPacket;

    private byte[] receivingDataBuffer;

    private SendObject sendObject;

    public User(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void sendMessage(String message) throws ClassNotFoundException {
        try {
            receivingDataBuffer = new byte[5000];
            byte[] data = message.getBytes();
            InetAddress address = InetAddress.getByName(host);
            // Создайте UDP-пакет
            sendingPacket = new DatagramPacket(data, data.length, address, port);
            // Отправьте UDP-пакет серверу
            socket.send(sendingPacket);
            receiveMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void receiveMessage() throws IOException {
        receivingPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
        socket.receive(receivingPacket);
        // Выведите на экране полученные данные
        String receivedData = new String(receivingPacket.getData()).trim();
        System.out.println("Sent from the server: " + receivedData);
    }

    private void sendExitMessage(String message) {
        try {
            receivingDataBuffer = new byte[2048];
            byte[] data = message.getBytes();
            InetAddress address = InetAddress.getByName(host);
            // Создайте UDP-пакет
            sendingPacket = new DatagramPacket(data, data.length, address, port);
            // Отправьте UDP-пакет серверу
            socket.send(sendingPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        User sender = new User("localhost", 59056);
        sender.setSocket(new DatagramSocket());
        BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
        boolean flag = false;
        //sender.sendObject = new SendObject(sender.getLabs());

        try {
            while (!flag) {
                System.out.println("Enter: ");
                String message = b.readLine().trim();
                if (!message.isEmpty()) {
                    if (message.equals("exit")) {
                        sender.sendExitMessage(message);
                        System.exit(0);
                    }
                    String[] arr = message.split(" ",2);
                    if (arr[0].equals("execute_script")) {
                        sender.executeScript(arr[1]);
                    } else {
                        sender.sendMessage(message);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        b.close();
        // Закрыть соединение
        sender.getSocket().close();
    }

    public void executeScript(String pathToFile) throws IOException, ClassNotFoundException {
        pathToFile = System.getProperty("user.dir") + "/" + pathToFile;
        System.out.println(pathToFile);
        if (new File(pathToFile).exists()) {
            // настраиваем поток ввода
            BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(pathToFile)));
            List<String> scriptCommands = new ArrayList<>();
            String s = buff.readLine();
            while(s != null){
                scriptCommands.add(s);
                s = buff.readLine();
            }

            for(String command:scriptCommands){
                sendMessage(command);
            }

        } else {
            sendMessage(" ");
        }
    }

    private void sendLabWorkObject() throws IOException {
        sendObject.start();

        InetAddress address = InetAddress.getByName(this.host);
        socket.connect(address, port);
        receivingDataBuffer = new byte[2048];
        byte[] data = SerializationManager.serialize(sendObject.getLabWork());
        // Создайте UDP-пакет
        sendingPacket = new DatagramPacket(data, data.length, address, port);
        // Отправьте UDP-пакет серверу
        socket.send(sendingPacket);
        // Создайте UDP-пакет
        receivingPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
        //Получите ответ от сервера
        try {
            socket.receive(receivingPacket);
        } catch (PortUnreachableException e) {
            System.out.println("Server do not respond(");
            socket.close();
        }

        String receivedData = new String(receivingPacket.getData());
        System.out.println(receivedData.trim());

    }

    private HashSet<LabWork> getLabs() throws IOException {

        InetAddress address = InetAddress.getByName(this.host);
        socket.connect(address, port);
        receivingDataBuffer = new byte[2048];
        byte[] data = "show".getBytes();
        // Создайте UDP-пакет
        sendingPacket = new DatagramPacket(data, data.length, address, port);
        // Отправьте UDP-пакет серверу
        socket.send(sendingPacket);
        // Создайте UDP-пакет
        receivingPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
        socket.receive(receivingPacket);
        try {
            HashSet<LabWork> labs = SerializationManager.deserialize(receivingPacket.getData());
            return labs;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    public DatagramSocket getSocket() {
        return socket;
    }
}


