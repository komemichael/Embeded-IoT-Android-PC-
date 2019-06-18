/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Runnable{

    InputStream input;
    OutputStream output;
    byte msg = ' ';
    servermessagehandler.ServerMesasgeHandler myServerCommandHandler;
    ServerSocket serverSocket = null;
    Socket MyClient = null;
    int portNumber;
    boolean stopThisThread = true;
    standarduserinterface.standardUserInterface myUI;
    private StringBuffer sb = new StringBuffer();
    
    public Client(int portNumber, standarduserinterface.standardUserInterface myUI)
    {
        this.portNumber = portNumber;
        this.myUI = myUI;
        this.myServerCommandHandler = new servermessagehandler.ServerMesasgeHandler(this);
    }
    
    public void connectToServer()
    {
        try
        {
                MyClient = new Socket("localhost", 5570);
                //MyClient = new Socket("192.168.1.17", 7777);
                input = MyClient.getInputStream();
                output = MyClient.getOutputStream();
                Thread thisClientThread = new Thread(this);
                thisClientThread.start();
                stopThisThread = false;
        }
        catch (IOException e)
        {
            System.err.println("Cannot create client Socket, Exiting Program!!");
            System.exit(1);
        }
        finally{}
    }
    
    public void sendStringMessageToServer(String theMessage) {
        for (int i = 0; i < theMessage.length(); i++) {
            byte msg = (byte) theMessage.charAt(i);           
            sendMessageToServer(msg);
        }
    }
    
    
    public void sendMessageToServer(byte msg)
    {
        try
        {
            output.write(msg);
            output.flush();
        }
        catch(IOException e)
        {
            System.err.println("Cannot send to Socket, Exiting!!");
            System.exit(1);
        }
        finally
        {}
    }
    
    public boolean isConnected()
    {   
        boolean isConnected = true;
        if(stopThisThread == true)
        {
            return false;
        }
        return isConnected;
        
    }
    
    public void stopThread()
    {
        stopThisThread = true;
        try {
            MyClient.close();
            MyClient = null;
            input = null;
            output = null;
        } catch (IOException ex) {
            //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void setPort(int portNumber) {
        this.portNumber = portNumber;
    }

    public int getPort() {
        return this.portNumber;
    }
    
    public void sendMessageToUI(String theString) {
        myUI.update(theString);
    }
    
     
    @Override
    public void run()
    {
        while (stopThisThread == false) {
            try {
                String message = "";
                int c;
                try
                {
                    while ((c = input.read()) != 255){
                        message = message + ((char) c);
                        if(c == 255)break;
                    }
                }
                catch(NullPointerException e)
                {
                
                }
                myServerCommandHandler.handleServerMessage(this, message);
                
            } catch (IOException e) {
                myServerCommandHandler.handleServerMessage("IOException: " +e.toString() +
                    ". Stopping thread and disconnecting client: ");
                stopThisThread = true;
            }
        }
    }
}

