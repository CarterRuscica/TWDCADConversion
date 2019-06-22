package com.example.twdcadconversion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<String>{

    Context context;
    String[] names;
    int[] images;

    /**
     * This .java is for the sole purpose of having a scrolling option bar with flags
     * Youtube tutorial helped in the creation of this
     *
     * @param context
     * @param names
     * @param images
     */

    public CustomAdapter(Context context, String[] names, int[] images){
        super(context, R.layout.spinner_item, names);
        this.context = context;
        this.names = names;
        this.images = images;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {


            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.spinner_item, null);
            TextView t1 = row.findViewById(R.id.textView);
            ImageView i1 = row.findViewById(R.id.imageView);

            t1.setText(names[position]);
            i1.setImageResource(images[position]);

        return row;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_item, null);
        TextView t1 = row.findViewById(R.id.textView);
        ImageView i1 = row.findViewById(R.id.imageView);

        t1.setText(names[position]);
        i1.setImageResource(images[position]);

        return row;
    }
}

