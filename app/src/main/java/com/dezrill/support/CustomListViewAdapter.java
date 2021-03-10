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

public class CustomListViewAdapter extends ArrayAdapter<String> {
    Context context;
    String currency;
    //double sum;
    //int count;
    ArrayList<String> denomination;

    public CustomListViewAdapter (Context context, ArrayList<String> denomination, String currency){
        super(context, R.layout.row, R.id.denominationRowTextView, denomination);
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
        TextView currencyTextView=row.findViewById(R.id.currencyRowTextView);
        TextView denominationTextView=row.findViewById(R.id.denominationRowTextView);
        TextView countTextView=row.findViewById(R.id.countRowTextView);
        TextView sumTextView=row.findViewById(R.id.sumRowTextView);

        currencyTextView.setText(currency);
        denominationTextView.setText(denomination.get(position));
        //res.setText(String.valueOf(result[position]));
        //cou.setText(String.valueOf(count[position]));

        return row;
    }
}
