package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainMenuActivity extends AppCompatActivity {

    Button tkoChess;
    Button chess;
    Button Boxing;

    ImageButton toSettingsBtn;
    ImageButton toFriendsBtn;
    ImageButton profilesBtn;
    ImageButton LogoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tkoChess = findViewById(R.id.toTKOChessbtn);

        tkoChess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, ChessBoxingActivity.class);
                startActivity(intent);
            }
        });

        chess = findViewById(R.id.toChessbtn);

        chess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, ChessActivity.class);
            }
        });

        Boxing = findViewById(R.id.toTKObtn);
        Boxing.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainMenuActivity.this, BoxingActivity.class);
            }
        });

        LogoutBtn = findViewById(R.id.LogoutBtn);

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, LogInActivity.class);
            }
        });

        toSettingsBtn = findViewById(R.id.SettingsBtn);

        toSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, Settings.class);
            }
        });

        profilesBtn = findViewById(R.id.ProfileBtn);

        profilesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, Profiles.class);
            }
        });

        toFriendsBtn = findViewById(R.id.FriendsBtn);

        toFriendsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, Friends.class);
            }
        });
        }
    }
