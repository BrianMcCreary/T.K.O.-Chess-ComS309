package com.example.tko_chess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tko_chess.ultils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author Lex Somers
 * Page for deleting users if user is an admin.
 */
public class SettingsActvity extends AppCompatActivity {

    /**
     * Takes user back to main manu
     */
    ImageButton SettingsToMenu;

    /**
     * TextView says settings unavailable if user is not admin
     */
    TextView SettingsAvailable;

    TextView ErrorMessage;

    /**
     * String stores the ending of the URL path mapping for the websocket.
     */
    String URLConcatenation = "";

    /**
     * String stores the list of users who are registered.
     */
    String ListOfUsers = "";

    /**
     * LinearLayout holding all the users currently registered.
     */
    LinearLayout UserList;

    /**
     * Context for any volley requests made within this activity.
     */

    Context context = this;

    /**
     * SingletonUser instance which stores the currently logged in user.
     */
    SingletonUser currUser = SingletonUser.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SettingsToMenu = findViewById(R.id.SettingsToMenuBtn);
        UserList = findViewById(R.id.AdminSettingsLinearLayout);
        SettingsAvailable = findViewById(R.id.SettingsUnavailableText);
        ErrorMessage = findViewById(R.id.DeleteErrorText);

        if (currUser.isAdmin()) {
            //SettingsAvailable.setVisibility(View.INVISIBLE);
            //getUsers();
            //displayUsers(ListOfUsers);
        }

        SettingsToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActvity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }



    private void getUsers() {
        //Request que used to send JSON requests
        RequestQueue queue = Volley.newRequestQueue(SettingsActvity.this);
        StringRequest GetUsersReq = new StringRequest(Request.Method.PUT, Const.URL_SERVER_GETUSERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("", "getUsers() responded.");
                ListOfUsers = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("", "getUsers() error.");
                ErrorMessage.setText("An error occurred");
            }
        });
        queue.add(GetUsersReq);
        URLConcatenation = "";
    }



    /**
     * Displays all others the user is currently friends with.
     * @param message List of others the user is friends with.
     */
    private void displayUsers(String message) {
        String[] Users = message.split(" ");

        UserList.removeAllViews();
        UserList = findViewById(R.id.AdminSettingsLinearLayout);

        //For User, put that User in the UserList
        for (int i = 0; i < Users.length; i++) {

            View inflatedLayout = getLayoutInflater().inflate(R.layout.delete_user_request_layout, null, false);
            TextView User = (TextView) inflatedLayout.findViewById(R.id.UserNameTextView);
            Button DeleteUserBtn = (Button) inflatedLayout.findViewById(R.id.DeleteUserBtn);

            //Displays the user's name
            User.setText(Users[i]);

            DeleteUserBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    URLConcatenation = User.getText().toString();
                    deleteUser();
                }
            });

            UserList.addView(inflatedLayout);
        }
    }

    /**
     * Sends POST request to backend removing the selected friend from the user's friends list.
     */
    private void deleteUser() {
        //Request que used to send JSON requests
        RequestQueue queue = Volley.newRequestQueue(SettingsActvity.this);
        StringRequest DeleteUserReq = new StringRequest(Request.Method.DELETE, Const.URL_SERVER_USERS + URLConcatenation, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //If removal succeeded, update UserList
                if (response.equals("success")) {
                    getUsers();
                    displayUsers(ListOfUsers);
                } else {
                    ErrorMessage.setText("Could not delete user.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorMessage.setText("An error occurred");
            }
        });
        queue.add(DeleteUserReq);

        URLConcatenation = "";
    }



    /**
     * Halts program for a specified amount of time.
     * @param time is a double containing the information of how long to wait in seconds.
     */
    private void waitTime(double time) {
        time *= 1000;
        try {
            Thread.sleep((int) time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}