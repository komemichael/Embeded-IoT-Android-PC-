package com.example.kome.client1;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements Runnable {

    com.example.kome.servermessagehandler.ServerMessageHandler myServerCommandHandler;
    com.example.kome.userinterface.UserInterface myUI;
    boolean stopThisThread = false, isConnected = false;
    byte messageCount = 0;
    InputStream input;
    OutputStream output;
    Socket clientSocket;
    int serverPort = 5555;
    String serverHostName = "localhost", serverMessage = "";

    public Client(int serverPort, String serverHost, com.example.kome.userinterface.UserInterface myUI) {
        this.serverPort = serverPort;
        this.serverHostName = serverHost;
        this.myUI = myUI;
        this.myServerCommandHandler = new com.example.kome.servermessagehandler.ServerMessageHandler(this);
    }

    public void sendMessageToUI(String theString) {
        myUI.update(theString);
    }

    public void stopThread() {
        stopThisThread = true;
    }

    @Override
    public void run() {
        byte byteMessageFromServer = 0x00;
        while (stopThisThread == false) {
            try {
                byteMessageFromServer = (byte) input.read();
                myServerCommandHandler.handleServerMessage(byteToString(byteMessageFromServer));
            } catch (IOException ex) {
                if (stopThisThread == false) {
                    myServerCommandHandler.handleServerMessage(ex);
                    disconnectFromServer();
                    stopThisThread = true;
                }
            }
        }
    }

    private String byteToString(byte theByte) {
        byte[] theByteArray = new byte[1];
        String theString = null;
        theByteArray[0] = theByte;
        try {
            theString = new String(theByteArray, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            sendMessageToUI("Cannot convert byte to UTF-8 String; exiting program.");
            System.exit(1);
        } finally {
            return theString;
        }
    }

    public boolean connectToServer() {
        if (isConnected == false) {
            try {
                clientSocket = new Socket("140.193.235.171" , 7777);//140.193.235.165 192.168.0.18
                input = clientSocket.getInputStream();
                output = clientSocket.getOutputStream();
                isConnected = true;
                stopThisThread = false;
                Thread myClientThread = new Thread(this);
                myClientThread.start();
            } catch (IOException e) {
                sendMessageToUI("Cannot connect to Server, because " + e + ". Exiting program.");
                //System.exit(1);
            }
        } else {
            return false;
        }
        return true;
    }

    public void disconnectFromServer() {
        try {
            isConnected = false;
            clientSocket.close();
            clientSocket = null;
            input = null;
            output = null;
        } catch (IOException e) {
            sendMessageToUI("Cannot disconnect from Server, because " + e + ". Exiting program.");
            System.exit(1);
        } finally {
        }
    }

    public void sendMessageToServer(byte msg) {
        try {
            output.write(msg);
            output.flush();
        } catch (IOException e) {
        } finally {
        }
    }

    public void sendStringMessageToServer(String theMessage) {
        for (int i = 0; i < theMessage.length(); i++) {
            byte msg = (byte) theMessage.charAt(i);
            sendMessageToServer(msg);
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setPort(int port) {
        this.serverPort = port;
    }

    public void setHost(String host) {
        this.serverHostName = host;
    }

}
