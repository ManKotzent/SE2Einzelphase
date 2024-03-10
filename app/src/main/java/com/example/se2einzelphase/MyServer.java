package com.example.se2einzelphase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MyServer extends Thread{
    static private int port;
    public MyServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while(true) {
                try {
                    //Waits till connection is called. To terminate the thread, interrupt it, call a new connection so that Thread.sleep throws an interruption and finishes the program
                    Socket client = serverSocket.accept();
                    Thread.sleep(0);
                    MyServerActivity myServerActivity = new MyServerActivity(client);
                    myServerActivity.start();
                } catch (InterruptedException e) {
                    break;
                }
            }

            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class MyServerActivity extends Thread{
        Socket client;
        BufferedWriter out;
        BufferedReader in;

        private MyServerActivity(Socket client) {
            this.client = client;
            try {
                out = new BufferedWriter(new OutputStreamWriter(this.client.getOutputStream()));
                in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                loop:
                while (true) {
                    String taskType = in.readLine();

                    switch (taskType) {
                        case "Task1": {
                            String input = in.readLine();
                            if(input.length() == 8) out.write(input);
                            else out.write("Matrikelnummer falsch!");

                            break;
                        }
                        case "Task2": {
                            String input = in.readLine();
                            if (!input.contains("[0-9]+")) out.write("Input falsch!");

                            //Turn String into int Array
                            int[] numbs = new int[input.length()];
                            for (int i = 0; i < input.length(); i++) {
                                numbs[i] = input.charAt(i) - 48;
                            }

                            //Eliminate primes and turn into int List
                            List<Integer> validNumbs = new LinkedList<Integer>();
                            for(int i = 0; i < numbs.length; i++) {
                                if(!isPrime(numbs[i])) validNumbs.add(numbs[i]);
                            }

                            //Turn int List into int Array and sort
                            numbs = new int[validNumbs.size()];
                            for(int i = 0; i < numbs.length; i++) {
                                numbs[i] = validNumbs.get(i);
                            }
                            Arrays.sort(numbs);

                            //Turn int Array into String
                            StringBuilder sb = new StringBuilder();
                            for(int i = 0; i < validNumbs.size(); i++) {
                                sb.append(numbs[i] + 48);
                            }
                            String output = sb.toString();

                            //Send output
                            out.write(output);

                            break;
                        }
                        case "Terminate":
                            break loop;
                    }

                }

                out.close();
                in.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private boolean isPrime(int num) {
            if(num <= 1)
            {
                return false;
            }
            for(int i =2 ;i <= num / 2; i++)
            {
                if((num % i) == 0)
                    return  false;
            }
            return true;
        }
    }
}


