package com.example.tko_chess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Login button that takes users to main menu after inputting a username and password
        Button Login = (Button) findViewById(R.id.LoginButton);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Text fields for users to enter username/password for their account
                EditText Username = (EditText) findViewById(R.id.UsernameText);
                EditText Password = (EditText) findViewById(R.id.PasswordText);

                //Strings containing username/password. Used to check that user does exist in database.
                String username = Username.getText().toString();
                String password = Username.getText().toString();


            }
        });

    }
}