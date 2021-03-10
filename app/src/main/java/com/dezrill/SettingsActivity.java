package com.dezrill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dezrill.calculator.R;
import com.dezrill.support.Settings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SettingsActivity extends AppCompatActivity {
    private Settings settings=new Settings();
    private RadioButton UAHDefaultRadioButton, USDDefaultRadioButton, EURDefaultRadioButton, RUBDefaultRadioButton;
    private Switch UAHCoinsSwitch, USDCoinsSwitch, EURCoinsSwitch, RUBCoinsSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        UAHDefaultRadioButton=findViewById(R.id.UAHDefaultRadioButton);
        USDDefaultRadioButton=findViewById(R.id.USDDefaultRadioButton);
        EURDefaultRadioButton=findViewById(R.id.EURDefaultRadioButton);
        RUBDefaultRadioButton=findViewById(R.id.RUBDefaultRadioButton);
        UAHCoinsSwitch=findViewById(R.id.UAHCoinsSwitch);
        USDCoinsSwitch=findViewById(R.id.USDCoinsSwitch);
        EURCoinsSwitch=findViewById(R.id.EURCoinsSwitch);
        RUBCoinsSwitch=findViewById(R.id.RUBCoinsSwitch);

        LoadSettings();
    }

    public void onClickCheсked(View view) {
        if(UAHDefaultRadioButton.isPressed()){
            USDDefaultRadioButton.setChecked(false);
            EURDefaultRadioButton.setChecked(false);
            RUBDefaultRadioButton.setChecked(false);
        }
        if(USDDefaultRadioButton.isPressed()){
            UAHDefaultRadioButton.setChecked(false);
            EURDefaultRadioButton.setChecked(false);
            RUBDefaultRadioButton.setChecked(false);
        }
        if(EURDefaultRadioButton.isPressed()){
            UAHDefaultRadioButton.setChecked(false);
            USDDefaultRadioButton.setChecked(false);
            RUBDefaultRadioButton.setChecked(false);
        }
        if(RUBDefaultRadioButton.isPressed()){
            UAHDefaultRadioButton.setChecked(false);
            USDDefaultRadioButton.setChecked(false);
            EURDefaultRadioButton.setChecked(false);
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
        if (UAHDefaultRadioButton.isChecked()) settings.setDefaultValue(UAHDefaultRadioButton.getId());
        else if (USDDefaultRadioButton.isChecked()) settings.setDefaultValue(USDDefaultRadioButton.getId());
        else if (EURDefaultRadioButton.isChecked()) settings.setDefaultValue(EURDefaultRadioButton.getId());
        else if (RUBDefaultRadioButton.isChecked()) settings.setDefaultValue(RUBDefaultRadioButton.getId());
        settings.setUAHcoins(UAHCoinsSwitch.isChecked());
        settings.setUSDcoins(USDCoinsSwitch.isChecked());
        settings.setEURcoins(EURCoinsSwitch.isChecked());
        settings.setRUBcoins(RUBCoinsSwitch.isChecked());
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

            UAHCoinsSwitch.setChecked(settings.isUAHcoins());
            USDCoinsSwitch.setChecked(settings.isUSDcoins());
            EURCoinsSwitch.setChecked(settings.isEURcoins());
            RUBCoinsSwitch.setChecked(settings.isRUBcoins());
            UAHDefaultRadioButton.setChecked(false);
            if (UAHDefaultRadioButton.getId()==settings.getDefaultValue()) UAHDefaultRadioButton.setChecked(true);
            else if (USDDefaultRadioButton.getId()==settings.getDefaultValue()) USDDefaultRadioButton.setChecked(true);
            else if (EURDefaultRadioButton.getId()==settings.getDefaultValue()) EURDefaultRadioButton.setChecked(true);
            else if (RUBDefaultRadioButton.getId()==settings.getDefaultValue()) RUBDefaultRadioButton.setChecked(true);
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
