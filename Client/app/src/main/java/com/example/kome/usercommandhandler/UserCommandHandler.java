package com.example.kome.usercommandhandler;

import java.io.IOException;

public class UserCommandHandler implements Runnable{
    com.example.kome.userinterface.UserInterface myUI;
    com.example.kome.client1.Client myClient;

    String theString;
    Boolean stopthisthread = false;

    public UserCommandHandler(com.example.kome.userinterface.UserInterface myUI, com.example.kome.client1.Client myClient) {
        this.myUI = myUI;
        this.myClient = myClient;
    }

    public void setCommand(String command)
    {
        this.theString = command;
    }

    @Override
    public void run()
    {
        handleUserCommand(this.theString);
    }

    public void handleUserCommand(String myCommand) {
        switch (myCommand) {
            case "q": //QUIT
                if (myClient.isConnected()) {
                    //myClient.stopThread();
                    myClient.sendMessageToServer((byte) 'q');
                    myClient.sendMessageToServer((byte) 0xFF);
                    //myClient.disconnectFromServer();
                    //System.exit(-1);
                } else {
                    System.exit(-1);
                }
                break;
            case "c": //Connect to Server
                if (myClient.connectToServer() == true) {
                    myUI.update("Connection to Server was successful.");
                } else {
                    myUI.update("Already connected to a server.");
                }
                break;
            case "d": //Disconnect From Server
                if (myClient.isConnected()) {
                    myClient.stopThread();
                    myClient.sendMessageToServer((byte)'d');
                    myClient.sendMessageToServer((byte) 0xFF);
                    myClient.disconnectFromServer();
                    myUI.update("Disconnect from Server was successful.");
                } else {
                    myUI.update("Not connected to a Server.");
                }                    
                break;
            case "t": //Request the time
                if (myClient.isConnected()) {
                    myClient.sendMessageToServer((byte) 't');
                    myClient.sendMessageToServer((byte) 0xFF);
                } else {
                    myUI.update("Not connected to a Server.");
                }                    
                break;
            case "L1on": //Switch LED 1 on
                if (myClient.isConnected()) {
                    myClient.sendStringMessageToServer("L1on");
                    myClient.sendMessageToServer((byte) 0xFF);
                } else {
                    myUI.update("Not connected to a Server.");
                }
                break;
            case "L2on": //Switch LED 2 on
                if (myClient.isConnected()) {
                    myClient.sendStringMessageToServer("L2on");
                    myClient.sendMessageToServer((byte) 0xFF);
                } else {
                    myUI.update("Not connected to a Server.");
                }
                break;
            case "L3on": //Switch LED 3 on
                if (myClient.isConnected()) {
                    myClient.sendStringMessageToServer("L3on");
                    myClient.sendMessageToServer((byte) 0xFF);
                } else {
                    myUI.update("Not connected to a Server.");
                }
                break;
            case "L4on": //Switch LED 4 on
                if (myClient.isConnected()) {
                    myClient.sendStringMessageToServer("L4on");
                    myClient.sendMessageToServer((byte) 0xFF);
                } else {
                    myUI.update("Not connected to a Server.");
                }
                break;
            case "L1off": //Switch LED 1 off
                if (myClient.isConnected()) {
                    myClient.sendStringMessageToServer("L1off");
                    myClient.sendMessageToServer((byte) 0xFF);
                } else {
                    myUI.update("Not connected to a Server.");
                }
                break;
            case "L2off": //Switch LED 2 off
                if (myClient.isConnected()) {
                    myClient.sendStringMessageToServer("L2off");
                    myClient.sendMessageToServer((byte) 0xFF);
                } else {
                    myUI.update("Not connected to a Server.");
                }
                break;
            case "L3off": //Switch LED 3 off
                if (myClient.isConnected()) {
                    myClient.sendStringMessageToServer("L3off");
                    myClient.sendMessageToServer((byte) 0xFF);
                } else {
                    myUI.update("Not connected to a Server.");
                }
                break;
            case "L4off": //Switch LED 4 on
                if (myClient.isConnected()) {
                    myClient.sendStringMessageToServer("L4off");
                    myClient.sendMessageToServer((byte) 0xFF);
                } else {
                    myUI.update("Not connected to a Server.");
                }
                break;
            case "gpb1": //Get the state of Push Button 1
                if (myClient.isConnected()) {
                    myClient.sendStringMessageToServer("gpb1");
                    myClient.sendMessageToServer((byte) 0xFF);
                } else {
                    myUI.update("Not connected to a Server.");
                }
                break;
             case "gpb2": //Get the state of Push Button 2
                 if (myClient.isConnected()) {
                     myClient.sendStringMessageToServer("gpb2");
                     myClient.sendMessageToServer((byte) 0xFF);
                 } else {
                     myUI.update("Not connected to a Server.");
                 }
                 break;
            case "gpb3": //Get the state of Push Button 3
                if (myClient.isConnected()) {
                    myClient.sendStringMessageToServer("gpb3");
                    myClient.sendMessageToServer((byte) 0xFF);
                } else {
                    myUI.update("Not connected to a Server.");
                }
                break;
            default:
                myUI.update("What? Command not recognized.");
                break;
        }
        theString= "";
    }
}