package com.example.twdcadconversion;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class DoubleBuilder {

    private int decimalLength = 0, decimalMaxLength;
    private int integerLength = 0, integerMaxLength;
    private boolean decimalMode = false;

    private static final String FILENAME = "DoubleBuilderData.txt";

    private Double currency;

    public DoubleBuilder(int integerMaxLength, int decimalLength){
        this.decimalMaxLength = decimalLength;
        this.integerMaxLength = integerMaxLength;
        this.currency = 0.0;
//        loadData();
    }

    public Double getInt() {
        return this.currency;
    }

    public void toggleDecimal(){
        decimalMode = true;
    }

    public void addNumber(int i){
        if (decimalMode){
            if (decimalLength < decimalMaxLength) {
                decimalLength++;
                currency = currency + i * Math.pow(0.1, decimalLength);
            }
        }else{
            if(integerLength < integerMaxLength) {
                currency = currency * 10 + i;
                integerLength++;
            }
        }
        saveData();
    }

    public void clear(){
        currency = 0.0;
        integerLength = 0;
        decimalLength = 0;
        decimalMode = false;
        saveData();
    }

    public void backSpace(){
        if (decimalMode){
            int temp = decimalLength;
            currency = currency*Math.pow(10, temp);
            decimalLength--;
            int subtract = (int)Math.round(currency%10);
            currency -=subtract;
            currency = round(currency*Math.pow(0.1, temp), 2);
            if (decimalLength == 0){
                decimalLength = 1;
                decimalMode = false;
            }
        }else{
            if (integerLength > 0) {
                integerLength--;
                int subtract = (int) Math.round(currency % 10);
                currency -= subtract;
                currency = currency * 0.1;
            }
        }
        saveData();
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void saveData(){
        System.out.println("Working on saving file");
        try {
            PrintWriter pw = new PrintWriter(FILENAME);
            String line =  currency + " " + integerLength + " " + decimalLength + " " + decimalMode;
            pw.println(line);
            pw.close();
        }catch(FileNotFoundException e){
            Log.i("File Error", "File error in saveData");
        }

    }

    public void loadData(){
        try{
            File file = new File(FILENAME);
            Scanner sc = new Scanner(file);
            currency = sc.nextDouble();
            integerLength = sc.nextInt();
            decimalLength = sc.nextInt();
            decimalMode = sc.nextBoolean();
        }catch (FileNotFoundException e){
            Log.i("File Error", "File not found");
        }
    }


}
