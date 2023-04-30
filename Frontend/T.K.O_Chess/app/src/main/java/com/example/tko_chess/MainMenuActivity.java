package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * @author Lex Somers
 * @author Zachary Scurlock
 *
 * Main menu screen where users can navigate to the different features of the app.
 */
public class MainMenuActivity extends AppCompatActivity {

    /**
     * Takes user to the host or join lobby screen.
     *      Specifies tko chess as the selected game mode.
     */
    Button tkoChess;

    /**
     * Takes user to the host or join lobby screen.
     *      Specifies chess as the selected game mode.
     */
    Button chess;

    /**
     * Takes user to the host or join lobby screen.
     *      Specifies boxing as the selected game mode.
     */
    Button Boxing;

    /**
     * Takes user to the settings screen.
     */
    ImageButton MenuToSettings;

    /**
     * Takes user to the friends screen.
     */
    ImageButton MenuToFriends;

    /**
     * Takes user to the profiles screen.
     */
    ImageButton MenuToProfiles;

    /**
     * Logs user out and returns them to the login screen.
     */
    ImageButton LogoutBtn;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     *     Loads main menus screen onto device.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tkoChess = findViewById(R.id.MenuToTKOChessBtn);

        tkoChess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, HostJoinActivity.class);
                intent.putExtra("Gamemode", "ChessBoxing");
                startActivity(intent);
            }
        });

        chess = findViewById(R.id.MenuToChessBtn);

        chess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, HostJoinActivity.class);
                intent.putExtra("Gamemode", "Chess");
                startActivity(intent);
            }
        });

        Boxing = findViewById(R.id.MenuToTKOBtn);
        Boxing.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainMenuActivity.this, HostJoinActivity.class);
                intent.putExtra("Gamemode", "Boxing");
                startActivity(intent);
            }
        });

        LogoutBtn = findViewById(R.id.MenuToLoginBtn);

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Logs user out by forgetting current user.
                SingletonUser.logout();

                Intent intent = new Intent(MainMenuActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

        MenuToSettings = findViewById(R.id.MenuToSettingsBtn);

        MenuToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, SettingsActvity.class);
                startActivity(intent);
            }
        });



        MenuToProfiles = findViewById(R.id.MenuToProfileBtn);

        MenuToProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        MenuToFriends = findViewById(R.id.MenuToFriendsBtn);



        MenuToFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, FriendsActivity.class);
                startActivity(intent);
            }
        });
        }
    }
