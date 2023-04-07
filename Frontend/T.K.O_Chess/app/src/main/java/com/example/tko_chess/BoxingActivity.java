package com.example.tko_chess;

import static android.text.TextUtils.split;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tko_chess.ultils.Const;

import java.net.URI;
import java.net.URISyntaxException;

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
    TextView ShowMoveCountDown3;
    TextView ShowMoveCountDown2;
    TextView ShowMoveCountDown1;

    //Int declarations
    int UserHealth = 3;
    int OpponentHealth = 3;

    //LinearLayout declarations
    LinearLayout OptionsLayout;
    LinearLayout GameOverLayout;

    //String declarations
    String SelectedMove = "";

    //WebSocket declarations
    private WebSocketClient WebSocket;

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
        ShowMoveCountDown3 = findViewById(R.id.MoveCountdown3Text);
        ShowMoveCountDown2 = findViewById(R.id.MoveCountdown2Text);
        ShowMoveCountDown1 = findViewById(R.id.MoveCountdown1Text);

        //LinearLayout initializations
        OptionsLayout = findViewById(R.id.OptionsLayout);
        GameOverLayout = findViewById(R.id.GameOverLayout);

        //Get access to currently logged in user info.
        SingletonUser currUser = SingletonUser.getInstance();

        //String initializations
        String URLConcatenation = "";
        URLConcatenation += currUser.getUsername();



        //Connect to WebSocket
        try {
            WebSocket = new WebSocketClient(new URI(Const.URL_SERVER_WEBSOCKET + URLConcatenation)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {

                }

                @Override
                public void onMessage(String message) {
                    String[] strings = message.split(" ");

                    //If user's move beat opponent's move
                    switch (strings[0]) {
                        case "RoundWin":
                            //Count down to showing move
                            showCountDown();

                            //Show opponent's move
                            showOpponentMove(strings[1]);

                            //Lowers health of opponent
                            OpponentHealth -= 1;

                            //Hides the full heart image and shows the empty heart image corresponding to their current health
                            if (OpponentHealth == 2) {
                                Player2FullHeart1.setVisibility(View.INVISIBLE);
                                Player2EmptyHeart1.setVisibility(View.VISIBLE);
                                //Enables buttons again for the new "round"
                                enableButtons();

                            } else if (OpponentHealth == 1) {
                                Player2FullHeart2.setVisibility(View.INVISIBLE);
                                Player2EmptyHeart2.setVisibility(View.VISIBLE);
                                //Enables buttons again for the new "round"
                                enableButtons();

                            } else if (OpponentHealth == 0) {
                                Player2FullHeart1.setVisibility(View.INVISIBLE);
                                Player2EmptyHeart1.setVisibility(View.VISIBLE);
                                //Don't enable buttons again because game should be over.
                            }

                            //Exit switch statement
                            break;


                        case "RoundLoss":
                            //Count down to showing move
                            showCountDown();

                            //Show opponent's move
                            showOpponentMove(strings[1]);

                            //Lowers health of User
                            UserHealth -= 1;

                            //Lower's user's health and updates health bar
                            if (UserHealth == 2) {
                                Player1FullHeart1.setVisibility(View.INVISIBLE);
                                Player1EmptyHeart1.setVisibility(View.VISIBLE);
                                //Enables buttons again for the new "round"
                                enableButtons();

                            } else if (UserHealth == 1) {
                                Player1FullHeart2.setVisibility(View.INVISIBLE);
                                Player1EmptyHeart2.setVisibility(View.VISIBLE);

                                //Enables buttons again for the new "round"
                                enableButtons();

                            } else if (UserHealth == 0) {
                                Player1FullHeart1.setVisibility(View.INVISIBLE);
                                Player1EmptyHeart1.setVisibility(View.VISIBLE);
                                //Don't enable buttons again because game should be over.
                            }

                            //Exit switch statement
                            break;


                        case "Tie":
                            //Count down to showing move
                            showCountDown();

                            //Show opponent's move
                            showOpponentMove(strings[1]);

                            //Enables buttons again for the new "round"
                            enableButtons();

                            //Exit switch statement
                            break;


                        case "GameWin":
                            //Clears and displays game over overlay
                            GameOverLayout.removeAllViews();
                            GameOverLayout.setVisibility(View.VISIBLE);

                            //Populates overlay with win text.
                            View inflatedLayout = getLayoutInflater().inflate(R.layout.game_result_layout, null, false);
                            TextView resultText = (TextView) inflatedLayout.findViewById(R.id.ResultText);
                            Button BoxingToMenuBtn = (Button) inflatedLayout.findViewById(R.id.BoxingToMenuBtn);

                            //Displays win message on screen
                            resultText.setText("You win!");

                            GameOverLayout.addView(inflatedLayout);

                            BoxingToMenuBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    WebSocket.close();

                                    //Returns user to main menu
                                    Intent intent = new Intent(BoxingActivity.this, MainMenuActivity.class);
                                    startActivity(intent);
                                }
                            });

                        case "GameLoss":
                            //Clears and displays game over overlay
                            GameOverLayout.removeAllViews();
                            GameOverLayout.setVisibility(View.VISIBLE);

                            //Populates overlay with win text.
                            View inflatedLayout2 = getLayoutInflater().inflate(R.layout.game_result_layout, null, false);
                            TextView resultText2 = (TextView) inflatedLayout2.findViewById(R.id.ResultText);
                            Button BoxingToMenuBtn2 = (Button) inflatedLayout2.findViewById(R.id.BoxingToMenuBtn);

                            //Displays win message on screen
                            resultText2.setText("You win!");

                            GameOverLayout.addView(inflatedLayout2);

                            BoxingToMenuBtn2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    WebSocket.close();

                                    //Returns user to main menu
                                    Intent intent = new Intent(BoxingActivity.this, MainMenuActivity.class);
                                    startActivity(intent);
                                }
                            });


                        case "OpponentLeft":
                            //Clears and displays game over overlay
                            GameOverLayout.removeAllViews();
                            GameOverLayout.setVisibility(View.VISIBLE);

                            //Populates overlay with win text.
                            View inflatedLayout3 = getLayoutInflater().inflate(R.layout.game_result_layout, null, false);
                            TextView resultText3 = (TextView) inflatedLayout3.findViewById(R.id.ResultText);
                            Button BoxingToMenuBtn3 = (Button) inflatedLayout3.findViewById(R.id.BoxingToMenuBtn);

                            //Displays win message on screen
                            resultText3.setText("You win!");

                            GameOverLayout.addView(inflatedLayout3);

                            BoxingToMenuBtn3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    WebSocket.close();

                                    //Returns user to main menu
                                    Intent intent = new Intent(BoxingActivity.this, MainMenuActivity.class);
                                    startActivity(intent);
                                }
                            });
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {

                }

                @Override
                public void onError(Exception ex) {

                }
            };
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        //Connect to the websocket
        WebSocket.connect();



        BlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Sets currently selected move to block
                SelectedMove = "";
                SelectedMove = "block";

                //Changes player1 icon to block pose
                Player1Kick.setVisibility(View.INVISIBLE);
                Player1Jab.setVisibility(View.INVISIBLE);

                Player1Block.setVisibility(View.VISIBLE);
            }
        });



        KickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Sets currently selected move to kick
                SelectedMove = "";
                SelectedMove = "kick";

                //Changes player1 icon to kick pose
                Player1Block.setVisibility(View.INVISIBLE);
                Player1Jab.setVisibility(View.INVISIBLE);

                Player1Kick.setVisibility(View.VISIBLE);
            }
        });



        JabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Sets currently selected move to jab
                SelectedMove = "";
                SelectedMove = "jab";

                //Changes player1 icon to jab pose
                Player1Block.setVisibility(View.INVISIBLE);
                Player1Kick.setVisibility(View.INVISIBLE);

                Player1Jab.setVisibility(View.VISIBLE);
            }
        });



        ConfirmMoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Sends move to backend
                //TODO Uncomment once websockets get pushed on the backend
                WebSocket.send(SelectedMove);

                //Disables buttons until other user confirms
                disableButtons();

            }
        });



        //Opens the options menu
        OptionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View inflatedLayout = getLayoutInflater().inflate(R.layout.boxing_menu_layout, null, false);
                Button ConcedeGameBtn = (Button) inflatedLayout.findViewById(R.id.ConcedeBtn);
                Button CloseOptionsBtn = (Button) inflatedLayout.findViewById(R.id.BackToGameBtn);
                Button TestBoxingBtn = (Button) inflatedLayout.findViewById(R.id.TestBoxingBtn);
                EditText BoxingTestText = (EditText) inflatedLayout.findViewById(R.id.BoxingTestText);

                //Concedes game and returns user to main menu
                ConcedeGameBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        WebSocket.send("leave");
                        WebSocket.close();

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

                //TODO ////////////////////////////////////DELETE WHEN DONE TESTING //////////////////////////////////////////
                //Temporary button meant for hosting a test game
                TestBoxingBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RequestQueue queue = Volley.newRequestQueue(BoxingActivity.this);

                        String temp = currUser.getUsername() + "/" + BoxingTestText.getText().toString();
                        StringRequest HostGameReq = new StringRequest(Request.Method.POST, Const.URL_SERVER_BOXINGTEST + temp, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                    }
                });
                //TODO ////////////////////////////////////DELETE WHEN DONE TESTING //////////////////////////////////////////

                OptionsLayout.addView(inflatedLayout);
            }
        });

    }



    //Makes buttons clickable and lightens their color back
    private void enableButtons() {
        //Changes appearance of buttons
        BlockBtn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.soft_blue)));
        KickBtn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.soft_blue)));
        JabBtn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.soft_blue)));
        ConfirmMoveBtn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.soft_blue)));

        //Enables buttons again
        BlockBtn.setClickable(true);
        KickBtn.setClickable(true);
        JabBtn.setClickable(true);
        ConfirmMoveBtn.setClickable(true);
    }



    //Makes buttons un-clickable and darkens their color
    private void disableButtons() {
        //Changes appearance of buttons
        BlockBtn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.faded_soft_blue)));
        KickBtn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.faded_soft_blue)));
        JabBtn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.faded_soft_blue)));
        ConfirmMoveBtn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.faded_soft_blue)));

        //Disables buttons until opponent confirms their move
        BlockBtn.setClickable(false);
        KickBtn.setClickable(false);
        JabBtn.setClickable(false);
        ConfirmMoveBtn.setClickable(false);
    }



    //Shows opponent's moves and then reverts back to default block stance.
    private void showOpponentMove(String move) {
        //Show opponent's move
        if (move.equals("block")) {
            //Do nothing because default stance is block
            waitTime(3.0);

        } else if (move.equals("kick")) {
            //Hides block and shows kick
            Player2Block.setVisibility(View.INVISIBLE);
            Player2Kick.setVisibility(View.VISIBLE);
            waitTime(3.0);
            //Hides kick and goes back to default block stance
            Player2Kick.setVisibility(View.INVISIBLE);
            Player2Block.setVisibility(View.VISIBLE);

        } else if (move.equals("jab")) {
            //Hides block and shows jab
            Player2Block.setVisibility(View.INVISIBLE);
            Player2Jab.setVisibility(View.VISIBLE);
            waitTime(3.0);
            //Hides jab and goes back to default block stance
            Player2Jab.setVisibility(View.INVISIBLE);
            Player2Block.setVisibility(View.VISIBLE);
        }
    }



    //Show move reveal countdown
    private void showCountDown() {
        //Show 3
        ShowMoveCountDown3.setVisibility(View.VISIBLE);
        waitTime(1.0);
        ShowMoveCountDown3.setVisibility(View.INVISIBLE);

        //Show 2
        ShowMoveCountDown2.setVisibility(View.VISIBLE);
        waitTime(1.0);
        ShowMoveCountDown2.setVisibility(View.INVISIBLE);

        //Show 1
        ShowMoveCountDown1.setVisibility(View.VISIBLE);
        waitTime(1.0);
        ShowMoveCountDown1.setVisibility(View.INVISIBLE);
    }



    //Wait time seconds
    private void waitTime(double time) {
        time *= 1000;
        try {
            Thread.sleep((int) time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
