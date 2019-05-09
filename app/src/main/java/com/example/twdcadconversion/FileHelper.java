package com.example.twdcadconversion;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class FileHelper {
    final static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/twdcadconversion/readwrite";
    private static final String file = "currency.txt";

    public void writeToFile(String data, Context c){
        try{
            OutputStreamWriter osw = new OutputStreamWriter(c.openFileOutput(file, c.MODE_PRIVATE));
            osw.write(data);
            osw.close();
        }catch(IOException e){
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public String readFromFile(Context c){
        String ret = "";
        try{
            InputStream is = c.openFileInput(file);
            if(is != null){
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String rS = "";
                StringBuilder sb = new StringBuilder();
                while((rS = br.readLine()) != null){
                    sb.append(rS);
                }
                is.close();
                ret = sb.toString();
            }
        }catch(FileNotFoundException e){
            Log.e("login activity", "File not found: " + e.toString());
        }catch(IOException e){
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return ret;
    }
}
