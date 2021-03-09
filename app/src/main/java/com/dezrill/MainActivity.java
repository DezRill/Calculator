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
import android.widget.Toast;

import com.dezrill.calculator.R;
import com.dezrill.support.MyAdapter;
import com.dezrill.support.Settings;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private RadioButton radioButton, radioButton2, radioButton3, radioButton4;
    private RadioGroup radioGroup;
    private String[] array;
    private MyAdapter adapter;
    private Settings settings=new Settings();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=findViewById(R.id.listView);
        radioButton=findViewById(R.id.radioButton);
        radioButton2=findViewById(R.id.radioButton2);
        radioButton3=findViewById(R.id.radioButton3);
        radioButton4=findViewById(R.id.radioButton4);
        radioGroup=findViewById(R.id.radioGroup);

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

            if (settings.getDefaultValue()==R.id.radioButton5) radioButton.setChecked(true);
            else if (settings.getDefaultValue()==R.id.radioButton6) radioButton2.setChecked(true);
            else if (settings.getDefaultValue()==R.id.radioButton7) radioButton3.setChecked(true);
            else if (settings.getDefaultValue()==R.id.radioButton8) radioButton4.setChecked(true);
        }
        catch (IOException | ClassNotFoundException e) {
            Toast toast=Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG);
            toast.show();
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
        RadioButton temp=findViewById(radioGroup.getCheckedRadioButtonId());

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
        RadioButton temp=findViewById(radioGroup.getCheckedRadioButtonId());

        switch (temp.getText().toString()){
            case "UAH":array=getResources().getStringArray(R.array.UAH); break;
            case "USD":array=getResources().getStringArray(R.array.USD); break;
            case "EUR":array=getResources().getStringArray(R.array.EUR); break;
            case "RUB":array=getResources().getStringArray(R.array.RUB); break;
        }
        adapter=new MyAdapter(this, new ArrayList<String>(Arrays.asList(array)), temp.getText().toString());
        listView.setAdapter(adapter);
    }

    private void setListViewOnClickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RadioButton temp=findViewById(radioGroup.getCheckedRadioButtonId());

                Intent intent=new Intent(MainActivity.this, CalculatorActivity.class);
                intent.putExtra("currency", temp.getText().toString());
                intent.putExtra("value", Double.valueOf(adapter.getItem(position)));
                startActivity(intent);
                finish();
            }
        });
    }
}