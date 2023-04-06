package com.example.tko_chess;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;

import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;


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

    //private WebSocketClient WebSocket;



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



        //Opens the options menu
        OptionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View inflatedLayout = getLayoutInflater().inflate(R.layout.boxing_menu_layout, null, false);
                Button ConcedeGameBtn = (Button) inflatedLayout.findViewById(R.id.ConcedeBtn);
                Button CloseOptionsBtn = (Button) inflatedLayout.findViewById(R.id.BackToGameBtn);

                //Concedes game and returns user to main menu
                ConcedeGameBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO Implement Concede game functionality

                        //Returns user to main menu
                        Intent intent = new Intent(BoxingActivity.this, MainMenuActivity.class);
                        startActivity(intent);
                    }
                });

                //Closes options menu
                CloseOptionsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OptionsLayout.removeAllViews();
                    }
                });
                OptionsLayout.addView(inflatedLayout);
            }
        });

    }
}
