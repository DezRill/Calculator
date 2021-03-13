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

public class CustomMainListViewAdapter extends ArrayAdapter<ItemInList> {
    private Context context;
    private ArrayList<ItemInList> items;

    public CustomMainListViewAdapter(Context context, ArrayList<ItemInList> items){
        super(context, R.layout.main_row, R.id.denominationRowTextView, items);
        this.context=context;
        this.items=items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=layoutInflater.inflate(R.layout.main_row,parent,false);
        TextView currencyTextView=row.findViewById(R.id.currencyRowTextView);
        TextView denominationTextView=row.findViewById(R.id.denominationRowTextView);
        TextView countTextView=row.findViewById(R.id.countRowTextView);
        TextView sumTextView=row.findViewById(R.id.sumRowTextView);

        currencyTextView.setText(items.get(position).getCurrency());
        denominationTextView.setText(items.get(position).getDenomination());
        countTextView.setText(items.get(position).getCount());
        sumTextView.setText(items.get(position).getSum());

        return row;
    }
}
