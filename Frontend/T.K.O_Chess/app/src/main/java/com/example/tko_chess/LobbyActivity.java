package com.example.tko_chess;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LobbyActivity extends AppCompatActivity {

    Button startBtn;
    ScrollView lobby;
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_game_lobby);
        backBtn = findViewById(R.id.backBtn2);
    }

}
