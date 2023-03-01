package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Profiles extends AppCompatActivity {

    ImageButton goBack;
    Button editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        goBack = findViewById(R.id.backBtn);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profiles.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

        editProfile = findViewById(R.id.editprofilebtn);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profiles.this, Profile_Editor.class);
                startActivity(intent);
            }
        });
    }
}
