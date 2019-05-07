package com.example.twdcadconversion;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.io.IOException;

public class MainActivity extends AppCompatActivity{
    private boolean twd;
    Switch toggle;
    TextView output, taiwan, canada;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        Button button = (Button)findViewById(R.id.button2);
        toggle = (Switch)findViewById(R.id.switch1);
        twd = toggle.isChecked();
        output = (TextView)findViewById(R.id.textView3);
        taiwan = (TextView)findViewById(R.id.editText4);
        canada = (TextView)findViewById(R.id.editText3);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (toggle.isChecked())
                    output.setText("to CAD");
                else
                    output.setText("to TWD");
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                convert();
                dataGetter dg = new dataGetter();
                FileHelper fh = new FileHelper();

                String input;
                try {
                    input = dg.getData(context);
                }catch(IOException e){
                    input = fh.readFromFile(context);
                }

                fh.writeToFile(input, context);
                output.setText(fh.readFromFile(context));
            }
        });


    }



    private void convert(){
        double cash;
        if (toggle.isChecked()){
            if (canada.getText().toString().length() >0)
                cash = new Double(canada.getText().toString());
            else
                cash = 0;// convert to Taiwan dollars




//            output.setText("Converting to CAD " + cash);
        }else{
            if (taiwan.getText().toString().length() >0)
                cash = new Double(taiwan.getText().toString());
            else
                cash = 0;




//            output.setText("Converting to TWD " + cash);
        }
    }

    private void textChange(){

    }
}
