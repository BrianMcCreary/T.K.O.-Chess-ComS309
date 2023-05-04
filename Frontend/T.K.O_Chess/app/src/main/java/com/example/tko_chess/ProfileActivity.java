package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tko_chess.ultils.Const;

import java.net.URLConnection;


/**
 * @author Zachary Scurlock
 * @author Lex Somers
 * This is where the user will be able to view their profile stats and have the option to edit their profile
 */
public class ProfileActivity extends AppCompatActivity {

    ImageButton ProfileToMenu; //takes user back to main menu
    Button editProfile; //takes user to profile editor screen
    TextView username; //Shows the username of the user
    TextView ChessBoxingStats;
    TextView ChessStats;
    TextView BoxingStats;
    String URLConcatenation = "";
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

        ProfileToMenu = findViewById(R.id.backBtn);
        editProfile = findViewById(R.id.editprofilebtn);
        username = findViewById(R.id.name);
        ChessBoxingStats = findViewById(R.id.ChessBoxingStatsText);
        ChessStats = findViewById(R.id.ChessStatsText);
        BoxingStats = findViewById(R.id.BoxingStatsText);

        getUserStats();

        ProfileToMenu.setOnClickListener(new View.OnClickListener() {
            /**
             * @param view The view that was clicked.
             * When pressed the user is taken back to the main menu
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            /**
             * @param v The view that was clicked
             *  When pressed the user is taken to the EditProfileActivity Screen
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        username.setText(currUser.getUsername());  //sets the username text view object text to the user's username
    }


    /**
     * Gets user's stats and displays them on screen.
     */
    private void getUserStats() {
        URLConcatenation = currUser.getUsername();

        //Request que used to send JSON requests
        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);

        StringRequest GetUserStatsReq = new StringRequest(Request.Method.GET, Const.URL_SERVER_GETUSERSTATS + URLConcatenation, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] strings = response.split(" ");
                for (int i = 0; i < strings.length; i++) {
                    switch (strings[i]) {
                        case "ChessBoxing":
                            ChessBoxingStats.setText("ChessBoxing: " + strings[i+1] + " - " + strings[i+2]);
                            i += 2;
                            break;

                        case "Chess":
                            ChessStats.setText("Chess: " + strings[i+1] + " - " + strings[i+2]);
                            i += 2;
                            break;

                        case "Boxing":
                            BoxingStats.setText("Boxing: " + strings[i+1] + " - " + strings[i+2]);
                            i += 2;
                            break;
                    }
                }
			}
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(GetUserStatsReq);
    }
}
