package com.example.tko_chess;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Lex Somers
 */
public class BoxingActivity extends AppCompatActivity {

    //ImageView declarations
    ImageView Player1Block;
    ImageView Player1Kick;
    ImageView Player1Jab;
    ImageView Player1FullHeart1;
    ImageView Player1FullHeart2;
    ImageView Player1FullHeart3;
    ImageView Player1EmptyHeart1;
    ImageView Player1EmptyHeart2;
    ImageView Player1EmptyHeart3;

    ImageView Player2Block;
    ImageView Player2Kick;
    ImageView Player2Jab;
    ImageView Player2FullHeart1;
    ImageView Player2FullHeart2;
    ImageView Player2FullHeart3;
    ImageView Player2EmptyHeart1;
    ImageView Player2EmptyHeart2;
    ImageView Player2EmptyHeart3;

    //Button declarations
    Button BlockBtn;
    Button KickBtn;
    Button JabBtn;
    Button ConfirmMoveBtn;
    ImageButton OptionsBtn;

    //TextView declarations
    TextView GameTimeText;
    TextView MoveTimeText;

    //LinearLayout declarations
    LinearLayout OptionsLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boxing);

        //ImageView initializations
        Player1Block = findViewById(R.id.Player1BlockImg);
        Player1Jab = findViewById(R.id.Player1JabImg);
        Player1Kick = findViewById(R.id.Player1KickImg);
        Player1FullHeart1 = findViewById(R.id.Player1FullHeart1);
        Player1FullHeart2 = findViewById(R.id.Player1FullHeart2);
        Player1FullHeart3 = findViewById(R.id.Player1FullHeart3);
        Player1EmptyHeart1 = findViewById(R.id.Player1EmptyHeart1);
        Player1EmptyHeart2 = findViewById(R.id.Player1EmptyHeart2);
        Player1EmptyHeart3 = findViewById(R.id.Player1EmptyHeart3);

        Player2Block = findViewById(R.id.Player2BlockImg);
        Player2Jab = findViewById(R.id.Player2JabImg);
        Player2Kick = findViewById(R.id.Player2KickImg);
        Player2FullHeart1 = findViewById(R.id.Player2FullHeart1);
        Player2FullHeart2 = findViewById(R.id.Player2FullHeart2);
        Player2FullHeart3 = findViewById(R.id.Player2FullHeart3);
        Player2EmptyHeart1 = findViewById(R.id.Player2EmptyHeart1);
        Player2EmptyHeart2 = findViewById(R.id.Player2EmptyHeart2);
        Player2EmptyHeart3 = findViewById(R.id.Player2EmptyHeart3);

        //Button initializations
        BlockBtn = findViewById(R.id.BlockBtn);
        KickBtn = findViewById(R.id.KickBtn);
        JabBtn = findViewById(R.id.JabBtn);
        ConfirmMoveBtn = findViewById(R.id.ConfirmMoveBtn);
        OptionsBtn = findViewById(R.id.BoxingMenuBtn);

        //TextView initializations
        GameTimeText = findViewById(R.id.GameTimeText);
        MoveTimeText = findViewById(R.id.ConfirmMoveTime);

        //LinearLayout initializations
        OptionsLayout = findViewById(R.id.OptionsLayout);
    }
}
