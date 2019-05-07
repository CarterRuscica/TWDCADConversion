package com.example.twdcadconversion;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public class dataGetter {
    private static final String DATAURL = "https://www.bankofcanada.ca/valet/observations/FXTWDCAD/json";


    public boolean isOnline(Context c){
        ConnectivityManager connMgr = (ConnectivityManager) c.getSystemService(c.CONNECTIVITY_SERVICE);
        NetworkInfo netWorkInfo = connMgr.getActiveNetworkInfo();
        if (netWorkInfo != null && netWorkInfo.isConnected())
            return true;
        else
            return false;
    }



    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while((cp = rd.read()) != -1){
            sb.append((char)cp);
        }
        return sb.toString();
    }

    public String getData(Context c) throws IOException{
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FileHelper fh = new FileHelper();
        InputStream is = new URL(DATAURL).openStream();
        try{
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            if (jsonText.length() > 5){
                fh.writeToFile(jsonText, c);
                return jsonText;
            }else{
                return fh.readFromFile(c);
            }
        } finally {
            is.close();
        }
    }

    private static String parseData(String json){
        String a = "Success";
        return a;
    }

}
