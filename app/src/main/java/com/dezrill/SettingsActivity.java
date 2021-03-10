package com.dezrill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.dezrill.calculator.R;
import com.dezrill.support.Settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SettingsActivity extends AppCompatActivity {
    private Settings settings=new Settings();
    private RadioButton radioButton5, radioButton6, radioButton7, radioButton8;
    private Switch switch1, switch2, switch3, switch4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        radioButton5=findViewById(R.id.radioButton5);
        radioButton6=findViewById(R.id.radioButton6);
        radioButton7=findViewById(R.id.radioButton7);
        radioButton8=findViewById(R.id.radioButton8);
        switch1=findViewById(R.id.switch1);
        switch2=findViewById(R.id.switch2);
        switch3=findViewById(R.id.switch3);
        switch4=findViewById(R.id.switch4);

        LoadSettings();
    }

    public void onClickCheсked(View view) {
        if(radioButton5.isPressed()){
            radioButton6.setChecked(false);
            radioButton7.setChecked(false);
            radioButton8.setChecked(false);
        }
        if(radioButton6.isPressed()){
            radioButton5.setChecked(false);
            radioButton7.setChecked(false);
            radioButton8.setChecked(false);
        }
        if(radioButton7.isPressed()){
            radioButton5.setChecked(false);
            radioButton6.setChecked(false);
            radioButton8.setChecked(false);
        }
        if(radioButton8.isPressed()){
            radioButton5.setChecked(false);
            radioButton6.setChecked(false);
            radioButton7.setChecked(false);
        }
    }

    public void onClickBackToMain(View view) {
        setSettingsAndBack();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) setSettingsAndBack();
        return super.onKeyDown(keyCode, event);
    }

    private void setSettingsAndBack(){
        if (radioButton5.isChecked()) settings.setDefaultValue(radioButton5.getId());
        else if (radioButton6.isChecked()) settings.setDefaultValue(radioButton6.getId());
        else if (radioButton7.isChecked()) settings.setDefaultValue(radioButton7.getId());
        else if (radioButton8.isChecked()) settings.setDefaultValue(radioButton8.getId());
        settings.setUAHcoins(switch1.isChecked());
        settings.setUSDcoins(switch2.isChecked());
        settings.setEURcoins(switch3.isChecked());
        settings.setRUBcoins(switch4.isChecked());
        SaveSettings();

        Intent intent=new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void LoadSettings(){
        try {
            FileInputStream fis=getApplicationContext().openFileInput("settings.dat");
            ObjectInputStream ois=new ObjectInputStream(fis);
            settings=(Settings) ois.readObject();
            ois.close();
            fis.close();

            switch1.setChecked(settings.isUAHcoins());
            switch2.setChecked(settings.isUSDcoins());
            switch3.setChecked(settings.isEURcoins());
            switch4.setChecked(settings.isRUBcoins());
            radioButton5.setChecked(false);
            if (radioButton5.getId()==settings.getDefaultValue()) radioButton5.setChecked(true);
            else if (radioButton6.getId()==settings.getDefaultValue()) radioButton6.setChecked(true);
            else if (radioButton7.getId()==settings.getDefaultValue()) radioButton7.setChecked(true);
            else if (radioButton8.getId()==settings.getDefaultValue()) radioButton8.setChecked(true);
        }
        catch (IOException | ClassNotFoundException e) {
        }
    }

    private void SaveSettings()
    {
        try {
            FileOutputStream fos=getApplicationContext().openFileOutput("settings.dat", Context.MODE_PRIVATE);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(settings);
            oos.close();
            fos.close();
        }
        catch (IOException e) {
        }
    }
}
