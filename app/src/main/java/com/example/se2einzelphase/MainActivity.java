package com.example.se2einzelphase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    static int port = 20080;

    MyServer myServer;
    Socket socket;
    BufferedWriter out;
    BufferedReader in;
    Button buttonTaskOne, buttonTaskTwo;
    TextView input;
    TextView serverAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myServer = new MyServer(port);

        try {
            socket = new Socket("localhost", port);
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        buttonTaskOne = (Button) findViewById(R.id.buttonTask1);
        buttonTaskTwo = (Button) findViewById(R.id.buttonTask2);
        input = (TextView) findViewById(R.id.matrikelNumberField);
        serverAnswer = (TextView) findViewById(R.id.serverAnswerTextView);
        buttonTaskOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    out.write("Task1");
                    out.write((String) input.getText());

                    String messageReceived = in.readLine();

                    serverAnswer.setText(messageReceived);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        buttonTaskTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    out.write("Task2");
                    out.write((String) input.getText());

                    String messageReceived = in.readLine();

                    serverAnswer.setText(messageReceived);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        setContentView(R.layout.activity_main);
    }

    protected void onDestroy(){
        super.onDestroy();
        try {
            //Terminate connection with Server Activity
            out.write("Terminate");
            in.close();
            out.close();
            socket.close();

            //Wake up Server and interrupt
            myServer.interrupt();
            Socket wakeupSocket = new Socket("se2-submission.aau.at", port);
            wakeupSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}