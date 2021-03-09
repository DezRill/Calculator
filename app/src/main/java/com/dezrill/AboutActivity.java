package com.dezrill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.dezrill.calculator.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void onClickBackToMain(View view) {
        Intent intent=new Intent(AboutActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickHyperlink(View view) {
        Intent intentBrowser=new Intent(Intent.ACTION_VIEW, Uri.parse("https://complife.ua/"));
        startActivity(intentBrowser);
    }
}