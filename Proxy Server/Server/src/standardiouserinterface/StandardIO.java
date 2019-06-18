package standardiouserinterface;

import java.io.*;

public class StandardIO implements Runnable, userinterface.UserInterface{

    BufferedReader console = null;
    usercommandhandler.UserCommandHandler myUserCommandHandler;
    String clientMessage;

    public StandardIO() {
        console = new BufferedReader(new InputStreamReader(System.in));
        if (console == null) {
            System.err.println("No Standard Input device, exiting program.");
            System.exit(1);
        }
    }
    
    public void setCommand(usercommandhandler.UserCommandHandler myUserCommandHandler) {
        this.myUserCommandHandler = myUserCommandHandler;
    }

    public void update(String theResult) {
        System.out.println(theResult);
    }
    
    public void setClientmessage(String command)
    {
        this.clientMessage = command;
    }

    @Override
    public void run() {
        String userInput = "no input";
        while (true) {
            try {
                userInput = console.readLine();
            } catch (IOException e) {
                System.err.println("Error reading from Standard Input device, exiting program.");
                System.exit(1);
            }
            myUserCommandHandler.handleUserCommand(userInput);
            //This should not take too long, else user-interface will be non-responsive
        }
    }
}
