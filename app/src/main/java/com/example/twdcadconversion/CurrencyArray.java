package com.example.twdcadconversion;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CurrencyArray {
    private static final String DOWNLOAD_FILE = "currency.csv";
    private static final String INFO_URL = ("https://www.bankofcanada.ca/valet/observations/group/FX_RATES_DAILY/csv?recent=1");

    List<rateObject> rateList = new ArrayList<>();
    private int length;
    Context context;
    InputStream is;
    File path;


    public CurrencyArray(){
        new DownloadFileFromURL().execute(INFO_URL);

        csvToObject();
    }

    public Double getRate(String c1, String c2, double convert){
        rateObject temp;
        Double frate = 1.0, srate = 1.0;
        for (int i = 0; i < length-1; i++){
            temp = rateList.get(i);
            if (temp.c1.equals(c1)){
//                System.out.println("We have the 1st object: " + temp.toString());
                frate = temp.rate;
            }
            if (temp.c1.equals(c2)){
//                System.out.println("We have the 2nd object: " + temp.toString());
                srate = temp.rate;
            }
        }
        Log.i("Search Currency details", c1 + c2 + " IS THE SEARCHED CURRENCY FOR: " + convert);
        return frate/srate*convert;
    }

    /**
     *
     * @return returns the data which was last updated by the program
     */

    public String getData(){
        rateObject temp = rateList.get(0);
        return temp.date;
    }


    /**
     * This method uses the downloaded file of CSV values then to load it and save it onto the device.
     *
     */
    private void csvToObject(){
        String line;
        File csv = new File(Environment.getExternalStorageDirectory().toString() + "/" + DOWNLOAD_FILE);
        try{
            Scanner input = new Scanner(csv);
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
            Log.i("Object Loading Fail", "Could not find CSV");
            e.printStackTrace();
        }

    }

    /**
     * This method is for debugging
     */
    public void printList(){
        int size = rateList.size(), index = 0;
        rateObject temp;
        while(index < size-1){
            temp = rateList.get(index);
            index++;
            Log.i("ARRAY CONTAINS: ", rateList.get(index).toString());
//            System.out.println("Our Objects: " + temp.toString());
        }
    }



}

/**
 * Background Async Task to download file
 * */
class DownloadFileFromURL extends AsyncTask<String, String, String> {
    private static final String DOWNLOAD_FILE = "currency.csv";
    /**
     * Before starting background thread Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection connection = url.openConnection();
            connection.connect();

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            int lenghtOfFile = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            // Output stream
            OutputStream output = new FileOutputStream(Environment
                    .getExternalStorageDirectory().toString()
                    + "/" + DOWNLOAD_FILE);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();


        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
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