package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * @author Zachary Scurlock
 * This is code for the functionality of the settings screen
 */
public class Settings extends AppCompatActivity {

    ImageButton goBack; //When pressed the user will be taken back to the main menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        goBack = findViewById(R.id.backBtn3);

        /*
         *  When pressed the user is taken back to the main menu
         */
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
