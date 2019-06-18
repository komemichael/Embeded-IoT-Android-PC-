package com.example.kome.client;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements com.example.kome.userinterface.UserInterface {

    com.example.kome.usercommandhandler.UserCommandHandler myUserCommandHandler;
    com.example.kome.client1.Client myClient;
    String led1 = "off";
    String led2 = "off";
    String led3 = "off";
    String led4 = "off";

    int port = 7777;
    String ipaddress = "140.193.235.171";//"140.193.235.165";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myClient = new com.example.kome.client1.Client( port , ipaddress, this);
        myUserCommandHandler = new com.example.kome.usercommandhandler.UserCommandHandler(this , myClient);
    }



    public void update(String theString) {
        Message msg = new Message();
        msg.obj = theString;
        msg.setTarget(myServerMessageTextBoxHandler);
        myServerMessageTextBoxHandler.sendMessage(msg);
    }

    @SuppressLint("HandlerLeak")
    Handler myServerMessageTextBoxHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            EditText serverMessage = findViewById(R.id.serverMessage);
            //serverMessage.append(msg.obj +"\n");
            serverMessage.setText(msg.obj + " ");
        }
    };

    public void connect(View view)
    {
        myUserCommandHandler.setCommand("c");
        Thread t = new Thread(myUserCommandHandler);
        t.start();
    }

    public void disconnect(View view)
    {
        myUserCommandHandler.setCommand("d");
        Thread t = new Thread(myUserCommandHandler);
        t.start();
    }

    public void gettime(View view)
    {
        myUserCommandHandler.setCommand("t");
        Thread t = new Thread(myUserCommandHandler);
        t.start();
    }


    public void led1(View view)
    {
        if (led1.equals("off"))
        {
            myUserCommandHandler.setCommand("L1on");
            Thread t = new Thread(myUserCommandHandler);
            t.start();
            led1 = "on";
        }
        else
        {
            myUserCommandHandler.setCommand("L1off");
            Thread t = new Thread(myUserCommandHandler);
            t.start();
            led1 = "off";
        }
    }

    public void led2(View view)
    {
        if (led2.equals("off"))
        {
            myUserCommandHandler.setCommand("L2on");
            Thread t = new Thread(myUserCommandHandler);
            t.start();
            led2 = "on";
        }
        else
        {
            myUserCommandHandler.setCommand("L2off");
            Thread t = new Thread(myUserCommandHandler);
            t.start();
            led2 = "off";
        }
    }

    public void led3(View view)
    {
        if (led3.equals("off"))
        {
            myUserCommandHandler.setCommand("L3on");
            Thread t = new Thread(myUserCommandHandler);
            t.start();
            led3 = "on";
        }
        else
        {
            myUserCommandHandler.setCommand("L3off");
            Thread t = new Thread(myUserCommandHandler);
            t.start();
            led3 = "off";
        }
    }

    public void led4(View view)
    {
        if (led4.equals("off"))
        {
            myUserCommandHandler.setCommand("L4on");
            Thread t = new Thread(myUserCommandHandler);
            t.start();
            led4 = "on";
        }
        else
        {
            myUserCommandHandler.setCommand("L4off");
            Thread t = new Thread(myUserCommandHandler);
            t.start();
            led4 = "off";
        }
    }

    public void btn1(View view)
    {
        myUserCommandHandler.setCommand("gpb1");
        Thread t = new Thread(myUserCommandHandler);
        t.start();
    }

    public void btn2(View view)
    {
        myUserCommandHandler.setCommand("gpb2");
        Thread t = new Thread(myUserCommandHandler);
        t.start();
    }

    public void btn3(View view)
    {
        myUserCommandHandler.setCommand("gpb3");
        Thread t = new Thread(myUserCommandHandler);
        t.start();
    }
}
