package com.dezrill.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String[] array;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=findViewById(R.id.listView);
        radioGroup=findViewById(R.id.radioGroup);
        radioButton=findViewById(R.id.radioButton);
        radioButton.setChecked(true);

        array=getResources().getStringArray(R.array.UAH);
        adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>(Arrays.asList(array)));
        listView.setAdapter(adapter);
    }

    public void onClickCheck(View view) {
        radioButton = findViewById(radioGroup.getCheckedRadioButtonId());

        switch (radioButton.getText().toString())
        {
            case "UAH":{
                array=getResources().getStringArray(R.array.UAH);
                adapter.clear();
                adapter.addAll(array);
                adapter.notifyDataSetChanged();
            }break;
            case "USD":{
                array=getResources().getStringArray(R.array.USD);
                adapter.clear();
                adapter.addAll(array);
                adapter.notifyDataSetChanged();
            }break;
            case "EUR":{
                array=getResources().getStringArray(R.array.EUR);adapter.clear();
                adapter.clear();
                adapter.addAll(array);
                adapter.notifyDataSetChanged();
            }break;
            case "RUB":{
                array=getResources().getStringArray(R.array.RUB);
                adapter.clear();
                adapter.addAll(array);
                adapter.notifyDataSetChanged();
            }break;
        }
    }
}