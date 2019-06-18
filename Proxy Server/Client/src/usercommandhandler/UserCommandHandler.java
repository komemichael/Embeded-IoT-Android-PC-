/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usercommandhandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Owner
 */

public class UserCommandHandler {
    standarduserinterface.standardUserInterface myUI;
    client.Client myClient;

    public UserCommandHandler(standarduserinterface.standardUserInterface myUI, client.Client myClient) {
        this.myUI = myUI;
        this.myClient = myClient;
    }

    
    public void execute(String theCommand) {
        if(theCommand.equals("c"))
        {
            myUI.update("connecting to Server...");
            myClient.connectToServer();
            if (myClient.isConnected() == true)
            {
                myUI.update("Connected \n\n");
            }
            else
            {
                myClient.connectToServer();
                
                myUI.update("Client No longer Connected!\n");
            }
        }
        else
            if(theCommand.equals("d"))
        {
            
            if(myClient.isConnected() == true)
            {
                
                myClient.sendMessageToServer((byte) theCommand.charAt(0));
                myClient.sendMessageToServer((byte) 0xFF);
                myClient.stopThread();
                myUI.update("Disconnected from server\n\n");
            }
            else
            {
                myUI.update("nDisconnected from server\n\n");
            }
        }
        else
            if(theCommand.equals("q"))
        {            
            if(myClient.isConnected() == true)
            {
                myClient.sendMessageToServer((byte) theCommand.charAt(0));
                myClient.sendMessageToServer((byte) 0xFF);
                myClient.stopThread();
            }
            else
            {
                myUI.update("Quiting Program!!\n\n");
                System.exit(0);
            }
        }else
            if(theCommand.equals("t"))
        {
            if(myClient.isConnected() == true)
            {
                myClient.sendMessageToServer((byte)  theCommand.charAt(0));
                myClient.sendMessageToServer((byte) 0xFF);
            }
            else
            {
                myUI.update("Client Not Connected\n\n");
            }
        }
        else
            if(theCommand.equals("L1on"))
        {
            if(myClient.isConnected() == true)
            {
            myClient.sendStringMessageToServer(theCommand);
            myClient.sendMessageToServer((byte) 0xFF);
            }
            else
            {
                myUI.update("Client Not Connected\n\n");
            }
        }
            else
            if(theCommand.equals("L1off"))
        {
            if(myClient.isConnected() == true)
            {
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);
            }
            else
            {
                myUI.update("Client Not Connected");
            }
        }
            else
            if(theCommand.equals("L2on"))
        {
            if(myClient.isConnected() == true)
            {
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);
            }
            else
            {
                myUI.update("Client Not Connected");
            }
        }
        else
            if(theCommand.equals("L2off"))
        {
            if(myClient.isConnected() == true)
            {
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);
            }
            else
            {
                myUI.update("Client not Connected");
            }
        }
            else
            if(theCommand.equals("L3on"))
        {
            if(myClient.isConnected() == true)
            {
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);
            }
            else
            {
                myUI.update("Client Not Connected");
            }
        }
        else
            if(theCommand.equals("L3off"))
        {
            if(myClient.isConnected() == true)
            {
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);
            }
            else
            {
                myUI.update("Client Not Connected");
            }
        }
            else
            if(theCommand.equals("L4on"))
        {
            if(myClient.isConnected() == true)
            {
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);
            }
            else
            {
                myUI.update("Client Not Connected");
            }
        }
        else
            if(theCommand.equals("L4off"))
        {
            if(myClient.isConnected() == true)
            {
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);
            }
            else
            {
                myUI.update("Client Not Connected");
            }
        }
        else
            if(theCommand.equals("gpb1"))
        {
            if(myClient.isConnected() == true)
            {
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);
            }
            else
            {
                myUI.update("Client Not Connected");
            }
        }
        else
            if(theCommand.equals("gpb2"))
        {
            if(myClient.isConnected() == true)
            {
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);
            }
            else
            {
                myUI.update("Client Not Connected");
            }
        }
        else
            if(theCommand.equals("gpb3"))
        {
            if(myClient.isConnected() == true)
            {
                myClient.sendStringMessageToServer(theCommand);
                myClient.sendMessageToServer((byte) 0xFF);
            }
            else
            {
                myUI.update("Client Not Connected");
            }
        }
    }
}

