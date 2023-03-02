package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ChangeUsername extends AppCompatActivity {

    ImageButton goBack;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        goBack = findViewById(R.id.backBtn5);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeUsername.this, Profiles.class);
                startActivity(intent);
            }
        });

        save = findViewById(R.id.savebtn2);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeUsername.this, Profiles.class); //needs to be changed to act like an actual save button
                startActivity(intent);
            }
        });
    }
}
