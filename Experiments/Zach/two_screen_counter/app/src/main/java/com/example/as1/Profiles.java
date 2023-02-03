package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Scanner;

public class Profiles extends AppCompatActivity{

    Button login;
    Button goBack;
    TextView TextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        login = findViewById(R.id.button);
        goBack = findViewById(R.id.button2);
        TextView = findViewById(R.id.textView);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Profiles.this, MainActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Profiles.this, MainActivity.class);
                Intent intent3 = new Intent(getApplicationContext(), MainActivity.class);
                intent3.putExtra("com.example.as1.SOMETHING", "Welcome User");
                startActivity(intent3);
                startActivity(intent);
            }
        });




    }
}
