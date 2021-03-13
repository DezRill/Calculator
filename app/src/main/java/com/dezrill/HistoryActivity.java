package com.dezrill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.Toast;

import com.dezrill.calculator.R;
import com.dezrill.support.CustomHistoryListViewAdapter;
import com.dezrill.support.HistoryItem;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private Animation blink;
    private ListView historyListView;
    ArrayList<HistoryItem> items;
    CustomHistoryListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyListView=findViewById(R.id.historyListView);

        LoadHistory();
    }

    public void onClickBackToMain(View view) {
        blink= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        view.startAnimation(blink);
        Intent intent=new Intent(HistoryActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void SetAdapter() {
        adapter=new CustomHistoryListViewAdapter(this, items);
        historyListView.setAdapter(adapter);
    }

    private void LoadHistory() {
        try {
            FileInputStream fis=getApplicationContext().openFileInput("history.dat");
            ObjectInputStream ois=new ObjectInputStream(fis);
            items=(ArrayList<HistoryItem>) ois.readObject();
            ois.close();
            fis.close();

            SetAdapter();
        }
        catch (IOException | ClassNotFoundException e) {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            Intent intent=new Intent(HistoryActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}