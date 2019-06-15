package com.example.twdcadconversion;

import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CurrencyArray {
    private static final String CURL = "https://www.bankofcanada.ca/valet/observations/FX";
    private static final String INFO_URL = ("https://www.bankofcanada.ca/valet/observations/group/FX_RATES_DAILY/csv?recent=1");
    private static final String FILENAME = "data.txt";
    private static final String DOWNLOAD_FILE = "currency.csv";
    List<rateObject> rateList = new ArrayList<>();
    private int length;

    public CurrencyArray(){
        try{
            downloadData();
        }catch(IOException e){
            System.out.println("File could not be downloaded");
        }
        csvToObject();
    }

    public Double getRate(String c1, String c2, double convert){
        rateObject temp;
        Double frate = 1.0, srate = 1.0;
        for (int i = 0; i < length-1; i++){
            temp = rateList.get(i);
            if (temp.c1.equals(c1)){
                System.out.println("We have the 1st object: " + temp.toString());
                frate = temp.rate;
            }
            if (temp.c1.equals(c2)){
                System.out.println("We have the 2nd object: " + temp.toString());
                srate = temp.rate;
            }
        }
        return frate/srate*convert;
    }


    /**
     * This method uses the downloaded file of CSV values then to load it and save it onto the device.
     *
     */
    private void csvToObject(){
        String line;
        try(Scanner input = new Scanner(new File(DOWNLOAD_FILE))){
            while(input.hasNext()) {
                line = input.nextLine();
                if (line.equals("OBSERVATIONS")){
                    String[] csvCList = input.nextLine().split(",");
                    String[] csvRateList = input.nextLine().split(",");
                    String date = csvRateList[0];
                    rateObject temp;
                    length = csvRateList.length;
                    for (int i = 1; i < length; i++){
                        temp = new rateObject(Double.parseDouble(csvRateList[i]), csvCList[i].substring(2, 5), csvCList[i].substring(5, 8), date);
                        rateList.add(temp);
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * This method downloads and saves the data from the INFO_URL
     * @throws IOException
     */
    private void downloadData() throws IOException {
        URL InfoURL = new URL(INFO_URL);
        ReadableByteChannel rbc = Channels.newChannel(InfoURL.openStream());
        FileOutputStream fos = new FileOutputStream(DOWNLOAD_FILE);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
    }

    /**
     * This method is for debugging
     */
    public void printList(){
        int size = rateList.size(), index = 0;
        rateObject temp;
        while(index < size){
            temp = rateList.get(index);
            index++;
            System.out.println("Our Objects: " + temp.toString());
        }
    }



}

class rateObject{
    double rate;
    String date;
    String c1, c2;

    public rateObject(double rate, String c1, String c2, String date){
        this.rate = rate;
        this.c1 = c1;
        this.c2 = c2;
        this.date = date;
    }

    @Override
    public String toString(){
        return (this.c1 + this.c2 + " " + this.rate + " " + this.date);
    }

}