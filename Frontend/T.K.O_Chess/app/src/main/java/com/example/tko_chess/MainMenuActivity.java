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

    ImageButton MenuToSettings;
    ImageButton MenuToFriends;
    ImageButton MenuToProfiles;
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
                startActivity(intent);
            }
        });

        Boxing = findViewById(R.id.toTKObtn);
        Boxing.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainMenuActivity.this, BoxingActivity.class);
                startActivity(intent);
            }
        });

        LogoutBtn = findViewById(R.id.LogoutBtn);

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

        MenuToSettings = findViewById(R.id.SettingsBtn);

        MenuToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, Settings.class);
                startActivity(intent);
            }
        });

        MenuToProfiles = findViewById(R.id.ProfileBtn);

        MenuToProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, Profiles.class);
                startActivity(intent);
            }
        });

        MenuToFriends = findViewById(R.id.FriendsBtn);

        MenuToFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, FriendsActivity.class);
                startActivity(intent);
            }
        });
        }
    }
