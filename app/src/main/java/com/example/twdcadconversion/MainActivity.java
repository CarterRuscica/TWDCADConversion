package com.example.twdcadconversion;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity{
    private boolean cur0;
    Switch toggle;
    TextView output, currency0, currency1, currType0, currType1;
    Context context;
    double rate;
    String currTi0 = "", currTi1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        Button button = (Button)findViewById(R.id.button2);
        toggle = (Switch)findViewById(R.id.switch1);
        cur0 = toggle.isChecked();
        output = (TextView)findViewById(R.id.textView3);

        currency0 = (TextView)findViewById(R.id.editText3); // TWD
        currType0 = (TextView)findViewById(R.id.editText2); // TWD
        currTi0 = currType0.getText().toString();

        currency1 = (TextView)findViewById(R.id.editText4); // CAD
        currType1 = (TextView)findViewById(R.id.editText); // CAD
        currTi1 = currType1.getText().toString();

        toggle.setText("Convert " + currTi0);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggle.setText("Convert " + currTi0);
            }
        });





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataGetter dg = new dataGetter();
                FileHelper fh = new FileHelper();

                currTi0 = currType0.getText().toString();
                toggle.setText("Convert " + currTi0);
                currTi1 = currType1.getText().toString();

                String input = fh.readFromFile(context);
                try{
                    if(dg.isOnline(context))
                        input = dg.getData(context, currTi0, currTi1);
                }catch(IOException e){
//                    input = fh.readFromFile(context);
                }
                rate = Double.parseDouble(input);
                convert();
            }
        });


    }

    private void convert(){


        double cash;
        if (toggle.isChecked()){
            if (currency1.getText().toString().length() >0)
                cash = new Double(currency1.getText().toString()) * rate;
            else
                cash = 0;// convert to Taiwan dollars

            output.setText(currency1.getText().toString() + " " + currTi0 +
                    "\nis\n" + String.format("%.2f", cash) + " " + currTi1);
            currency0.setText(Double.toString(round(cash,2)));

        }else{
            if (currency0.getText().toString().length() >0)
                cash = new Double(currency0.getText().toString()) / rate;
            else
                cash = 0;
            output.setText(currency0.getText() + " " + currTi1 +
                    "\nis\n" + String.format("%.2f", cash) + " " + currTi0);
            currency1.setText(Double.toString(round(cash, 2)));
        }
    }

    private static double round(double value, int place) {
        if (place < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, place);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
