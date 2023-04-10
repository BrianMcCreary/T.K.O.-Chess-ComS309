package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tko_chess.ultils.Const;

public class TKOChessHostOrJoinActivity extends AppCompatActivity {

    Button hostBtn;
    Button joinBtn;
    ImageButton backBtn;
    String URLConcatenation = "";
    public String lobbyKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tko_chess_host_or_join);
        backBtn = findViewById(R.id.backBtn8);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TKOChessHostOrJoinActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

        hostBtn = findViewById(R.id.hostBtn2);
        hostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TKOChessHostOrJoinActivity.this, TKOChessLobbyActivity.class);
                startActivity(intent);
            }
        });

        joinBtn = findViewById(R.id.joinBtn);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TKOChessHostOrJoinActivity.this, TKOChessLobbyKeyActivity.class);
                startActivity(intent);
            }
        });


    }
}
