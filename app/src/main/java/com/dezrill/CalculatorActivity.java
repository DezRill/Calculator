package com.dezrill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dezrill.calculator.R;

public class CalculatorActivity extends AppCompatActivity {
    TextView sumMainTextView, sumValueTextView, resultTextView;
    String currency;
    double denomination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        sumMainTextView=findViewById(R.id.chosenValueTextView);
        sumValueTextView=findViewById(R.id.sumValueTextView);
        resultTextView=findViewById(R.id.resultTextView);

        Intent intent=getIntent();
        currency=intent.getStringExtra("currency");
        denomination=intent.getDoubleExtra("value", 0);

        sumMainTextView.setText(denomination+" "+currency);
        resultTextView.setText(resultTextView.getText()+" "+currency);
    }

    public void onClickBackToMain(View view) {
        BackToMain();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) BackToMain();
        return super.onKeyDown(keyCode, event);
    }

    public void onClickDeleteLast(View view) {
        String str=sumValueTextView.getText().toString();
        if (str.length()==1) str="0";
        else if (str.substring(str.length()-2).equals(" 0")) str=str.substring(0,str.length()-4);
        else str=str.substring(0,str.length()-1);
        if (str.substring(str.length()-1).equals(" ")) str+="0";
        sumValueTextView.setText(str);
        SumAll();
    }

    public void onClickDeleteAll(View view) {
        sumValueTextView.setText("0");
        resultTextView.setText("= 0");
    }

    public void onClickNumber(View view) {
        Button btn=findViewById(view.getId());
        String str=sumValueTextView.getText().toString();
        if (str.substring(str.length()-1).equals("0")) str=str.substring(0,str.length()-1);
        str+=btn.getText();
        sumValueTextView.setText(str);
        SumAll();
    }

    public void onClickPlus(View view) {
        sumValueTextView.setText(sumValueTextView.getText()+" + 0");
    }

    public void onClickOK(View view) {
    }

    private void BackToMain() {
        Intent intent=new Intent(CalculatorActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void SumAll() {
        String counts[];
        double result=0;
        String str=sumValueTextView.getText().toString();
        counts=str.split(" \\+ ");
        for (int i=0;i<counts.length;i++) {
            result=(Integer.parseInt(counts[i])*denomination)+result;
        }
        resultTextView.setText("= "+result+" "+currency);
    }
}