package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ChangePassword extends AppCompatActivity {

    ImageButton goBack;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        goBack = findViewById(R.id.backBtn6);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePassword.this, Profiles.class);
                startActivity(intent);
            }
        });

        save = findViewById(R.id.savebtn3);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassword.this, Profiles.class); //needs to be changed to act like an actual save button
                startActivity(intent);
            }
        });
    }
}
