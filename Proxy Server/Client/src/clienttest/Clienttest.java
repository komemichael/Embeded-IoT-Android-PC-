/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienttest;

/**
 *
 * @author Owner
 */
public class Clienttest {
    public static void main(String[] args)
    {
        final standarduserinterface.standardUserInterface myUI = new standarduserinterface.standardUserInterface();
        client.Client myClient = new client.Client(7777, myUI);
        usercommandhandler.UserCommandHandler myUserCommandHandler = new usercommandhandler.UserCommandHandler(myUI, myClient);
        myUI.setCommandHandler(myUserCommandHandler);
        
        Thread myUIthread = new Thread(myUI);
        myUIthread.start();     
        myUI.update("1:\t for temperature");
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() 
//            {
//                myUI.setVisible(true);
//            }
//        });
    }
}
