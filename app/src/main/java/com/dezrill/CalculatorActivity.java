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
    TextView chosenValueTextView, sumValueTextView, resultTextView;
    private Animation blink;
    ArrayList<ItemInList> items;
    int position;
    String counts[];
    double result=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        chosenValueTextView=findViewById(R.id.chosenValueTextView);
        sumValueTextView=findViewById(R.id.sumValueTextView);
        resultTextView=findViewById(R.id.resultTextView);

        Intent intent=getIntent();
        items=(ArrayList<ItemInList>) intent.getSerializableExtra("Items");
        position=intent.getIntExtra("Position", 0);

        chosenValueTextView.setText(items.get(position).getDenomination()+" "+items.get(position).getCurrency());
        resultTextView.setText(resultTextView.getText()+" "+items.get(position).getCurrency());
    }

    public void onClickBackToMain(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) finish();
        return super.onKeyDown(keyCode, event);
    }

    public void onClickDeleteLast(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        String str=sumValueTextView.getText().toString();
        if (str.length()==1) str="0";
        else if (str.substring(str.length()-2).equals(" 0")) str=str.substring(0,str.length()-4);
        else str=str.substring(0,str.length()-1);
        if (str.substring(str.length()-1).equals(" ")) str+="0";
        sumValueTextView.setText(str);
        result=0;
        SumAll();
    }

    public void onClickDeleteAll(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        sumValueTextView.setText("0");
        resultTextView.setText("= 0.00" + items.get(0).getCurrency());
    }

    public void onClickNumber(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        Button btn=findViewById(view.getId());
        String str=sumValueTextView.getText().toString();
        if (!TooMuch()){
            if (str.length()>1)
                if (str.substring(str.length()-2).equals(" 0")) str=str.substring(0,str.length()-1);
            if (str.length()==1 && str.equals("0")) str="";
            str+=btn.getText();
            sumValueTextView.setText(str);
            result=0;
            SumAll();
        }
    }

    public void onClickPlus(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        sumValueTextView.setText(sumValueTextView.getText()+" + 0");
    }

    public void onClickOK(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        int count=0;
        String string=String.format(Locale.ROOT,"%.2f", result);
        for (String str:counts) {
            count+=Integer.parseInt(str);
        }
        items.get(position).setCount(String.valueOf(count));
        items.get(position).setSum(string);
        Intent intent=new Intent();
        intent.putExtra("Items", items);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void SumAll() {
        String str=sumValueTextView.getText().toString();
        counts=str.split(" \\+ ");
        for (int i=0;i<counts.length;i++) {
            result=(Integer.parseInt(counts[i])*Double.parseDouble(items.get(position).getDenomination()))+result;
        }
        str=String.format(Locale.ROOT,"%.2f", result);
        resultTextView.setText("= "+str+" "+ items.get(position).getCurrency());
    }

    private boolean TooMuch()
    {
        String str=sumValueTextView.getText().toString();
        String[] needLast=str.split(" \\+ ");
        if (needLast[needLast.length-1].length()>6) return true;
        else return false;
    }
}