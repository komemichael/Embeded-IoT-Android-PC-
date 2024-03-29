package servertest;

/**
 *
 * @author ferens
 */
public class ServerTest {

    public static void main(String[] args) {
        standardiouserinterface.StandardIO myUI = new standardiouserinterface.StandardIO();
        client.Client myClient = new client.Client(5555, myUI);
        server.Server myServer = new server.Server(7777, 1, myUI, myClient);
        usercommandhandler.UserCommandHandler myUserCommandHandler = new usercommandhandler.UserCommandHandler(myUI,myServer);
        myUI.setCommand(myUserCommandHandler);
        Thread myUIthread = new Thread(myUI);
        myUIthread.start();     
        myUI.update("1:\tQuit\n2:\tlisten\n3:\tSet Port\n4:\tGet Port\n5:\tStop listening\n6:\tStart Server Socket\n");
    }
}
