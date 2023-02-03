package com.example.twoscreengoogle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button LoginBtn = (Button) findViewById(R.id.LoginBtn);
        LoginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText password = findViewById(R.id.password);
                String corrPassword = password.getText().toString();

                if (corrPassword.equals("Password1")) {
                    EditText user = findViewById(R.id.username);
                    String username = user.getText().toString();

                    Intent correctLogin = new Intent(getApplicationContext(), SecondActivity.class);
                    correctLogin.putExtra("key1", username);
                    startActivity(correctLogin);
                }
            }
        });

        Button rickRollBtn = (Button) findViewById(R.id.rickRollBtn);
        rickRollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rollingRick = "https://www.youtube.com/watch?v=xvFZjo5PgG0";
                Uri webAddress = Uri.parse(rollingRick);

                Intent goToYoutube = new Intent(Intent.ACTION_VIEW, webAddress);
                startActivity(goToYoutube);
                /*Ask TA's About why resolveActivity(getPackageManager()) didn't work
                if (goToYoutube.resolveActivity(getPackageManager()) != null) {
                   startActivity(goToYoutube);
                }*/
            }
        });


    }
}