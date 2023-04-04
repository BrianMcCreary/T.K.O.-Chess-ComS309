package com.example.tko_chess;

import android.content.Intent;
import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class HostOrJoinActivity extends AppCompatActivity {

    Button hostBtn;
    Button joinBtn;
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_or_join);
        backBtn = findViewById(R.id.backBtn7);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HostOrJoinActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

        hostBtn = findViewById(R.id.hostBtn);
        hostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HostOrJoinActivity.this, LobbyActivity.class);
                startActivity(intent);
            }
        });

        joinBtn = findViewById(R.id.joinBtn);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HostOrJoinActivity.this, LobbyPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
