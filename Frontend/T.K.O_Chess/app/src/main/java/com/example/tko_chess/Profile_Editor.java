package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * @author Zachary Scurlock
 * This is where the user will be able to choose whether to change their username or password
 */
public class Profile_Editor extends AppCompatActivity {

    ImageButton goBack; // takes user back to profile screen
    Button changeUser; // takes user to the change username screen
    Button changePass; // takes user to the change password screen

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     *     loads the profile editor screen for the user
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editor);

        goBack = findViewById(R.id.backBtn4);

        goBack.setOnClickListener(new View.OnClickListener() {
            /**
             * @param view The view that was clicked.
             * When pressed the user is taken back to the profile screen
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_Editor.this, Profiles.class);
                startActivity(intent);
            }
        });

        changeUser = findViewById(R.id.changeUserBtn);

        changeUser.setOnClickListener(new View.OnClickListener() {
            /**
             * @param v The view that was clicked.
             * When pressed the user is taken to the change username screen
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Editor.this, ChangeUsername.class);
                startActivity(intent);
            }
        });

        changePass = findViewById(R.id.ChangePasswordBtn);

        changePass.setOnClickListener(new View.OnClickListener() {
            /**
             * @param v The view that was clicked.
             * When pressed the user is taken to the change password screen
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Editor.this, ChangePassword.class);
                startActivity(intent);
            }
        });
    }
}
