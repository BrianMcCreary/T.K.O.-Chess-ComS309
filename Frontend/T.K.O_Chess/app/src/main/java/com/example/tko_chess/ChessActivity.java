package com.example.tko_chess;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

public class ChessActivity extends AppCompatActivity {

    Drawable piece;
    Drawable selectedSpot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);

        ImageButton A1 = findViewById(R.id.A1);
        ImageButton B1 = findViewById(R.id.B1);
        ImageButton C1 = findViewById(R.id.C1);
        ImageButton D1 = findViewById(R.id.D1);
        ImageButton E1 = findViewById(R.id.E1);
        ImageButton F1 = findViewById(R.id.F1);
        ImageButton G1 = findViewById(R.id.G1);
        ImageButton H1 = findViewById(R.id.H1);

        ImageButton A2 = findViewById(R.id.A2);
        ImageButton B2 = findViewById(R.id.B2);
        ImageButton C2 = findViewById(R.id.C2);
        ImageButton D2 = findViewById(R.id.D2);
        ImageButton E2 = findViewById(R.id.E2);
        ImageButton F2 = findViewById(R.id.F2);
        ImageButton G2 = findViewById(R.id.G2);
        ImageButton H2 = findViewById(R.id.H2);

        ImageButton A3 = findViewById(R.id.A3);
        ImageButton B3 = findViewById(R.id.B3);
        ImageButton C3 = findViewById(R.id.C3);
        ImageButton D3 = findViewById(R.id.D3);
        ImageButton E3 = findViewById(R.id.E3);
        ImageButton F3 = findViewById(R.id.F3);
        ImageButton G3 = findViewById(R.id.G3);
        ImageButton H3 = findViewById(R.id.H3);

        ImageButton A4 = findViewById(R.id.A4);
        ImageButton B4 = findViewById(R.id.B4);
        ImageButton C4 = findViewById(R.id.C4);
        ImageButton D4 = findViewById(R.id.D4);
        ImageButton E4 = findViewById(R.id.E4);
        ImageButton F4 = findViewById(R.id.F4);
        ImageButton G4 = findViewById(R.id.G4);
        ImageButton H4 = findViewById(R.id.H4);

        ImageButton A5 = findViewById(R.id.A5);
        ImageButton B5 = findViewById(R.id.B5);
        ImageButton C5 = findViewById(R.id.C5);
        ImageButton D5 = findViewById(R.id.D5);
        ImageButton E5 = findViewById(R.id.E5);
        ImageButton F5 = findViewById(R.id.F5);
        ImageButton G5 = findViewById(R.id.G5);
        ImageButton H5 = findViewById(R.id.H5);

        ImageButton A6 = findViewById(R.id.A6);
        ImageButton B6 = findViewById(R.id.B6);
        ImageButton C6 = findViewById(R.id.C6);
        ImageButton D6 = findViewById(R.id.D6);
        ImageButton E6 = findViewById(R.id.E6);
        ImageButton F6 = findViewById(R.id.F6);
        ImageButton G6 = findViewById(R.id.G6);
        ImageButton H6 = findViewById(R.id.H6);

        ImageButton A7 = findViewById(R.id.A7);
        ImageButton B7 = findViewById(R.id.B7);
        ImageButton C7 = findViewById(R.id.C7);
        ImageButton D7 = findViewById(R.id.D7);
        ImageButton E7 = findViewById(R.id.E7);
        ImageButton F7 = findViewById(R.id.F7);
        ImageButton G7 = findViewById(R.id.G7);
        ImageButton H7 = findViewById(R.id.H7);

        ImageButton A8 = findViewById(R.id.A8);
        ImageButton B8 = findViewById(R.id.B8);
        ImageButton C8 = findViewById(R.id.C8);
        ImageButton D8 = findViewById(R.id.D8);
        ImageButton E8 = findViewById(R.id.E8);
        ImageButton F8 = findViewById(R.id.F8);
        ImageButton G8 = findViewById(R.id.G8);
        ImageButton H8 = findViewById(R.id.H8);

        A1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                piece = A1.getDrawable();
            }
        });
        A2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        A3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        A4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        A5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        A6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        A7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        A8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                piece = A1.getDrawable();
            }
        });
        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        B3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        B4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        B5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        B6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        B7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        B8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        C1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                piece = A1.getDrawable();
            }
        });
        C2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        C3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        C4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        C5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        C6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        C7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        C8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        D1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                piece = A1.getDrawable();
            }
        });
        D2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        D3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        D4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        D5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        D6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        D7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        D8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        E1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                piece = A1.getDrawable();
            }
        });
        E2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        E3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        E4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        E5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        E6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        E7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        E8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        F1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                piece = A1.getDrawable();
            }
        });
        F2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        F3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        F4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        F5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        F6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        F7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        F8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
