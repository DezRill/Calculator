package com.dezrill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dezrill.calculator.R;
import com.dezrill.support.CustomListViewAdapter;
import com.dezrill.support.ItemInList;
import com.dezrill.support.Settings;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView valuesListView;
    private RadioButton radioButtonUAH, radioButtonUSD, radioButtonEUR, radioButtonRUB;
    private RadioGroup currenciesGroup;
    private String[] valuesArray;
    private ArrayList<ItemInList> items;
    private CustomListViewAdapter adapter;
    private Settings settings=new Settings();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valuesListView=findViewById(R.id.valuesListView);
        radioButtonUAH=findViewById(R.id.UAHRadioButton);
        radioButtonUSD=findViewById(R.id.USDRadioButton);
        radioButtonEUR=findViewById(R.id.EURRadioButton);
        radioButtonRUB=findViewById(R.id.RUBRadioButton);
        currenciesGroup=findViewById(R.id.currenciesGroup);

        LoadSettings();
        SetAdapter();
        setListViewOnClickListener();
    }

    public void onClickCheck(View view) {
        RenderListView();
    }

    public void onClickOpenSettings(View view) {
        Intent intent=new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickOpenAbout(View view) {
        Intent intent=new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            finish();
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void LoadSettings()
    {
        try {
            FileInputStream fis=getApplicationContext().openFileInput("settings.dat");
            ObjectInputStream ois=new ObjectInputStream(fis);
            settings=(Settings)ois.readObject();
            ois.close();
            fis.close();

            if (settings.getDefaultValue()==R.id.UAHDefaultRadioButton) radioButtonUAH.setChecked(true);
            else if (settings.getDefaultValue()==R.id.USDDefaultRadioButton) radioButtonUSD.setChecked(true);
            else if (settings.getDefaultValue()==R.id.EURDefaultRadioButton) radioButtonEUR.setChecked(true);
            else if (settings.getDefaultValue()==R.id.RUBDefaultRadioButton) radioButtonRUB.setChecked(true);
        }
        catch (IOException | ClassNotFoundException e) {
        }
    }

    private void RemoveBelowOne()
    {
        for (int i=0;i<adapter.getCount();i++)
        {
            if (Double.parseDouble(adapter.getItem(i).getDenomination())<1) {
                adapter.remove(adapter.getItem(i));
                i--;
            }
        }
    }

    private void RenderListView()
    {
        RadioButton temp=findViewById(currenciesGroup.getCheckedRadioButtonId());

        switch (temp.getText().toString())
        {
            case "UAH":{
                valuesArray=getResources().getStringArray(R.array.UAH);
                UpdateItemsArray("UAH");
                adapter.clear();
                adapter.addAll(items);
                if (settings!=null && !settings.isUAHcoins()) RemoveBelowOne();
                adapter.notifyDataSetChanged();
            }break;
            case "USD":{
                valuesArray=getResources().getStringArray(R.array.USD);
                UpdateItemsArray("USD");
                adapter.clear();
                adapter.addAll(items);
                if (settings!=null && !settings.isUSDcoins()) RemoveBelowOne();
                adapter.notifyDataSetChanged();
            }break;
            case "EUR":{
                valuesArray=getResources().getStringArray(R.array.EUR);
                UpdateItemsArray("EUR");
                adapter.clear();
                adapter.addAll(items);
                if (settings!=null && !settings.isEURcoins()) RemoveBelowOne();
                adapter.notifyDataSetChanged();
            }break;
            case "RUB":{
                valuesArray=getResources().getStringArray(R.array.RUB);
                UpdateItemsArray("RUB");
                adapter.clear();
                adapter.addAll(items);
                if (settings!=null && !settings.isRUBcoins()) RemoveBelowOne();
                adapter.notifyDataSetChanged();
            }break;
        }
    }

    private void SetAdapter(){
        CreateItemsArray();
        adapter=new CustomListViewAdapter(this, items);
        if (settings!=null && !settings.isRUBcoins()) RemoveBelowOne();
        valuesListView.setAdapter(adapter);
    }

    private void setListViewOnClickListener(){
        valuesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RadioButton temp=findViewById(currenciesGroup.getCheckedRadioButtonId());

                Intent intent=new Intent(MainActivity.this, CalculatorActivity.class);
                intent.putExtra("currency", temp.getText().toString());
                intent.putExtra("value", Double.valueOf(adapter.getItem(position).getDenomination()));
                startActivity(intent);
                finish();
            }
        });
    }

    private void CreateItemsArray() {
        RadioButton temp=findViewById(currenciesGroup.getCheckedRadioButtonId());

        switch (temp.getText().toString()) {
            case "UAH":valuesArray=getResources().getStringArray(R.array.UAH); break;
            case "USD":valuesArray=getResources().getStringArray(R.array.USD); break;
            case "EUR":valuesArray=getResources().getStringArray(R.array.EUR); break;
            case "RUB":valuesArray=getResources().getStringArray(R.array.RUB); break;
        }
        items=new ArrayList<>();
        UpdateItemsArray(temp.getText().toString());
    }

    private void UpdateItemsArray(String currency) {
        items=new ArrayList<>();
        for (String value: valuesArray){
            ItemInList item=new ItemInList();
            item.setCurrency(currency);
            item.setDenomination(value);
            item.setCount("0");
            item.setSum("0.00");
            items.add(item);
        }
    }
}