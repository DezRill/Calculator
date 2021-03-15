package com.dezrill;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dezrill.calculator.R;
import com.dezrill.support.Settings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private Settings settings;
    private RadioButton UAHDefaultRadioButton, USDDefaultRadioButton, EURDefaultRadioButton, RUBDefaultRadioButton, defaultRUradioButton, defaultUAradioButton, defaultENradioButton;
    private Switch UAHCoinsSwitch, USDCoinsSwitch, EURCoinsSwitch, RUBCoinsSwitch;
    private Animation blink;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        UAHDefaultRadioButton=findViewById(R.id.UAHDefaultRadioButton);
        USDDefaultRadioButton=findViewById(R.id.USDDefaultRadioButton);
        EURDefaultRadioButton=findViewById(R.id.EURDefaultRadioButton);
        RUBDefaultRadioButton=findViewById(R.id.RUBDefaultRadioButton);
        defaultRUradioButton=findViewById(R.id.defaultRUradioButton);
        defaultUAradioButton=findViewById(R.id.defaultUAradioButton);
        defaultENradioButton=findViewById(R.id.defaultENradioButton);
        UAHCoinsSwitch=findViewById(R.id.UAHCoinsSwitch);
        USDCoinsSwitch=findViewById(R.id.USDCoinsSwitch);
        EURCoinsSwitch=findViewById(R.id.EURCoinsSwitch);
        RUBCoinsSwitch=findViewById(R.id.RUBCoinsSwitch);

        Intent intent=getIntent();
        settings=(Settings) intent.getSerializableExtra("Settings");
        getSettings();
    }

    public void onClickCheсked(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);

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
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        setSettings();
        SaveSettings();
        BackToMainMenu();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            setSettings();
            SaveSettings();
            BackToMainMenu();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setSettings(){
        if (UAHDefaultRadioButton.isChecked()) settings.setDefaultValue(UAHDefaultRadioButton.getId());
        else if (USDDefaultRadioButton.isChecked()) settings.setDefaultValue(USDDefaultRadioButton.getId());
        else if (EURDefaultRadioButton.isChecked()) settings.setDefaultValue(EURDefaultRadioButton.getId());
        else if (RUBDefaultRadioButton.isChecked()) settings.setDefaultValue(RUBDefaultRadioButton.getId());
        settings.setUAHcoins(UAHCoinsSwitch.isChecked());
        settings.setUSDcoins(USDCoinsSwitch.isChecked());
        settings.setEURcoins(EURCoinsSwitch.isChecked());
        settings.setRUBcoins(RUBCoinsSwitch.isChecked());
        if (defaultRUradioButton.isChecked()) settings.setLanguage("ru");
        else if (defaultUAradioButton.isChecked()) settings.setLanguage("uk");
        else if (defaultENradioButton.isChecked()) settings.setLanguage("en");
    }

    private void BackToMainMenu() {
        Intent intent=new Intent();
        intent.putExtra("Settings", settings);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void getSettings() {
        try {
            UAHCoinsSwitch.setChecked(settings.isUAHcoins());
            USDCoinsSwitch.setChecked(settings.isUSDcoins());
            EURCoinsSwitch.setChecked(settings.isEURcoins());
            RUBCoinsSwitch.setChecked(settings.isRUBcoins());
            UAHDefaultRadioButton.setChecked(false);
            if (UAHDefaultRadioButton.getId()==settings.getDefaultValue()) UAHDefaultRadioButton.setChecked(true);
            else if (USDDefaultRadioButton.getId()==settings.getDefaultValue()) USDDefaultRadioButton.setChecked(true);
            else if (EURDefaultRadioButton.getId()==settings.getDefaultValue()) EURDefaultRadioButton.setChecked(true);
            else if (RUBDefaultRadioButton.getId()==settings.getDefaultValue()) RUBDefaultRadioButton.setChecked(true);
            if (settings.getLanguage().equals("ru")) defaultRUradioButton.setChecked(true);
            else if (settings.getLanguage().equals("uk")) defaultUAradioButton.setChecked(true);
            else if (settings.getLanguage().equals("en")) defaultENradioButton.setChecked(true);
        }
        catch (Exception e) {
            UAHDefaultRadioButton.setChecked(true);
            setCheckedBySystemLanguage();
        }
    }

    private void SaveSettings()
    {
        try {
            setSettings();
            FileOutputStream fos=getApplicationContext().openFileOutput("settings.dat", Context.MODE_PRIVATE);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(settings);
            oos.close();
            fos.close();
        }
        catch (IOException e) {
        }
    }

    public void onClickChangeDefaultLanguage(View view) {
        if (view==findViewById(R.id.langRUTextView) || view==defaultRUradioButton) {
            LoadLanguage("ru");
            defaultRUradioButton.setChecked(true);
            defaultUAradioButton.setChecked(false);
            defaultENradioButton.setChecked(false);
        }
        else if (view==findViewById(R.id.langUATextView) || view==defaultUAradioButton) {
            LoadLanguage("uk");
            defaultRUradioButton.setChecked(false);
            defaultUAradioButton.setChecked(true);
            defaultENradioButton.setChecked(false);
        }
        else if (view==findViewById(R.id.langENTextView) || view==defaultENradioButton) {
            LoadLanguage("en");
            defaultRUradioButton.setChecked(false);
            defaultUAradioButton.setChecked(false);
            defaultENradioButton.setChecked(true);
        }
        defaultRUradioButton.invalidate();
        defaultUAradioButton.invalidate();
        defaultENradioButton.invalidate();
    }

    private void LoadLanguage(String lang)
    {
        String languageToLoad=lang;
        Locale locale=new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config=new Configuration();
        config.locale=locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        updateElements();
    }

    private void setCheckedBySystemLanguage() {
        if (Locale.getDefault().getLanguage().equals("ru")) {
            defaultRUradioButton.setChecked(true);
            defaultUAradioButton.setChecked(false);
            defaultENradioButton.setChecked(false);
        }
        else if (Locale.getDefault().getLanguage().equals("uk")) {
            defaultRUradioButton.setChecked(false);
            defaultUAradioButton.setChecked(true);
            defaultENradioButton.setChecked(false);
        }
        else if (Locale.getDefault().getLanguage().equals("en")) {
            defaultRUradioButton.setChecked(false);
            defaultUAradioButton.setChecked(false);
            defaultENradioButton.setChecked(true);
        }
    }

    private void updateElements() {
        TextView header=findViewById(R.id.headerTextView);
        TextView currency=findViewById(R.id.currencyTextView);
        TextView language=findViewById(R.id.languageTextView);
        header.setText(R.string.settings);
        currency.setText(R.string.currency);
        language.setText(R.string.language);
        UAHCoinsSwitch.setText(R.string.coins);
        USDCoinsSwitch.setText(R.string.coins);
        EURCoinsSwitch.setText(R.string.coins);
        RUBCoinsSwitch.setText(R.string.coins);
        UAHDefaultRadioButton.setText(R.string.def_value);
        USDDefaultRadioButton.setText(R.string.def_value);
        EURDefaultRadioButton.setText(R.string.def_value);
        RUBDefaultRadioButton.setText(R.string.def_value);
        defaultRUradioButton.setText(R.string.def_value);
        defaultUAradioButton.setText(R.string.def_value);
        defaultENradioButton.setText(R.string.def_value);
    }
}
