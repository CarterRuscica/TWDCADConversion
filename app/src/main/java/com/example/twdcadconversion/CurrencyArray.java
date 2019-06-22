package com.example.twdcadconversion;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;


public class CurrencyArray {
    private static final String DOWNLOAD_FILE = "currency.csv";
    private static final String INFO_URL = ("https://www.bankofcanada.ca/valet/observations/group/FX_RATES_DAILY/csv?recent=1");

    List<rateObject> rateList = new ArrayList<>();
    private int length;
    private boolean canUpdate = true;


    public CurrencyArray(){
        csvToObject();
        rateObject temp;
        if(rateList.size() > 1){
            temp = rateList.get(1);
            updateData(temp);
        }
        if (canUpdate == true){
            new DownloadFileFromURL().execute(INFO_URL);
        }
    }

    /**
     * This code is from stackoverflow
     * https://stackoverflow.com/questions/20165564/calculating-days-between-two-dates-with-java
     * @param day1
     * @param day2
     * @return
     */

    public static int daysBetween(Calendar day1, Calendar day2){
        Calendar dayOne = (Calendar) day1.clone(),
                dayTwo = (Calendar) day2.clone();

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                // getActualMaximum() important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays ;
        }
    }

    /**
     * This method decides whether it is necessary to download the .csv file
     * If it is current we do not need to update
     * If it is the weekend and we have friday's data, we do not need to update
     * Otherwise we are going to search for the csv file
     *
     * @param temp
     */
    private void updateData(rateObject temp){
        Calendar cal = Calendar.getInstance();
        String[] sDate = temp.date.split("-");
        Calendar forDays = Calendar.getInstance();
        forDays.set(Integer.parseInt(sDate[0]), (Integer.parseInt(sDate[1])-1), Integer.parseInt(sDate[2]));

        int days = daysBetween(cal, forDays);

        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
            cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
//            cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY){
            if (days <= 3){
                canUpdate = false;
            }
        }else if(days == 0){
            canUpdate = false;
        }
    }

    /**
     *
     * Works in the way of all currencies are USDCAD, TWDCAD, EURCAD so one way
     * Rearranging formula we get, c1 rate / c2 rate * rate
     * It works
     * @param c1 - from currency
     * @param c2 - to currency
     * @param convert - amount of what we are going to convert
     * @return
     */

    public Double getRate(String c1, String c2, double convert){
        if(rateList.size() < 1){
            csvToObject();
        }
        rateObject temp;
        Double frate = 1.0, srate = 1.0;
        for (int i = 0; i < length-1; i++){
            temp = rateList.get(i);
            if (temp.c1.equals(c1)){
                frate = temp.rate;
            }
            if (temp.c1.equals(c2)){
                srate = temp.rate;
            }
        }
//        Log.i("Search Currency details", c1 + c2 + " IS THE SEARCHED CURRENCY FOR: " + convert);

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

            byte[] data = new byte[1024];

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

/**
 * Rate object allows for a list of the data for the device to search for
 * Could be optimized by not having a string date as it could be initialized to a global variable
 * AKA
 * define rateobject;
 * define date object once;
 * all dates are equal. Waste of memory.
 */
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