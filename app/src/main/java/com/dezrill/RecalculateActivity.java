package com.dezrill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
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
    private RadioButton rememberRateRadioButton;
    private Button changeOperationButton;
    private boolean multiplication=true;
    private boolean radioChecked=false;
    private double result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recalculate);

        currencySpinner=findViewById(R.id.currencySpinner);
        currencyTextView=findViewById(R.id.currencyTextView);
        changeOperationButton=findViewById(R.id.changeOperationButton);
        sumValueTextView=findViewById(R.id.sumValueTextView);
        rememberRateRadioButton=findViewById(R.id.rememberRateRadioButton);

        Intent intent=getIntent();
        if (intent.getDoubleExtra("OperationValue",0)!=0) {
            sumValueTextView.setText(String.valueOf(intent.getDoubleExtra("OperationValue",0)));
            if (sumValueTextView.getText().toString().substring(sumValueTextView.getText().toString().indexOf(".")).equals(".0")) sumValueTextView.setText(String.format("%.0f", intent.getDoubleExtra("OperationValue", 0)));
        }
        if (intent.getBooleanExtra("Remember", false)!=false) {
            rememberRateRadioButton.setChecked(true);
            radioChecked=true;
        }

        currencyTextView.setText(intent.getStringExtra("Currency")+ " ×");
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
        if (sumValueTextView.length()>5) sumValueTextView.setTextSize(51);
        if (sumValueTextView.length()>6) sumValueTextView.setTextSize(46);
        if (sumValueTextView.length()<=5) sumValueTextView.setTextSize(56);
    }

    public void onClickNumber(View view) {
        blink=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        Button btn=findViewById(view.getId());
        String str=sumValueTextView.getText().toString();
        if (str.length()<7){
            if (str.length()>1) {
                if (str.substring(str.length()-1).equals(".") && view.getId()==R.id.button_point) str=str.substring(0,str.length()-1);
            }
            if (str.length()==1 && str.equals("0") && view.getId()!=R.id.button_point) str="";
            str+=btn.getText();
            String[] points=str.split("\\.");
            if (points.length>=2 && str.substring(str.length()-1).equals(".")) str=str.substring(0,str.length()-1);
            sumValueTextView.setText(str);
            if (sumValueTextView.length()>5) sumValueTextView.setTextSize(51);
            if (sumValueTextView.length()>6) sumValueTextView.setTextSize(46);
            if (sumValueTextView.length()<=5) sumValueTextView.setTextSize(56);
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
            intent.putExtra("Remember", radioChecked);
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    public void onClickChangeOperation(View view) {
        blink=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        if (multiplication) {
            changeOperationButton.setText("ᐸ");
            currencyTextView.setText(currencyTextView.getText().toString().substring(0,currencyTextView.getText().toString().length()-2)+" ÷");
            multiplication=false;
        }
        else {
            changeOperationButton.setText("ᐳ");
            currencyTextView.setText(currencyTextView.getText().toString().substring(0,currencyTextView.getText().toString().length()-2)+" ×");
            multiplication=true;
        }
    }

    public void onClickRememberRate(View view) {
        blink=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        if (radioChecked) {
            rememberRateRadioButton.setChecked(false);
            radioChecked=false;
        }
        else {
            rememberRateRadioButton.setChecked(true);
            radioChecked=true;
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
                adapter=new ArrayAdapter<>(this, R.layout.spinner_item, isUAH);
            }break;
            case "USD": {
                adapter=new ArrayAdapter<>(this, R.layout.spinner_item, isUSD);
            }break;
            case "EUR": {
                adapter=new ArrayAdapter<>(this, R.layout.spinner_item, isEUR);
            }break;
            case "RUB": {
                adapter=new ArrayAdapter<>(this, R.layout.spinner_item, isRUB);
            }break;
            default: adapter=new ArrayAdapter<>(this, R.layout.spinner_item, isUAH); break;
        }
        currencySpinner.setAdapter(adapter);
    }
}