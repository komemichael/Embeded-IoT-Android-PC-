/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package standarduserinterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Owner
 */
public class standardUserInterface implements Runnable {

    BufferedReader console = null;
    usercommandhandler.UserCommandHandler myUserCommandHandler;

    public standardUserInterface() {
        console = new BufferedReader(new InputStreamReader(System.in));
        if (console == null) {
            System.err.println("No Standard Input device, exiting program.");
            System.exit(1);
        }
    }
    
    public void setCommandHandler(usercommandhandler.UserCommandHandler myUserCommandHandler) {
        this.myUserCommandHandler = myUserCommandHandler;
    }

    public void update(String theResult) {
        System.out.println(theResult);
    }

    @Override
    public void run() {
        String userInput = "no input";
        while (true) {
            try {
                userInput = console.readLine();
            } catch (IOException e) {
                System.err.println("Error reading from "
                        + "Standard Input device, "
                        + "exiting program.");
                System.exit(1);
            }
            myUserCommandHandler.execute(userInput);
            //This should not take too long, else 
            //user-interface will be non-responsive
        }
    }
}
