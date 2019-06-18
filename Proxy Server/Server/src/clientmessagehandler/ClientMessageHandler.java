/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientmessagehandler;

import clientconnection.ClientConnection;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ferens
 */
public class ClientMessageHandler{

    server.Server myServer;
    client.Client myClient;
    String theCommand = "";
    boolean isMessage = false;

    public ClientMessageHandler(server.Server myServer,client.Client myClient) {
        this.myServer = myServer;
        this.myClient = myClient;
    }
    

    public void handleClientMessage(clientconnection.ClientConnection myClientConnection, String msg) {
        //if (!msg.equals("255")) {
        if (msg.charAt(0)!=0xFFFD) { //0xFFFD = UTF-8 encoding of 0xFF
            theCommand += msg;
        } else {
            handleCompleteClientMessage(myClientConnection, theCommand);
            theCommand = "";
        }
    }



    public void handleClientMessage(String theExceptionalEvent) {
        myServer.sendMessageToUI(theExceptionalEvent);
    }
    
    

    public void handleCompleteClientMessage(clientconnection.ClientConnection myClientConnection, String theCommand) {
        
        switch (theCommand) {
            case "d":
                myServer.sendMessageToUI("Disconnect command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
                myClientConnection.sendStringMessageToClient("Disconnect Ack: " + myClientConnection.getClientSocket().getRemoteSocketAddress());
                myClientConnection.clientDisconnect();
                myServer.sendMessageToUI("\tDisconnect successful. ");
                break;
            case "q":
                myServer.sendMessageToUI("Quit command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
                myClientConnection.sendStringMessageToClient("Quit Ack: " + myClientConnection.getClientSocket().getRemoteSocketAddress());
                myClientConnection.clientQuit();
                myServer.sendMessageToUI("\tQuit successful. ");
                break;
            case "t":
                myServer.sendMessageToUI("Get Time command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
                Calendar cal = Calendar.getInstance();
                myClientConnection.sendStringMessageToClient("The time is: ");
                cal.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                for (int i = 0; i < sdf.format(cal.getTime()).length(); i++) {
                    byte msg;
                    msg = (byte) sdf.format(cal.getTime()).charAt(i);
                    myClientConnection.sendMessageToClient(msg);
                }
                myClientConnection.sendMessageToClient((byte) 0xFF);
                myServer.sendMessageToUI("\tClient given time: " + sdf.format(cal.getTime()));
                break;
            case "temp":
                myServer.sendMessageToUI("Temp command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);       
                break;
            case "L1on":
                myServer.sendMessageToUI("L1on command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);       
                break;
            case "L1off":
                myServer.sendMessageToUI("L1off command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);               
                break;
            case "L2on":
                myServer.sendMessageToUI("L2on command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);       
                break;
            case "L2off":
                myServer.sendMessageToUI("L2off command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);               
                break;
            case "L3on":
                myServer.sendMessageToUI("L3on command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);       
                break;
            case "L3off":
                myServer.sendMessageToUI("L3off command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);               
                break;
            case "L4on":
                myServer.sendMessageToUI("L4on command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);       
                break;
            case "L4off":
                myServer.sendMessageToUI("L4off command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);               
                break;
            case "gpb1":
                myServer.sendMessageToUI("gpb1 command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);       
                break;
            case "gpb2":
                myServer.sendMessageToUI("gpb2 command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);               
                break;
            case "gpb3":
                myServer.sendMessageToUI("gpb3 command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);               
                break;
            
        }
    }
    
    private String byteToString(byte theByte) {
        byte[] theByteArray = new byte[1];
        String theString = null;
        theByteArray[0] = theByte;
        try {
            theString = new String(theByteArray, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
            myServer.sendMessageToUI("Cannot convert from UTF-8 to String; exiting program.");
            System.exit(1);
        } finally {
            return theString;
        }
    }

}
