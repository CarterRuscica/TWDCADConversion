package com.example.twdcadconversion;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity{
    Context context;
    private static final String SAVEFILE = "lastuse.txt";


    /**
     * These variables are used for the Spinner and also converting spinner information into a readable currency
     */
    CustomAdapter adapter;
    Spinner sp1, sp2;
    //Probably could've just made an array of the currencies that match along the curreny ones by doing index
    String[][] searchCurrency = {{"Canadian dollar", "CAD"},{"Australian dollar", "AUD"}, {"Brazilian real","BRL"},{"Chinese renminbi", "CNY"}, {"European Euro","EUR"}, {"Hong Kong dollar", "HKD"}
            , {"Indian rupee","IDR"}, {"Indonesian rupiah","IDR"}, {"Japanese yen","JPY"} , {"Malaysian ringgit","MYR"}, {"New Zealand dollar", "NZD"}
            , {"Norwegian krone","NOK"} , {"Peruvian new sol","PEN"} , {"Saudi riyal","SAR"} , {"Singapore dollar","SGD"} , {"South African rand", "ZAR"}
            , {"South Korean won","KRW"} , {"Swedish krona","SEK"} , {"Swiss franc","SEK"} , {"Taiwanese dollar","TWD"} , {"Thai baht","THB"} , {"Turkish lira","TRY"}
            , {"British Pound","GBP"} , {"US Dollar","USD"} , {"Vietnamese dong","VND"}};
    String[] names = {"Canadian dollar","Australian dollar", "Brazilian real", "Chinese renminbi", "European Euro", "Hong Kong dollar"
            , "Indian rupee", "Indonesian rupiah", "Japanese yen", "Malaysian ringgit", "New Zealand dollar"
            , "Norwegian krone", "Peruvian new sol", "Saudi riyal", "Singapore dollar", "South African rand"
            , "South Korean won", "Swedish krona", "Swiss franc", "Taiwanese dollar", "Thai baht", "Turkish lira"
            , "British Pound", "US Dollar", "Vietnamese dong"};
    int[] images = {R.drawable.ca, R.drawable.au, R.drawable.br, R.drawable.cn, R.drawable.be, R.drawable.hk
            ,R.drawable.in, R.drawable.id, R.drawable.jp,R.drawable.my, R.drawable.nz
            ,R.drawable.no, R.drawable.pe, R.drawable.sa, R.drawable.sg, R.drawable.za
            ,R.drawable.kr, R.drawable.se, R.drawable.li, R.drawable.tw, R.drawable.th, R.drawable.tr
            , R.drawable.gb, R.drawable.us, R.drawable.vn};

    /**
     * Initializing helper objects
     * ca being the main currency converting app
     * db being the number handler object
     */
    CurrencyArray ca = new CurrencyArray();
    DoubleBuilder db = new DoubleBuilder(10, 2);

    TextView firstCurrency, convertedCurrency;
    String fC, cC;
    int fCindex, cCindex;

    private static final String FILENAME = "memCurr.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = getApplicationContext();

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);



        sp1 = findViewById(R.id.spinner);
        sp2 = findViewById(R.id.spinner2);

        firstCurrency = findViewById(R.id.FirstCurrency);
        firstCurrency.setText(db.getCurrency().toString());
        convertedCurrency = findViewById(R.id.SecondCurrency);

        adapter = new CustomAdapter(this, names, images);
        sp1.setAdapter(adapter);
        sp2.setAdapter(adapter);

        String text = sp1.getSelectedItem().toString();
        Log.d("System Debugging: ", "SPINNER POSITION BY STRING " + text);



        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), names[position], Toast.LENGTH_SHORT).show();
                updateNums();
                saveIndexes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), names[position], Toast.LENGTH_SHORT).show();
                updateNums();
                saveIndexes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        loadIndexes();
    }

    /**
     * Button method to do input based on button
     * If button to be added remember to add buttonClick to the button in OnClick in the .xml
     * @param view
     */

    public void buttonClick(View view){
        switch(view.getId()){
            case R.id.one:
                db.addNumber(1);
                break;
            case R.id.two:
                db.addNumber(2);
                break;
            case R.id.three:
                db.addNumber(3);
                break;
            case R.id.four:
                db.addNumber(4);
                break;
            case R.id.five:
                db.addNumber(5);
                break;
            case R.id.six:
                db.addNumber(6);
                break;
            case R.id.seven:
                db.addNumber(7);
                break;
            case R.id.eight:
                db.addNumber(8);
                break;
            case R.id.nine:
                db.addNumber(9);
                break;
            case R.id.zero:
                db.addNumber(0);
                break;
            case R.id.decimal:
                db.toggleDecimal();
                break;
            case R.id.clear:
                Toast.makeText(getApplicationContext(), "Cleared", Toast.LENGTH_SHORT).show();
                db.clear();
                break;
            case R.id.backspace:
                db.backSpace();
                break;
            case R.id.swap:
                swap();
                saveIndexes();
                Toast.makeText(context, "Swapped", Toast.LENGTH_LONG).show();
                break;
        }
        int decimals = db.getDecimalLength();
        String fS = "%." + decimals + "f";
        double val = db.getCurrency();

        firstCurrency.setText(String.format(fS, val));
        updateNums();
    }

    /**
     * Private class to swap the 2 currencies
     */
    private void swap(){
        String ttext = sp1.getSelectedItem().toString();
        String btext = sp2.getSelectedItem().toString();
        for (int i = 0; i< names.length; i++){
            if(names[i].equals(ttext)){ fCindex = i;}
            if(names[i].equals(btext)){ cCindex = i;}
        }
        sp1.setSelection(cCindex);
        sp2.setSelection(fCindex);
        saveIndexes();
    }

    /**
     * Helper method to update the currency
     */
    private void updateNums() {
        fC = sp1.getSelectedItem().toString();
        cC = sp2.getSelectedItem().toString();
        for (int i = 0; i < names.length; i++) {
            if (searchCurrency[i][0].equals(fC)) {
                this.fCindex = i;
            }
            if (searchCurrency[i][0].equals(cC)) {
                cCindex = i;
            }
        }
        int decimals = 2;
        String fS = "%." + decimals + "f";
        double val = ca.getRate(searchCurrency[fCindex][1], searchCurrency[cCindex][1], db.getCurrency());

        convertedCurrency.setText(String.format(fS, val));
//        convertedCurrency.setText(ca.getRate(searchCurrency[fCindex][1], searchCurrency[cCindex][1], db.getCurrency()).toString());
    }


    /**
     * save indexes saves the last user settings implemented
     */
    private void saveIndexes(){
        String ttext = sp1.getSelectedItem().toString();
        String btext = sp2.getSelectedItem().toString();
        for (int i = 0; i< names.length; i++){
            if(names[i].equals(ttext)){ fCindex = i;}
            if(names[i].equals(btext)){ cCindex = i;}
        }
        String storage = cCindex  + " " + fCindex;
        try {
            File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + SAVEFILE);
//            File file = new File(this.context.getExternalFilesDir(null), SAVEFILE);
            if (!file.exists()){
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            writer.append(storage);
            writer.flush();
            writer.close();
        }catch(IOException e){
            Log.e("Failed Saving:", " Loading Error Message : " + e.toString());
        }
    }

    /**
     * loads indexes the users used.
     */
    private void loadIndexes(){
        File save = new File(Environment.getExternalStorageDirectory().toString() + "/" + SAVEFILE);
        try{
            Scanner saveScanner = new Scanner(save);
            String[] nums = saveScanner.nextLine().split(" ");
            fCindex = Integer.parseInt(nums[0]);
            cCindex = Integer.parseInt(nums[1]);
            sp1.setSelection(cCindex);
            sp2.setSelection(fCindex);
        }catch(FileNotFoundException e){
            Log.e("Failed Loading:", " Loading Error Message : " + e.toString());
        }
    }

    /**
     * ASKS FOR USER PERMISSION TO WRITE DATA
     * VERY NECESSARY UNLESS USER GOES TO SETTINGS-> APPS-> TWDCADCurrency -> PERMISSION -> ALLOW STORAGE
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
