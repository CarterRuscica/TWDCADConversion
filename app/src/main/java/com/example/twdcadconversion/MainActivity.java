package com.example.twdcadconversion;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity{


    /**
     * These variables are used for the Spinner and also converting spinner information into a readable currency
     */
    CustomAdapter adapter;
    Spinner sp1, sp2;
    //Probably could've just made an array of the currencies that match along the curreny ones by doing index
    String[][] searchCurrency = {{"Australian dollar", "AUR"}, {"Brazilian real","BRL"},{"Chinese renminbi", "CNY"}, {"European Euro","EUR"}, {"Hong Kong dollar", "HKD"}
            , {"Indian rupee","IDR"}, {"Indonesian rupiah","IDR"}, {"Japanese ye","JPY"} , {"Malaysian ringgit","MYR"}, {"New Zealand dollar", "NZD"}
            , {"Norwegian krone","NOK"} , {"Peruvian new sol","PEN"} , {"Saudi riyal","SAR"} , {"Singapore dollar","SGD"} , {"South African rand", "ZAR"}
            , {"South Korean won","KRW"} , {"Swedish krona","SEK"} , {"Swiss franc","SEK"} , {"Taiwanese dollar","TWD"} , {"Thai baht","TBH"} , {"Turkish lira","TRY"}
            , {"UK Pound","GBP"} , {"US Dollar","USD"} , {"Vietnamese dong","VND"}};
    String[] names = {"Australian dollar", "Brazilian real", "Chinese renminbi", "European Euro", "Hong Kong dollar"
            , "Indian rupee", "Indonesian rupiah", "Japanese ye", "Malaysian ringgit", "New Zealand dollar"
            , "Norwegian krone", "Peruvian new sol", "Saudi riyal", "Singapore dollar", "South African rand"
            , "South Korean won", "Swedish krona", "Swiss franc", "Taiwanese dollar", "Thai baht", "Turkish lira"
            , "UK Pound", "US Dollar", "Vietnamese dong"};
    int[] images = {R.drawable.au, R.drawable.br, R.drawable.cn, R.drawable.be, R.drawable.hk
            ,R.drawable.in, R.drawable.id, R.drawable.jp,R.drawable.my, R.drawable.nz
            ,R.drawable.no, R.drawable.pe, R.drawable.sa, R.drawable.sg, R.drawable.za
            ,R.drawable.kr, R.drawable.se, R.drawable.li, R.drawable.tw, R.drawable.th, R.drawable.tr
            , R.drawable.gb, R.drawable.us, R.drawable.vn};

    /**
     * REMEMBER TO ADD CANADIAN FLAG
     */
    CurrencyArray ca = new CurrencyArray();
    DoubleBuilder db = new DoubleBuilder(10, 2);

    TextView firstCurrency, convertedCurrency;
    String fC, cC;


    private static final String FILENAME = "memCurr.txt";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * Initializing helper objects
         * ca being the main currency converting app
         * db being the number handler object
         */


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp1 = (Spinner)findViewById(R.id.spinner);
        adapter = new CustomAdapter(this, names, images);

        firstCurrency = (TextView)findViewById(R.id.FirstCurrency);
        firstCurrency.setText(db.getInt().toString());
        convertedCurrency = (TextView)findViewById(R.id.SecondCurrency);

        convertedCurrency.setText(ca.getRate())

        sp1.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition("Chinese reminbi");
        sp1.setSelection(3);
        Log.d("System Debugging: " , "SPINNER POSITION: " +  spinnerPosition + " Is my position");
        String text = sp1.getSelectedItem().toString();
        Log.d("System Debugging: ", "SPINNER POSITION BY STRING " + text);




        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), names[position], Toast.LENGTH_LONG).show();
                Log.d("System Spinner", sp1.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp2 = (Spinner)findViewById(R.id.spinner2);
        adapter = new CustomAdapter(this, names, images);

        sp2.setAdapter(adapter);

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), names[position], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

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
                db.clear();
                break;
        }
        firstCurrency.setText(db.getInt().toString());
    }


    private boolean isOnline(Context c){
        ConnectivityManager connMgr = (ConnectivityManager) c.getSystemService(c.CONNECTIVITY_SERVICE);
        NetworkInfo netWorkInfo = connMgr.getActiveNetworkInfo();
        if (netWorkInfo != null && netWorkInfo.isConnected())
            return true;
        else
            return false;
    }

    /**
     * Update data updates currencies
     */
    private void updateData(){

    }


    /**
     * save indexes saves the last user settings implemented
     */
    private static void saveIndexes(){

    }

    /**
     * loads indexes the users used.
     */
    private static void loadIndexes(){

    }

}
