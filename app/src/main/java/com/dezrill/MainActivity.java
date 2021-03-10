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
import com.dezrill.support.Settings;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ListView valuesListView;
    private RadioButton radioButtonUAH, radioButtonUSD, radioButtonEUR, radioButtonRUB;
    private RadioGroup currenciesGroup;
    private String[] array;
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
        RenderListView();
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
            if (Float.parseFloat(adapter.getItem(i))<1) {
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
                array=getResources().getStringArray(R.array.UAH);
                adapter.clear();
                adapter.addAll(array);
                if (settings!=null && !settings.isUAHcoins()) RemoveBelowOne();
                adapter.notifyDataSetChanged();
            }break;
            case "USD":{
                array=getResources().getStringArray(R.array.USD);
                adapter.clear();
                adapter.addAll(array);
                if (settings!=null && !settings.isUSDcoins()) RemoveBelowOne();
                adapter.notifyDataSetChanged();
            }break;
            case "EUR":{
                array=getResources().getStringArray(R.array.EUR);adapter.clear();
                adapter.clear();
                adapter.addAll(array);
                if (settings!=null && !settings.isEURcoins()) RemoveBelowOne();
                adapter.notifyDataSetChanged();
            }break;
            case "RUB":{
                array=getResources().getStringArray(R.array.RUB);
                adapter.clear();
                adapter.addAll(array);
                if (settings!=null && !settings.isRUBcoins()) RemoveBelowOne();
                adapter.notifyDataSetChanged();
            }break;
        }
    }

    private void SetAdapter(){
        RadioButton temp=findViewById(currenciesGroup.getCheckedRadioButtonId());

        switch (temp.getText().toString()){
            case "UAH":array=getResources().getStringArray(R.array.UAH); break;
            case "USD":array=getResources().getStringArray(R.array.USD); break;
            case "EUR":array=getResources().getStringArray(R.array.EUR); break;
            case "RUB":array=getResources().getStringArray(R.array.RUB); break;
        }
        adapter=new CustomListViewAdapter(this, new ArrayList<String>(Arrays.asList(array)), temp.getText().toString());
        valuesListView.setAdapter(adapter);
    }

    private void setListViewOnClickListener(){
        valuesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RadioButton temp=findViewById(currenciesGroup.getCheckedRadioButtonId());

                Intent intent=new Intent(MainActivity.this, CalculatorActivity.class);
                intent.putExtra("currency", temp.getText().toString());
                intent.putExtra("value", Double.valueOf(adapter.getItem(position)));
                startActivity(intent);
                finish();
            }
        });
    }
}