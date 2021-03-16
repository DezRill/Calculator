package com.dezrill;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dezrill.calculator.R;
import com.dezrill.support.CustomMainListViewAdapter;
import com.dezrill.support.HistoryItem;
import com.dezrill.support.ItemInList;
import com.dezrill.support.Settings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView valuesListView;
    private RadioButton radioButtonUAH, radioButtonUSD, radioButtonEUR, radioButtonRUB, activeRadioButton;
    private RadioGroup currenciesGroup;
    private String[] valuesArray;
    private ArrayList<ItemInList> items;
    private CustomMainListViewAdapter adapter;
    private Settings settings=new Settings();
    private TextView sumValueTextView;
    private ArrayList<HistoryItem> history_items;
    private Animation blink;
    private boolean operation;
    private double operationValue=0;
    private String currencyOperation;
    private Button recalculateButton;
    private static final int SECOND_ACTIVITY_REQUEST_CODE=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadSettings();
        setContentView(R.layout.activity_main);

        valuesListView=findViewById(R.id.valuesListView);
        radioButtonUAH=findViewById(R.id.UAHRadioButton);
        radioButtonUSD=findViewById(R.id.USDRadioButton);
        radioButtonEUR=findViewById(R.id.EURRadioButton);
        radioButtonRUB=findViewById(R.id.RUBRadioButton);
        currenciesGroup=findViewById(R.id.currenciesGroup);
        sumValueTextView=findViewById(R.id.sumValueTextView);
        recalculateButton=findViewById(R.id.recalculateButton);

        setRadioButtons();
        SetAdapter();
        RenderListView();
        setListViewOnClickListener();
    }

    public void onClickCheck(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        if (!sumValueTextView.getText().equals("0.00")) {
            currenciesGroup.check(activeRadioButton.getId());
            NotEmpty(findViewById(view.getId()));
        }
        else RenderListView();
    }

    public void onClickOpenSettings(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        Intent intent=new Intent(MainActivity.this, SettingsActivity.class);
        intent.putExtra("Settings", settings);
        startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
    }

    public void onClickOpenAbout(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        Intent intent=new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    public void onClickOpenHistory(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        Intent intent=new Intent(MainActivity.this, HistoryActivity.class);
        startActivity(intent);
    }

    public void onClickOpenCommenting(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
    }

    public void onClickClearAll(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);

        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.warning);
        builder.setMessage(R.string.clearInputsAlert);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                items.clear();
                RenderListView();
                if (operationValue!=0) {
                    String str=String.valueOf(operationValue);
                    if (str.substring(str.indexOf(".")).equals(".0")) str=String.format("%.0f", operationValue);
                    else str=String.format("%.2f", operationValue);
                    if (operation) recalculateButton.setText("×" + str + " = " + "0.00" + " " + currencyOperation);
                    else recalculateButton.setText("÷" + str + " = " + "0.00" + " " + currencyOperation);
                }
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    public void onClickSave(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);

        if (!sumValueTextView.getText().toString().equals("0.00")) {
            LoadHistory();

            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.warning);
            builder.setMessage(R.string.saveRecordAlert);
            builder.setCancelable(false);
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SaveHistory();
                    RenderListView();
                }
            });

            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }
    }

    public void onClickRecalculate(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        Intent intent=new Intent(MainActivity.this, RecalculateActivity.class);
        intent.putExtra("Currency", activeRadioButton.getText().toString());
        if (operationValue!=0) intent.putExtra("OperationValue", operationValue);
        startActivityForResult(intent,SECOND_ACTIVITY_REQUEST_CODE);
    }

    private void setListViewOnClickListener(){
        valuesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                view.startAnimation(blink);
                Intent intent=new Intent(MainActivity.this, CalculatorActivity.class);
                intent.putExtra("Items", items);
                intent.putExtra("Position", position);
                startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            finish();
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode==RESULT_OK) {
                if (data.getSerializableExtra("Settings")!=null) {
                    settings=(Settings) data.getSerializableExtra("Settings");
                    LoadLanguage(settings.getLanguage());
                    UpdateLanguage();
                    adapter.clear();
                    adapter.addAll(items);
                    RemoveAfterBackToActivity();
                    adapter.notifyDataSetChanged();
                }
                BackFromRecalculate(data);
                BackFromCalculate(data);
            }
        }
    }

    private void RemoveAfterBackToActivity() {
        switch (activeRadioButton.getText().toString()) {
            case "UAH": if (!settings.isUAHcoins()) RemoveBelowOne(); break;
            case "USD": if (!settings.isUSDcoins()) RemoveBelowOne(); break;
            case "EUR": if (!settings.isEURcoins()) RemoveBelowOne(); break;
            case "RUB": if (!settings.isRUBcoins()) RemoveBelowOne(); break;
        }
    }

    private void UpdateLanguage() {
        TextView appNameTextView=findViewById(R.id.appNameTextView);
        TextView sumTextView=findViewById(R.id.sumTextView);
        TextView denominationTextView=findViewById(R.id.denominationTextView);
        TextView countTextView=findViewById(R.id.countTextView);
        TextView sumOnRightTextView=findViewById(R.id.sumOnRightTextView);
        Button calculateButton=findViewById(R.id.recalculateButton);
        Button saveButton=findViewById(R.id.saveButton);

        appNameTextView.setText(R.string.title_main);
        sumTextView.setText(R.string.sum_static);
        denominationTextView.setText(R.string.denomination);
        countTextView.setText(R.string.count);
        sumOnRightTextView.setText(R.string.sum_static);
        calculateButton.setText(R.string.sum_static);
        saveButton.setText(R.string.Save);

    }

    private void LoadSettings()
    {
        try {
            FileInputStream fis=getApplicationContext().openFileInput("settings.dat");
            ObjectInputStream ois=new ObjectInputStream(fis);
            settings=(Settings)ois.readObject();
            ois.close();
            fis.close();

            LoadLanguage(settings.getLanguage());
        }
        catch (IOException | ClassNotFoundException e) {
        }
    }

    private void setRadioButtons() {
        if (settings.getDefaultValue()==R.id.UAHDefaultRadioButton) radioButtonUAH.setChecked(true);
        else if (settings.getDefaultValue()==R.id.USDDefaultRadioButton) radioButtonUSD.setChecked(true);
        else if (settings.getDefaultValue()==R.id.EURDefaultRadioButton) radioButtonEUR.setChecked(true);
        else if (settings.getDefaultValue()==R.id.RUBDefaultRadioButton) radioButtonRUB.setChecked(true);
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
        activeRadioButton=findViewById(currenciesGroup.getCheckedRadioButtonId());

        switch (activeRadioButton.getText().toString())
        {
            case "UAH":{
                valuesArray=getResources().getStringArray(R.array.UAH);
                UpdateItemsArray("UAH");
                adapter.clear();
                adapter.addAll(items);
                if (settings!=null && !settings.isUAHcoins()) RemoveBelowOne();
                adapter.notifyDataSetChanged();
                sumValueTextView.setText("0.00");
                sumValueTextView.setTextSize(50);
            }break;
            case "USD":{
                valuesArray=getResources().getStringArray(R.array.USD);
                UpdateItemsArray("USD");
                adapter.clear();
                adapter.addAll(items);
                if (settings!=null && !settings.isUSDcoins()) RemoveBelowOne();
                adapter.notifyDataSetChanged();
                sumValueTextView.setText("0.00");
                sumValueTextView.setTextSize(50);
            }break;
            case "EUR":{
                valuesArray=getResources().getStringArray(R.array.EUR);
                UpdateItemsArray("EUR");
                adapter.clear();
                adapter.addAll(items);
                if (settings!=null && !settings.isEURcoins()) RemoveBelowOne();
                adapter.notifyDataSetChanged();
                sumValueTextView.setText("0.00");
                sumValueTextView.setTextSize(50);
            }break;
            case "RUB":{
                valuesArray=getResources().getStringArray(R.array.RUB);
                UpdateItemsArray("RUB");
                adapter.clear();
                adapter.addAll(items);
                if (settings!=null && !settings.isRUBcoins()) RemoveBelowOne();
                adapter.notifyDataSetChanged();
                sumValueTextView.setText("0.00");
                sumValueTextView.setTextSize(50);
            }break;
        }
    }

    private void SetAdapter(){
        CreateItemsArray();
        adapter=new CustomMainListViewAdapter(this, items);
        if (settings!=null && !settings.isRUBcoins()) RemoveBelowOne();
        valuesListView.setAdapter(adapter);
    }

    private void CreateItemsArray() {
        activeRadioButton=findViewById(currenciesGroup.getCheckedRadioButtonId());

        switch (activeRadioButton.getText().toString()) {
            case "UAH":valuesArray=getResources().getStringArray(R.array.UAH); break;
            case "USD":valuesArray=getResources().getStringArray(R.array.USD); break;
            case "EUR":valuesArray=getResources().getStringArray(R.array.EUR); break;
            case "RUB":valuesArray=getResources().getStringArray(R.array.RUB); break;
        }
        UpdateItemsArray(activeRadioButton.getText().toString());
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

    private void SaveHistory() {
        HistoryItem item=new HistoryItem();
        item.setDate(getCurrentDate());
        item.setTime(getCurrentTime());
        item.setCurrency(items.get(0).getCurrency());
        item.setSum(sumValueTextView.getText().toString());
        item.setValues(getAllValues());
        history_items.add(item);

        try {
            FileOutputStream fos=getApplicationContext().openFileOutput("history.dat", Context.MODE_PRIVATE);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(history_items);
            oos.close();
            fos.close();
        }
        catch (IOException e) {
        }
    }

    private String getCurrentDate() {
        DateFormat dateFormat=new SimpleDateFormat("dd.MM.y");
        Date date=new Date();
        return dateFormat.format(date);
    }

    private String getCurrentTime() {
        DateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
        Date date=new Date();
        return dateFormat.format(date);
    }

    private Map<String, String> getAllValues() {
        Map<String, String> map=new HashMap<>();
        for (ItemInList item:items) {
            if (!item.getCount().equals("0")) {
                map.put(item.getDenomination(), item.getCount());
            }
        }
        return map;
    }

    private void LoadHistory() {
        try {
            FileInputStream fis=getApplicationContext().openFileInput("history.dat");
            ObjectInputStream ois=new ObjectInputStream(fis);
            history_items=(ArrayList<HistoryItem>) ois.readObject();
            ois.close();
            fis.close();
        }
        catch (IOException | ClassNotFoundException e) {
            history_items=new ArrayList<>();
        }
    }

    private void BackFromCalculate(Intent data) {
        if (data.getSerializableExtra("Items")!=null) {
            items=(ArrayList<ItemInList>) data.getSerializableExtra("Items");
            adapter.clear();
            adapter.addAll(items);
            RemoveAfterBackToActivity();
            adapter.notifyDataSetChanged();

            double result=0;
            for(ItemInList item:items) {
                if (item.getSum()!="0.00") {
                    result+=Double.parseDouble(item.getSum());
                }
            }
            String str=String.format(Locale.ROOT,"%.2f", result);
            if (str.length()>10){
                if (getWindowManager().getDefaultDisplay().getWidth()<=720) sumValueTextView.setTextSize(30);
                else sumValueTextView.setTextSize(40);
            }
            sumValueTextView.setText(str);
        }

        if (operationValue!=0) {
            String str=String.valueOf(operationValue);
            if (str.substring(str.indexOf(".")).equals(".0")) str=String.format("%.0f", operationValue);
            else str=String.format("%.2f", operationValue);
            if (operation) recalculateButton.setText("×" + str + " = " + String.format("%.2f",Double.parseDouble(sumValueTextView.getText().toString())*operationValue) + " " + currencyOperation);
            else recalculateButton.setText("÷" + str + " = " + String.format("%.2f",Double.parseDouble(sumValueTextView.getText().toString())/operationValue) + " " + currencyOperation);
            if (recalculateButton.getText().toString().length()>=18) {
                ViewGroup.LayoutParams lp=recalculateButton.getLayoutParams();
                lp.width= ViewGroup.LayoutParams.WRAP_CONTENT;
                recalculateButton.setLayoutParams(lp);
            }
            else {
                ViewGroup.LayoutParams lp=recalculateButton.getLayoutParams();
                lp.width=415;
                recalculateButton.setLayoutParams(lp);
            }
        }
    }

    private void BackFromRecalculate(Intent data) {
        if (data.getDoubleExtra("Result", 0)!=0) {
            operationValue=data.getDoubleExtra("Result",0);
            operation=data.getBooleanExtra("Operation", true);
            currencyOperation=data.getStringExtra("Currency");
        }
    }

    private void LoadLanguage(String lang)
    {
        String languageToLoad=lang;
        Locale locale=new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config=new Configuration();
        config.locale=locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    private void NotEmpty(RadioButton button) {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.warning);
        builder.setMessage(R.string.changeCurrencyAlert);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currenciesGroup.check(button.getId());
                items.clear();
                RenderListView();
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}