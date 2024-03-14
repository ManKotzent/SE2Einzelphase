package com.example.se2einzelphase;

import android.widget.TextView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MyThread extends Thread{
    private String input;
    private static TextView outputView;

    public MyThread(String input, TextView outputView) {
        this.input = input;
        this.outputView = outputView;
    }

    public MyThread(){}

    public void setInput (String input) {
        this.input = input;
    }

    @Override
    public void start() {
        super.start();

        if (!input.contains("[0-9]+")) {
            outputView.setText("Ungültiger Input");
            this.interrupt();
        }

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
            sb.append(numbs[i]);
        }
        String output = sb.toString();
        outputView.setText(output);
    }

    private boolean isPrime(int num) {
        if(num <= 1) return false;

        for(int i = 2; i <= num / 2; i++) {
            if((num % i) == 0) return false;
        }
        return true;
    }
}
