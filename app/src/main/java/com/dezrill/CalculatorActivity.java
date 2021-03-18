package com.dezrill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.dezrill.calculator.R;
import com.dezrill.support.ItemInList;

import java.util.ArrayList;
import java.util.Locale;

public class CalculatorActivity extends AppCompatActivity {
    private TextView chosenValueTextView, sumValueTextView, plusTextView, resultTextView;
    private Animation blink;
    private ArrayList<ItemInList> items;
    int position;
    private String counts[];
    double result=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        chosenValueTextView=findViewById(R.id.chosenValueTextView);
        sumValueTextView=findViewById(R.id.sumValueTextView);
        plusTextView=findViewById(R.id.plusTextView);
        resultTextView=findViewById(R.id.resultTextView);

        Intent intent=getIntent();
        items=(ArrayList<ItemInList>) intent.getSerializableExtra("Items");
        position=intent.getIntExtra("Position", 0);

        chosenValueTextView.setText(items.get(position).getDenomination()+" "+items.get(position).getCurrency());
        plusTextView.setText(items.get(position).getCount());
        resultTextView.setText("= "+items.get(position).getSum()+" "+items.get(position).getCurrency());
    }

    public void onClickBackToMain(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        finish();
    }

    public void onClickDeleteLast(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        String str1=plusTextView.getText().toString();
        String str2=sumValueTextView.getText().toString();
        counts=str2.split(" \\+ ");
        if (str1.length()==1 && !str1.equals("0")) str1="0";
        else if (counts.length>1 && str1.equals("0")) {
            str1=counts[counts.length-1];
            str2="";
            for (int i=0;i<counts.length-1;i++) {
                str2+=counts[i]+" + ";
            }
        }
        else if (counts.length==1 && str1.equals("0") && !counts[0].equals("")){
            str1=counts[0];
            str2="";
        }
        else if (!str1.equals("0")) str1=str1.substring(0,str1.length()-1);
        plusTextView.setText(str1);
        sumValueTextView.setText(str2);
        result=0;
        SumAll();
    }

    public void onClickDeleteAll(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        sumValueTextView.setText("");
        plusTextView.setText("0");
        resultTextView.setText("= 0.00 " + items.get(0).getCurrency());
    }

    public void onClickNumber(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        Button btn=findViewById(view.getId());
        String str=plusTextView.getText().toString();
        if (plusTextView.length()<7){
            if (plusTextView.getText().toString().equals("0")) str="";
            str+=btn.getText();
            plusTextView.setText(str);
            result=0;
            SumAll();
        }
    }

    public void onClickPlus(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        sumValueTextView.setText(sumValueTextView.getText().toString()+plusTextView.getText().toString()+" + ");
        plusTextView.setText("0");
    }

    public void onClickOK(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        if (counts!=null) {
            int count=0;
            String string=String.format(Locale.ROOT,"%.2f", result);
            for (String str:counts) {
                if (!sumValueTextView.getText().toString().equals(""))count+=Integer.parseInt(str);
            }
            count+=Integer.parseInt(plusTextView.getText().toString());
            items.get(position).setCount(String.valueOf(count));
            items.get(position).setSum(string);
        }
        Intent intent=new Intent();
        intent.putExtra("Items", items);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) finish();
        return super.onKeyDown(keyCode, event);
    }

    private void SumAll() {
        String str=sumValueTextView.getText().toString();
        counts=str.split(" \\+ ");
        for (int i=0;i<counts.length;i++) {
            if (!sumValueTextView.getText().toString().equals(""))result=(Integer.parseInt(counts[i])+Integer.parseInt(plusTextView.getText().toString()))*Double.parseDouble(items.get(position).getDenomination());
            else result=Integer.parseInt(plusTextView.getText().toString())*Double.parseDouble(items.get(position).getDenomination());
        }
        str=String.format(Locale.ROOT,"%.2f", result);
        resultTextView.setText("= "+str+" "+ items.get(position).getCurrency());
    }
}