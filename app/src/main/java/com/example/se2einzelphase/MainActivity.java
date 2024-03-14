package com.example.se2einzelphase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    static int port = 20080;

    MyThread myThread;
    Socket socket;
    BufferedWriter out;
    BufferedReader in;
    Button buttonTaskOne, buttonTaskTwo;
    TextView input;
    TextView serverAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        buttonTaskOne = (Button) findViewById(R.id.buttonTask1);
        buttonTaskTwo = (Button) findViewById(R.id.buttonTask2);
        input = (TextView) findViewById(R.id.matrikelNumberField);
        serverAnswer = (TextView) findViewById(R.id.serverAnswerTextView);

        buttonTaskOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //Reestablishing a connection is seemingly necessary as otherwise the server will return null
                    Log.d("SERVER-CON", "Opening connection.");
                    socket = new Socket("se2-submission.aau.at", port);
                    out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    Log.d("SERVER-CON", "Sending message.");
                    out.write(new String((input.getText().toString()).getBytes(StandardCharsets.US_ASCII), StandardCharsets.UTF_8));
                    out.newLine();
                    out.flush();

                    Log.d("SERVER-CON", "Waiting for answer.");
                    String messageReceived = in.readLine();
                    Log.d("SERVER-CON", "Answer received: " + messageReceived);

                    serverAnswer.setText(messageReceived);

                    Log.d("SERVER-CON", "Closing connection.");
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException e) {
                    Log.d(e.getClass().toString(), e.toString());
                }
            }
        });

        buttonTaskTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("THREADS", "Starting Thread.");
                myThread = new MyThread(input.getText().toString(), serverAnswer);
                myThread.start();
            }
        });
    }

    protected void onDestroy(){
        super.onDestroy();

        Log.d("THREADS", "Joining Thread");
        try {
            myThread.join();
        } catch (InterruptedException e) {
            Log.e(e.getClass().toString(), e.toString());
        }
        Log.d("THREADS", "Joined Thread");
    }
}