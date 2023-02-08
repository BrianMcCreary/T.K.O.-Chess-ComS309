package com.example.twoscreengoogle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        if (getIntent().hasExtra("key1")) {
            TextView tv = (TextView) findViewById(R.id.textView);
            String text = getIntent().getExtras().getString("key1");
            tv.setText(text);
        }
    }
}