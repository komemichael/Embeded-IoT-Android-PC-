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
public class ServerMesasgeHandler {
    
    client.Client myClient;
    
    public ServerMesasgeHandler(client.Client myClient)
    {
        this.myClient = myClient;
    }
    
    
     public void handleServerMessage(client.Client myClient,String msg) {
        if(myClient.isConnected() == true){
            myClient.sendMessageToUI(msg);
        }
        else
        {
            myClient.sendMessageToUI("Client not connected\n");
        }
    }

    public void handleServerMessage(String message) {
        myClient.sendMessageToUI(message);
    }
}
