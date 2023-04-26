package com.example.tko_chess;

import static android.text.TextUtils.split;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.net.URISyntaxException;

import com.example.tko_chess.ultils.Const;

/**
 * @author Lex Somers
 *
 * Boxing activity where users play against another user in a game of
 *     rock, papaer, scissors like "boxing".
 */
public class BoxingActivity extends AppCompatActivity {

    /**
     * ImageView declarations.
     */
    ImageView Player1Block;
    ImageView Player1Kick;
    ImageView Player1Jab;

    ImageView UserHeart6;
    ImageView UserHeart5;
    ImageView UserHeart4;
    ImageView UserHeart3;
    ImageView UserHeart2;
    ImageView UserHeart1;

    ImageView Player2Block;
    ImageView Player2Kick;
    ImageView Player2Jab;
    ImageView OpponentHeart6;
    ImageView OpponentHeart5;
    ImageView OpponentHeart4;
    ImageView OpponentHeart3;
    ImageView OpponentHeart2;
    ImageView OpponentHeart1;

    /**
     * Button declarations.
     */
    Button BlockBtn;
    Button KickBtn;
    Button JabBtn;
    Button ConfirmMoveBtn;
    ImageButton OptionsBtn;

    /**
     * TextView declarations.
     */
    TextView GameTimeText;
    TextView Player1Name;
    TextView Player2Name;
    TextView SelectMoveText;

    /**
     * Int declarations.
     */
    int UserHealth = 3;
    int OpponentHealth = 3;

    /**
     * LinearLayout declarations.
     */
    LinearLayout OptionsLayout;
    LinearLayout GameOverLayout;

    /**
     * String declarations.
     */
    String SelectedMove = "";
    String GameMode;
    String UserRole;

    /**
     * Stores the currently logged in user.
     */
    SingletonUser currUser = SingletonUser.getInstance();

    /**
     * WebSocket used for sending and receiving messages about the user and
     *      opponent's moves, round results, and when the game ends.
     */
    private WebSocketClient WebSocket;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     * Loads boxing screen onto device. Displays starting health, opposing player's username,
     *     and other information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boxing);



        //ImageView initializations
        Player1Block = findViewById(R.id.Player1BlockImg);
        Player1Jab = findViewById(R.id.Player1JabImg);
        Player1Kick = findViewById(R.id.Player1KickImg);
        UserHeart6 = findViewById(R.id.Player1Heart6);
        UserHeart5 = findViewById(R.id.Player1Heart5);
        UserHeart4 = findViewById(R.id.Player1Heart4);
        UserHeart3 = findViewById(R.id.Player1Heart3);
        UserHeart2 = findViewById(R.id.Player1Heart2);
        UserHeart1 = findViewById(R.id.Player1Heart1);

        Player2Block = findViewById(R.id.Player2BlockImg);
        Player2Jab = findViewById(R.id.Player2JabImg);
        Player2Kick = findViewById(R.id.Player2KickImg);
        OpponentHeart6 = findViewById(R.id.Player2Heart6);
        OpponentHeart5 = findViewById(R.id.Player2Heart5);
        OpponentHeart4 = findViewById(R.id.Player2Heart4);
        OpponentHeart3 = findViewById(R.id.Player2Heart3);
        OpponentHeart2 = findViewById(R.id.Player2Heart2);
        OpponentHeart1 = findViewById(R.id.Player2Heart1);

        //Button initializations
        BlockBtn = findViewById(R.id.BlockBtn);
        KickBtn = findViewById(R.id.KickBtn);
        JabBtn = findViewById(R.id.JabBtn);
        ConfirmMoveBtn = findViewById(R.id.ConfirmMoveBtn);
        OptionsBtn = findViewById(R.id.BoxingMenuBtn);

        //TextView initializations
        GameTimeText = findViewById(R.id.RoundNumberText);
        Player1Name = findViewById(R.id.Player1NameText);
        Player2Name = findViewById(R.id.Player2NameText);
        SelectMoveText = findViewById(R.id.SelectMoveText);

        //LinearLayout initializations
        OptionsLayout = findViewById(R.id.OptionsLayout);
        GameOverLayout = findViewById(R.id.GameOverLayout);

        //String initializations
        GameMode = getIntent().getExtras().getString("Gamemode");
        UserRole = getIntent().getExtras().getString("UserRole");

        String URLConcatenation = "";
        URLConcatenation += currUser.getUsername();

        Player1Name.setText(currUser.getUsername());
        //TODO Display opponent's name on screen as well using displayOpponentName();

        Draft[] drafts = {
                new Draft_6455()
        };

        //Connect to WebSocket
        try {
            WebSocket = new WebSocketClient(new URI(Const.URL_SERVER_WEBSOCKETBOXING + URLConcatenation), (Draft)drafts[0]) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d("OPEN", "run() returned: " + "is connecting");
                    System.out.println("onOpen returned");
                }

                @Override
                public void onMessage(String message) {
                    Log.d("", "run() returned: " + message);
                    String[] strings = message.split(" ");

                    switch (strings[0]) {
                        case "RoundWin":
                            //Show opponent's move
                            showOpponentMove(strings[1]);
                            System.out.println("show move returned");
                            waitTime(3.0);

                            //Hide opponent's move
                            hideOpponentMove();
                            System.out.println("hide move returned");


                            //Lowers health of opponent
                            OpponentHealth -= 1;
                            lowerOpponentHealth(OpponentHealth);
                            System.out.println("lower health returned");

                            //Reset user's stance to default
                            showDefaultStance();
                            SelectedMove = "";

                            //Enables buttons again for the new "round"
                            enableButtons();
                            System.out.println("buttons enabled");

                            //Exit switch statement
                            break;


                        case "RoundLoss":
                            //Show opponent's move
                            showOpponentMove(strings[1]);
                            System.out.println("show move returned");
                            waitTime(3.0);

                            //Hide opponent's move
                            hideOpponentMove();
                            System.out.println("hide move returned");


                            //Lowers health of User
                            UserHealth -= 1;
                            lowerUserHealth(UserHealth);
                            System.out.println("lower health returned");

                            //Reset user's stance to default
                            showDefaultStance();
                            SelectedMove = "";

                            //Enables buttons again for the new "round"
                            enableButtons();
                            System.out.println("buttons enabled");

                            //Exit switch statement
                            break;


                        case "Tie":
                            //Show opponent's move
                            showOpponentMove(strings[1]);
                            System.out.println("show move returned");
                            waitTime(3.0);

                            //Hide opponent's move
                            hideOpponentMove();
                            System.out.println("hide move returned");

                            //Reset user's stance to default
                            showDefaultStance();
                            SelectedMove = "";

                            //Enables buttons again for the new "round"
                            enableButtons();
                            System.out.println("enable");

                            //Exit switch statement
                            break;


                        case "GameWin":
                            //Displays game over popup
                            displayGameResult("You win!");
                            break;


                        case "GameLoss":
                            //Displays game over popup
                            displayGameResult("You lose.");
                            break;


                        case "OpponentLeft":
                            //Displays game over popup
                            displayGameResult("Opponent conceded.");
                            break;
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("CLOSE", "onClose() returned: " + reason);
                    System.out.println("onClose returned");
                }

                @Override
                public void onError(Exception ex) {
                    Log.d("Exception:", ex.getMessage().toString());

                }
            };
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        //Connect to the websocket
        WebSocket.connect();



        /**
         * Selects and displays "block" as the user's selected move.
         */
        BlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Sets currently selected move to block
                SelectedMove = "block";

                //Changes player1 icon to block pose
                Player1Kick.setVisibility(View.INVISIBLE);
                Player1Jab.setVisibility(View.INVISIBLE);

                Player1Block.setVisibility(View.VISIBLE);
            }
        });



        /**
         * Selects and displays "kick" as the user's selected move.
         */
        KickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Sets currently selected move to kick
                SelectedMove = "kick";

                //Changes player1 icon to kick pose
                Player1Block.setVisibility(View.INVISIBLE);
                Player1Jab.setVisibility(View.INVISIBLE);

                Player1Kick.setVisibility(View.VISIBLE);
            }
        });


        /**
         * Selects and displays "jab" as the user's selected move.
         */
        JabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Sets currently selected move to jab
                SelectedMove = "jab";

                //Changes player1 icon to jab pose
                Player1Block.setVisibility(View.INVISIBLE);
                Player1Kick.setVisibility(View.INVISIBLE);

                Player1Jab.setVisibility(View.VISIBLE);
            }
        });


        /**
         * Locks in the user's currently selected move and updates backend of the change.
         */
        ConfirmMoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Sends move to backend
                if (SelectedMove.equals("")) {
                    SelectedMove = "block";
                }
                WebSocket.send(SelectedMove);

                //Disables buttons until other user confirms
                disableButtons();
            }
        });


        /**
         * Opens the options menu and displays buttons to leave the game and close the menu.
         */
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

                        //Send the request we created
                        queue.add(HostGameReq);
                    }
                });
                //TODO ////////////////////////////////////DELETE WHEN DONE TESTING //////////////////////////////////////////

                OptionsLayout.addView(inflatedLayout);
            }
        });

    }


    /**
     * Makes buttons clickable and changes their color.
     */
    private void enableButtons() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
        });
    }


    /**
     * Makes buttons un-clickable and changes their color.
     */
    private void disableButtons() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
        });
    }


    /**
     * Displays opponent's username on the screen.
     */
    private void displayOpponentName() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //TODO Implement method that requests opponent's name and then displays it in textview?
            }
        });
    }


    /**
     * Shows opponent's move on screen.
     * @param move is a string containing the opponent's selected move.
     */
    private void showOpponentMove(String move) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Show opponent's move
                switch (move) {
                    case "kick":
                        //Hides block and shows kick
                        Player2Block.setVisibility(View.INVISIBLE);
                        Player2Kick.setVisibility(View.VISIBLE);
                        break;

                    case "jab":
                        //Hides block and shows jab
                        Player2Block.setVisibility(View.INVISIBLE);
                        Player2Jab.setVisibility(View.VISIBLE);
                        break;

                    default:
                        //Do nothing because default stance is block
                        break;
                }
            }
        });
    }


    /**
     * Hides opponent's move on screen and displays default block stance again.
     */
    private void hideOpponentMove() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Player2Kick.setVisibility(View.INVISIBLE);
                Player2Jab.setVisibility(View.INVISIBLE);
                Player2Block.setVisibility(View.VISIBLE);
            }
        });
    }


    /**
     * Displays user's current health on screen.
     * @param health is an int containing the user's current health.
     */
    private void lowerUserHealth(int health) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Changes image of Player1Heartx
                if (health == 5) {
                    UserHeart6.setImageResource(R.drawable.emptyheart);

                } else if (health == 4) {
                    UserHeart5.setImageResource(R.drawable.emptyheart);

                } else if (health == 3) {
                    UserHeart4.setImageResource(R.drawable.emptyheart);

                } else if (health == 2) {
                    UserHeart3.setImageResource(R.drawable.emptyheart);

                } else if (health == 1) {
                    UserHeart2.setImageResource(R.drawable.emptyheart);

                } else if (health == 0) {
                    UserHeart1.setImageResource(R.drawable.emptyheart);
                }
            }
        });
    }


    /**
     * Displays opponent's current health on screen.
     * @param health is an int containing the opponent's current health
     */
    private void lowerOpponentHealth(int health) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Changes image of Player1Heartx
                if (health == 5) {
                    OpponentHeart6.setImageResource(R.drawable.emptyheart);

                } else if (health == 4) {
                    OpponentHeart5.setImageResource(R.drawable.emptyheart);

                } else if (health == 3) {
                    OpponentHeart4.setImageResource(R.drawable.emptyheart);

                } else if (health == 2) {
                    OpponentHeart3.setImageResource(R.drawable.emptyheart);

                } else if (health == 1) {
                    OpponentHeart2.setImageResource(R.drawable.emptyheart);

                } else if (health == 0) {
                    OpponentHeart1.setImageResource(R.drawable.emptyheart);
                }
            }
        });
    }


    /**
     * Displays end of game overlay and appropriate game over prompt.
     * @param result is a string containing the information of why the game ended.
     */
    private void displayGameResult(String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Clears and displays game over overlay
                GameOverLayout.removeAllViews();
                GameOverLayout.setVisibility(View.VISIBLE);

                //Populates overlay with win text.
                View inflatedLayout = getLayoutInflater().inflate(R.layout.game_result_layout, null, false);
                TextView resultText = (TextView) inflatedLayout.findViewById(R.id.ResultText);
                Button BoxingToMenuBtn = (Button) inflatedLayout.findViewById(R.id.BoxingToMenuBtn);

                //Displays win message on screen
                resultText.setText(result);

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
            }
        });
    }


    /**
     * Changes user's player icon to the "block" stance.
     */
    private void showDefaultStance() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Player1Kick.setVisibility(View.INVISIBLE);
                Player1Jab.setVisibility(View.INVISIBLE);
                Player1Block.setVisibility(View.VISIBLE);
            }
        });
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
