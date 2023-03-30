package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class FriendsActivity extends AppCompatActivity {

    ImageButton FriendsToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        FriendsToMenu = findViewById(R.id.backBtn2);

        FriendsToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendsActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
