package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

public class TKOLobbyActivity extends AppCompatActivity {

    Button startBtn;
    ScrollView lobby;
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tko_game_lobby);

        backBtn = findViewById(R.id.backBtn2);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TKOLobbyActivity.this, TKOHostOrJoinActivity.class);
                startActivity(intent);
            }
        });

        startBtn = findViewById(R.id.startBtn3);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TKOLobbyActivity.this, BoxingActivity.class);
                startActivity(intent);
            }
        });
    }

}
