package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * @author Zachary Scurlock
 * This is where the user will be able to view their profile stats and have the option to edit their profile
 */
public class Profiles extends AppCompatActivity {

    ImageButton goBack; //takes user back to main menu
    Button editProfile; //takes user to profile editor screen
    TextView username; //Shows the username of the user
    SingletonUser currUser = SingletonUser.getInstance();

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     *     loads the profile screen for the user
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        goBack = findViewById(R.id.backBtn);

        goBack.setOnClickListener(new View.OnClickListener() {
            /**
             * @param view The view that was clicked.
             * When pressed the user is taken back to the main menu
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profiles.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

        editProfile = findViewById(R.id.editprofilebtn);

        editProfile.setOnClickListener(new View.OnClickListener() {
            /**
             * @param v The view that was clicked
             *  When pressed the user is taken to the Profile_Editor Screen
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profiles.this, Profile_Editor.class);
                startActivity(intent);
            }
        });

        username = findViewById(R.id.name);
        username.setText(currUser.getUsername());  //sets the username text view object text to the user's username
    }
}
