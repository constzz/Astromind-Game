package com.example.astromind.ui;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.astromind.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setupHyperlink();
    }

    private void setupHyperlink(){
        ((TextView) findViewById(R.id.info_about_app_tv)).setMovementMethod(LinkMovementMethod.getInstance());
    }
}