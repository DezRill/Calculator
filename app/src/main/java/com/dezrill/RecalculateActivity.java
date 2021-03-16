package com.dezrill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.dezrill.calculator.R;

import java.util.Locale;

public class RecalculateActivity extends AppCompatActivity {

    private Animation blink;
    private String[] isUAH={"EUR", "USD", "RUB"};
    private String[] isUSD={"UAH", "EUR", "RUB"};
    private String[] isEUR={"UAH", "USD", "RUB"};
    private String[] isRUB={"UAH", "USD", "EUR"};
    private Spinner currencySpinner;
    private TextView currencyTextView, sumValueTextView;
    private Button chaneOperationButton;
    private boolean multiplication=true;
    private double result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recalculate);

        currencySpinner=findViewById(R.id.currencySpinner);
        currencyTextView=findViewById(R.id.currencyTextView);
        chaneOperationButton=findViewById(R.id.changeOperationButton);
        sumValueTextView=findViewById(R.id.sumValueTextView);

        Intent intent=getIntent();
        if (intent.getDoubleExtra("OperationValue",0)!=0) sumValueTextView.setText(String.valueOf(intent.getDoubleExtra("OperationValue",0)));

        currencyTextView.setText(intent.getStringExtra("Currency")+ "×");
        FillSpinner(intent.getStringExtra("Currency"));
    }

    public void onClickBackToMain(View view) {
        blink=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        finish();
    }

    public void onClickDeleteLast(View view) {
        blink=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        String str=sumValueTextView.getText().toString();
        if (str.length()==1) str="0";
        else if (str.substring(str.length()-2).equals(" 0")) str=str.substring(0,str.length()-4);
        else str=str.substring(0,str.length()-1);
        if (str.substring(str.length()-1).equals(" ")) str+="0";
        sumValueTextView.setText(str);
    }

    public void onClickNumber(View view) {
        blink=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        Button btn=findViewById(view.getId());
        String str=sumValueTextView.getText().toString();
        if (!TooMuch()){
            if (str.length()>1) {
                if (str.substring(str.length()-1).equals(".") && view.getId()==R.id.button_point) str=str.substring(0,str.length()-1);
            }
            if (str.length()==1 && str.equals("0") && view.getId()!=R.id.button_point) str="";
            str+=btn.getText();
            String[] points=str.split("\\.");
            if (points.length>=2 && str.substring(str.length()-1).equals(".")) str=str.substring(0,str.length()-1);
            sumValueTextView.setText(str);
        }
    }

    public void onClickOK(View view) {
        blink=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);

        if (!sumValueTextView.getText().toString().equals("0")) {
            result=Double.parseDouble(sumValueTextView.getText().toString());
            Intent intent=new Intent();
            intent.putExtra("Result", result);
            intent.putExtra("Operation", multiplication);
            intent.putExtra("Currency", currencySpinner.getSelectedItem().toString());
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    public void onClickChangeOperation(View view) {
        blink=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        if (multiplication) {
            chaneOperationButton.setText("ᐳ");
            currencyTextView.setText(currencyTextView.getText().toString().substring(0,currencyTextView.getText().toString().length()-1)+"÷");
            multiplication=false;
        }
        else {
            chaneOperationButton.setText("ᐸ");
            currencyTextView.setText(currencyTextView.getText().toString().substring(0,currencyTextView.getText().toString().length()-1)+"×");
            multiplication=true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void FillSpinner (String currency) {
        ArrayAdapter<String> adapter;
        switch (currency) {
            case "UAH": {
                adapter=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, isUAH);
            }break;
            case "USD": {
                adapter=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, isUSD);
            }break;
            case "EUR": {
                adapter=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, isEUR);
            }break;
            case "RUB": {
                adapter=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, isRUB);
            }break;
            default: adapter=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, isUAH); break;
        }
        currencySpinner.setAdapter(adapter);
    }

    private boolean TooMuch()
    {
        String str=sumValueTextView.getText().toString();
        String[] needLast=str.split(" \\+ ");
        if (needLast[needLast.length-1].length()>7) return true;
        else return false;
    }
}