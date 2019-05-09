package com.example.twdcadconversion;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public class dataGetter {
//    private static final String CURL = "https://www.bankofcanada.ca/valet/observations/FXCADTWD/json";
    private static final String CURL = "https://www.bankofcanada.ca/valet/observations/FX";
//    private static final String


    public boolean isOnline(Context c){
        ConnectivityManager connMgr = (ConnectivityManager) c.getSystemService(c.CONNECTIVITY_SERVICE);
        NetworkInfo netWorkInfo = connMgr.getActiveNetworkInfo();
        if (netWorkInfo != null && netWorkInfo.isConnected())
            return true;
        else
            return false;
    }


    private static String readAll(Reader rd, String curr1, String curr2) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while((cp = rd.read()) != -1){
            sb.append((char)cp);
        }
        String data = sb.toString();

        try{
            data = parseData(data, curr1, curr2);
        }catch (JSONException e){
            Log.e("TAG", "Cannot parse: " + e.toString());
        }
        return data;
    }

    public String getData(Context c, String curr1, String curr2) throws IOException{
        //private static final String CURL = "https://www.bankofcanada.ca/valet/observations/FX";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FileHelper fh = new FileHelper();
        String url = CURL + curr1 + curr2 + "/json";
        InputStream is = new URL(url).openStream();
        try{
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd, curr1, curr2);
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

    private static String parseData(String json, String curr1, String curr2)throws JSONException {
        JSONObject jsonObj = new JSONObject(json);
        JSONArray jsonRay = jsonObj.getJSONArray("observations");
        String pars = "FX" + curr1 + curr2;
        String data = jsonRay.getJSONObject(jsonRay.length()-1).getString(pars);
        return data.replaceAll("[^\\d.]","");
    }

}
