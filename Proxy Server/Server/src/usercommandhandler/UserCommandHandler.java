/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package usercommandhandler;

/**
 *
 * @author ferens
 */
public class UserCommandHandler implements Runnable{
    userinterface.UserInterface myUI;
    server.Server myServer;
    String command = "";
    
    public void run()
    {
        handleUserCommand(command);
    }
    
    public void setCommand(String command)
    {
        this.command = command;
    }

    public UserCommandHandler(userinterface.UserInterface myUI, server.Server myServer) {
        this.myUI = myUI;
        this.myServer = myServer;
    }

    public void handleUserCommand(String theCommand) {
        
        switch (Integer.parseInt(theCommand)) {
            case 1: //QUIT
                myServer.stopServer();
                myUI.update("Quiting program by User command.");
                System.exit(-1);
                break;
            case 2: //LISTEN
                myServer.listen();
                myUI.update("Server is now listening, ...");
                break;
            case 3: //SET PORT
                myUI.update("The port number set function is not available at this time.");
                break;
            case 4: //GET PORT
                myUI.update("The port number is: " +String.valueOf(myServer.getPort()));
                break;
            case 5: //Stop Listening
                myServer.stopListening();
                myUI.update("Server is not listening, ...");
                break;
            case 6: //START SERVER SOCKET
                myServer.startServer();
                myUI.update("Server and client Socket has been created.");
                break;
            default:
                break;
        }
    }
}
