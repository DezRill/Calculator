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

public class CustomHistoryListViewAdapter extends ArrayAdapter<HistoryItem> {
    private Context context;
    private ArrayList<HistoryItem> items;

    public CustomHistoryListViewAdapter(Context context, ArrayList<HistoryItem> items){
        super(context, R.layout.history_row, R.id.valueHistoryTextView, items);
        this.context=context;
        this.items=items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=layoutInflater.inflate(R.layout.history_row,parent,false);
        TextView dateTextView=row.findViewById(R.id.dateTextView);
        TextView timeTextView=row.findViewById(R.id.timeTextView);
        TextView currencyHistoryTextView=row.findViewById(R.id.currencyHistoryTextView);
        TextView valueHistoryTextView=row.findViewById(R.id.valueHistoryTextView);

        dateTextView.setText(items.get(position).getDate());
        timeTextView.setText(items.get(position).getTime());
        currencyHistoryTextView.setText(items.get(position).getCurrency());
        valueHistoryTextView.setText(items.get(position).getSum());

        return row;
    }
}
