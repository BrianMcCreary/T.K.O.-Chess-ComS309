package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LobbyPasswordActivity extends AppCompatActivity {

    EditText lobbyKey;
    Button joinBtn;
    ImageButton backBtn;
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_password);

        backBtn = findViewById(R.id.backBtn8);

        //Goes back to Host or Join Screen
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LobbyPasswordActivity.this, ChessHostOrJoinActivity.class);
                startActivity(intent);
            }
        });

        joinBtn = findViewById(R.id.joinBtn2);

        //Takes user to joined lobby
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
