package com.dezrill;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.dezrill.calculator.R;

public class AboutActivity extends AppCompatActivity {
    private Animation blink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void onClickBackToMain(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        Intent intent=new Intent(AboutActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickHyperlink(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        Intent intentBrowser=new Intent(Intent.ACTION_VIEW, Uri.parse("https://complife.ua/"));
        startActivity(intentBrowser);
    }
}