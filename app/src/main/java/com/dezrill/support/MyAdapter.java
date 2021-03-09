package com.dezrill.support;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dezrill.calculator.R;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<String> {
    Context context;
    String currency;
    //double result;
    //int count;
    ArrayList<String> denomination;

    public MyAdapter (Context context, ArrayList<String> denomination, String currency){
        super(context, R.layout.row, R.id.textView19, denomination);
        this.context=context;
        this.currency=currency;
        this.denomination=denomination;
        //this.result=result;
        //this.count=count;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=layoutInflater.inflate(R.layout.row,parent,false);
        TextView curr=row.findViewById(R.id.textView17);
        TextView denom=row.findViewById(R.id.textView19);
        TextView res=row.findViewById(R.id.textView21);
        TextView cou=row.findViewById(R.id.textView22);

        curr.setText(currency);
        denom.setText(denomination.get(position));
        //res.setText(String.valueOf(result[position]));
        //cou.setText(String.valueOf(count[position]));

        return row;
    }
}
