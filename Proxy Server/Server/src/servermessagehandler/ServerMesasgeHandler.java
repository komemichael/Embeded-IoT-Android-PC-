/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servermessagehandler;
/**
 *
 * @author Owner
 */
public class ServerMesasgeHandler implements Runnable{
    
    client.Client myClient;
    String message;
    clientconnection.ClientConnection cmh;
    
    public ServerMesasgeHandler(client.Client myClient)
    {
        this.myClient = myClient;
        this.message = "";
    }
    
    @Override
    public void run()
    {
        myClient.sendMessageToUI(this.message);
        cmh.sendStringMessageToClient(message);
        cmh.sendMessageToClient((byte) 0xFF); 
        this.message = "";
    }

    
    public void handleServerMessage(client.Client myClient, byte msg) 
    {       
        if (msg != -1){ 
            message = message + ((char) msg);
        }
        else
        {
            //Handle Complete server message
            cmh = myClient.getCC();
            Thread t = new Thread(this);
            t.start();
        }
    }

    public void handleServerMessage(String message) {
        
        myClient.sendMessageToUI("Error: " + message);
    }
}
