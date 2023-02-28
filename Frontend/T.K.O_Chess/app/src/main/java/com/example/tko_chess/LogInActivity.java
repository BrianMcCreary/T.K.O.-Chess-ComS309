package com.example.tko_chess;

import android.content.Intent;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.tko_chess.ultils.Const;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class LogInActivity extends AppCompatActivity {

    EditText Username, Password;
    Button Login, toRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Register button that takes user to register page so they can make an account.
        toRegister = (Button) findViewById(R.id.RegisterButton);
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
            }
        });



        //Login button that takes users to main menu after inputting a username and password
        Login = (Button) findViewById(R.id.LoginButton);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Text fields for users to enter username/password for their account
                Username = (EditText) findViewById(R.id.UsernameText);
                Password = (EditText) findViewById(R.id.PasswordText);

                //Strings containing username/password. Used to check that user does exist in database.
                String username = Username.getText().toString();
                String password = Username.getText().toString();

                //Checks to see if there is a user that matches the input username and login.
                JsonObjectRequest userObjectReq = new JsonObjectRequest(Request.Method.GET, Const.URL_SERVER_AN5, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });

                Intent intent = new Intent(LogInActivity.this, MainMenuActivity.class);
                startActivity(intent);

            }
        });

    }
}