package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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

        tkoChess = findViewById(R.id.MenuToTKOChessBtn);

        tkoChess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, ChessBoxingActivity.class);
                startActivity(intent);
            }
        });

        chess = findViewById(R.id.MenuToChessBtn);

        chess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, ChessActivity.class);
                startActivity(intent);
            }
        });

        Boxing = findViewById(R.id.MenuToTKOBtn);
        Boxing.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainMenuActivity.this, BoxingActivity.class);
                startActivity(intent);
            }
        });

        LogoutBtn = findViewById(R.id.MenuToLoginBtn);

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Logs user out by forgetting current user.
                SingletonUser.logout();

                Intent intent = new Intent(MainMenuActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

        MenuToSettings = findViewById(R.id.MenuToSettingsBtn);

        MenuToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, Settings.class);
                startActivity(intent);
            }
        });


        MenuToProfiles = findViewById(R.id.MenuToProfileBtn);

        MenuToProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, Profiles.class);
                startActivity(intent);
            }
        });

        MenuToFriends = findViewById(R.id.MenuToFriendsBtn);


        MenuToFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, FriendsActivity.class);
                startActivity(intent);
            }
        });
        }
    }
