package com.dezrill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dezrill.calculator.R;
import com.dezrill.support.CustomListViewAdapter;
import com.dezrill.support.ItemInList;
import com.dezrill.support.Settings;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ListView valuesListView;
    private RadioButton radioButtonUAH, radioButtonUSD, radioButtonEUR, radioButtonRUB;
    private RadioGroup currenciesGroup;
    private String[] valuesArray;
    private ArrayList<ItemInList> items;
    private CustomListViewAdapter adapter;
    private Settings settings=new Settings();
    private TextView sumValueTextView;
    private Animation blink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        blink=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        valuesListView=findViewById(R.id.valuesListView);
        radioButtonUAH=findViewById(R.id.UAHRadioButton);
        radioButtonUSD=findViewById(R.id.USDRadioButton);
        radioButtonEUR=findViewById(R.id.EURRadioButton);
        radioButtonRUB=findViewById(R.id.RUBRadioButton);
        currenciesGroup=findViewById(R.id.currenciesGroup);
        sumValueTextView=findViewById(R.id.sumValueTextView);

        LoadSettings();
        SetAdapter();
        setListViewOnClickListener();
        BackFromCalculate();
    }

    public void onClickCheck(View view) {
        view.startAnimation(blink);
        RenderListView();
    }

    public void onClickOpenSettings(View view) {
        view.startAnimation(blink);
        Intent intent=new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickOpenAbout(View view) {
        view.startAnimation(blink);
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
                sumValueTextView.setText("0.00");
            }break;
            case "USD":{
                valuesArray=getResources().getStringArray(R.array.USD);
                UpdateItemsArray("USD");
                adapter.clear();
                adapter.addAll(items);
                if (settings!=null && !settings.isUSDcoins()) RemoveBelowOne();
                adapter.notifyDataSetChanged();
                sumValueTextView.setText("0.00");
            }break;
            case "EUR":{
                valuesArray=getResources().getStringArray(R.array.EUR);
                UpdateItemsArray("EUR");
                adapter.clear();
                adapter.addAll(items);
                if (settings!=null && !settings.isEURcoins()) RemoveBelowOne();
                adapter.notifyDataSetChanged();
                sumValueTextView.setText("0.00");
            }break;
            case "RUB":{
                valuesArray=getResources().getStringArray(R.array.RUB);
                UpdateItemsArray("RUB");
                adapter.clear();
                adapter.addAll(items);
                if (settings!=null && !settings.isRUBcoins()) RemoveBelowOne();
                adapter.notifyDataSetChanged();
                sumValueTextView.setText("0.00");
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
                view.startAnimation(blink);
                Intent intent=new Intent(MainActivity.this, CalculatorActivity.class);
                intent.putExtra("Items", items);
                intent.putExtra("Position", position);
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

    private void BackFromCalculate() {
        Intent intent=getIntent();
        if (intent.getSerializableExtra("Items")!=null) {
            items=(ArrayList<ItemInList>) intent.getSerializableExtra("Items");
            adapter.clear();
            adapter.addAll(items);
            if (settings!=null && !settings.isRUBcoins()) RemoveBelowOne();
            adapter.notifyDataSetChanged();

            double result=0;
            for(ItemInList item:items) {
                if (item.getSum()!="0.00") {
                    result+=Double.parseDouble(item.getSum());
                }
            }
            String str=String.format(Locale.ROOT,"%.2f", result);
            if (str.length()>10) sumValueTextView.setTextSize(40);
            sumValueTextView.setText(str);
        }
    }

    public void onClickOpenHistory(View view) {
        view.startAnimation(blink);
    }

    public void onClickOpenCommenting(View view) {
        view.startAnimation(blink);
    }

    public void onClickClearAll(View view) {
        view.startAnimation(blink);
    }

    public void onClickSave(View view) {
        view.startAnimation(blink);
    }

    public void onClickRecalculate(View view) {
        view.startAnimation(blink);
    }
}