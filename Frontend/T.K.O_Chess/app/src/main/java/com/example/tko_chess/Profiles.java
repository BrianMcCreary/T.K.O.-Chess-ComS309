package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Profiles extends AppCompatActivity {

    ImageButton goBack;
    Button editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        goBack = findViewById(R.id.backBtn);

        if(getIntent().hasExtra("key1")){
            TextView username = (TextView)findViewById(R.id.name);
            String s = getIntent().getExtras().getString("key1");
            username.setText(s);
        }

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
