package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Profile_Editor extends AppCompatActivity{

    ImageButton goBack;
    Button save;
    Button changeUser;
    Button changePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editor);

        goBack = findViewById(R.id.backBtn4);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_Editor.this, Profiles.class);
                startActivity(intent);
            }
        });

        changeUser = findViewById(R.id.changeUserBtn);

        changeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Editor.this, ChangeUsername.class);
                startActivity(intent);
            }
        });

        changePass = findViewById(R.id.ChangePasswordBtn);

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Editor.this, ChangePassword.class);
                startActivity(intent);
            }
        });
    }
}
