/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.kome.servermessagehandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ferens
 */
public class ServerMessageHandler {

    com.example.kome.client1.Client myClient;
    String serverMessage = "";
    
    public ServerMessageHandler(com.example.kome.client1.Client myClient) {
        this.myClient = myClient;
    }

    public void handleCompleteServerMessage(String theCommand) {
        
        if (theCommand.contains("Hello: ")) {
            myClient.sendMessageToUI("Server has acknowledged the connection request");
            myClient.sendMessageToUI("\t " + theCommand);
        } 
        else if (theCommand.contains("Disconnect Ack: ")) {
            myClient.sendMessageToUI("Server has acknowledged the disconnect request");
            myClient.sendMessageToUI("\t " + theCommand);
            myClient.stopThread();
            myClient.disconnectFromServer();
        } 
        else if (theCommand.contains("Quit Ack: ")) {
            myClient.sendMessageToUI("Server has acknowledged the quit request");
            myClient.sendMessageToUI("\t " + theCommand);
            myClient.stopThread();
            myClient.disconnectFromServer();
            System.exit(-1);
        } 
        else if (theCommand.contains("The time is: ")) {
            //myClient.sendMessageToUI("Server has acknowledged the get time request");
            myClient.sendMessageToUI("\t " + theCommand);
        }    
        else if (theCommand.contains("LED 1 On")) {
            //myClient.sendMessageToUI("Server has acknowledged the switch L1on request");
            myClient.sendMessageToUI("\t " + theCommand);
        } 
        else if (theCommand.contains("LED 2 On")) {
            //myClient.sendMessageToUI("Server has acknowledged the switch L2on request");
            myClient.sendMessageToUI("\t " + theCommand);
        } 
        else if (theCommand.contains("LED 3 On")) {
            //myClient.sendMessageToUI("Server has acknowledged the switch L3on request");
            myClient.sendMessageToUI("\t " + theCommand);
        } 
        else if (theCommand.contains("LED 4 On")) {
            //myClient.sendMessageToUI("Server has acknowledged the switch L4on request");
            myClient.sendMessageToUI("\t " + theCommand);
        } 
        else if (theCommand.contains("LED 1 Off")) {
            //myClient.sendMessageToUI("Server has acknowledged the switch L1off request");
            myClient.sendMessageToUI("\t " + theCommand);
        } 
        else if (theCommand.contains("LED 2 Off")) {
            //myClient.sendMessageToUI("Server has acknowledged the switch L2off request");
            myClient.sendMessageToUI("\t " + theCommand);
        } 
        else if (theCommand.contains("LED 3 Off")) {
            //myClient.sendMessageToUI("Server has acknowledged the switch L3off request");
            myClient.sendMessageToUI("\t " + theCommand);
        } 
        else if (theCommand.contains("LED 4 Off")) {
            //myClient.sendMessageToUI("Server has acknowledged the switch L4off request");
            myClient.sendMessageToUI("\t " + theCommand);
        } 
        else if (theCommand.contains("PB 1 D")) {
            myClient.sendMessageToUI("Push Button 1 is down");
        } 
        else if (theCommand.contains("PB 1 U")) {
            myClient.sendMessageToUI("Push Button 1 is up");
        } 
        else if (theCommand.contains("PB 2 D")) {
            myClient.sendMessageToUI("Push Button 2 is down");
        } 
        else if (theCommand.contains("PB 2 U")) {
            myClient.sendMessageToUI("Push Button 2 is up");
        } 
        else if (theCommand.contains("PB 3 D")) {
            myClient.sendMessageToUI("Push Button 3 is down");
        } 
        else if (theCommand.contains("PB 3 U")) {
            myClient.sendMessageToUI("Push Button 3 is up");
        } 
    
    }

    public void handleServerMessage(String msg) {

        if (msg.charAt(0)!=0xFFFD) { //0xFFFD = UTF-8 encoding of 0xFF
            serverMessage += msg;
        } else {
            handleCompleteServerMessage(serverMessage);
            serverMessage = "";
        }
    }

    public void handleServerMessage(IOException ex) {
        myClient.sendMessageToUI("Unexpected disconnection from server. Disconnecting from server. Please try connecting to server again." + ex);
    }

}
