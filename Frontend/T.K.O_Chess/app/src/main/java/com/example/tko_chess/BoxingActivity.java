package com.example.tko_chess;

import static android.text.TextUtils.split;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.PKIXRevocationChecker;

import com.example.tko_chess.ultils.Const;

/**
 * @author Lex Somers
 *
 * Boxing activity where users play against another user in a game of
 *     rock, papaer, scissors like "boxing".
 */
public class BoxingActivity extends AppCompatActivity {

    /**
     * ImageView showing player 1's "block" pose.
     */
    ImageView Player1Block;

    /**
     * ImageView showing player 1's "kick" pose.
     */
    ImageView Player1Kick;

    /**
     * ImageView showing player 1's "jab".
     */
    ImageView Player1Jab;

    /**
     * ImageView showing user's 6th heart.
     */
    ImageView UserHeart6;

    /**
     * ImageView showing user's 5th heart.
     */
    ImageView UserHeart5;

    /**
     * ImageView showing user's 4th heart.
     */
    ImageView UserHeart4;

    /**
     * ImageView showing user's 3rd heart.
     */
    ImageView UserHeart3;

    /**
     * ImageView showing user's 2nd heart.
     */
    ImageView UserHeart2;

    /**
     * ImageView showing user's 1st heart.
     */
    ImageView UserHeart1;

    /**
     * ImageView showing player 2's "block" pose.
     */
    ImageView Player2Block;

    /**
     * ImageView showing player 2's "kick" pose.
     */
    ImageView Player2Kick;

    /**
     * ImageView showing player 2's "jab".
     */
    ImageView Player2Jab;

    /**
     * ImageView showing user's 6th heart.
     */
    ImageView OpponentHeart6;

    /**
     * ImageView showing user's 5th heart.
     */
    ImageView OpponentHeart5;

    /**
     * ImageView showing user's 4th heart.
     */
    ImageView OpponentHeart4;

    /**
     * ImageView showing user's 3rd heart.
     */
    ImageView OpponentHeart3;

    /**
     * ImageView showing user's 2nd heart.
     */
    ImageView OpponentHeart2;

    /**
     * ImageView showing user's 1st heart.
     */
    ImageView OpponentHeart1;

    /**
     * Button changes user's character pose to "block".
     */
    Button BlockBtn;

    /**
     * Button changes user's character pose to "kick".
     */
    Button KickBtn;

    /**
     * Button changes user's character pose to "jab".
     */
    Button JabBtn;

    /**
     * Button locks in user's selected move.
     */
    Button ConfirmMoveBtn;

    /**
     * Button displays the options menu.
     */
    ImageButton OptionsBtn;

    /**
     * TextView displays boxing header.
     */
    TextView GameTimeText;

    /**
     * TextView displays player1's username.
     */
    TextView Player1Name;

    /**
     * Textview displays current round of boxing.
     */
    TextView CurrRound;

    /**
     * Textview displays Player1 total rounds won.
     */
    TextView Player1Wins;

    /**
     * Textview displays Player2 total rounds won.
     */
    TextView Player2Wins;

    /**
     * TextView displays player2's username.
     */
    TextView Player2Name;

    /**
     * TextView displays select move prompt.
     */
    TextView SelectMoveText;

    /**
     * Int holds user's current health.
     */
    int UserHealth = 3;

    /**
     * Int stores the current round of boxing.
     */
    int RoundNum;

    /**
     * Int stores how many rounds of boxing Player1 has won.
     */
    //int Player1GamesWon = 0;

    /**
     * Int stores how many rounds of boxing Player2 has won.
     */
    //int Player2GamesWon = 0;

    /**
     * Int holds opponent's current health.
     */
    int OpponentHealth = 3;

    /**
     * LinearLayout container for the options menu.
     */
    LinearLayout OptionsLayout;

    /**
     * LinearLayout container for the "game over" overlay.
     */
    LinearLayout GameOverLayout;

    /**
     * String holds user's currently selected move.
     */
    String SelectedMove = "";

    /**
     * String holds what gamemode a user is playing.
     * Gamemodes are chess, chessboxing, or boxing.
     */
    String GameMode;

    /**
     * String holds the user's player type.
     * Player type Player1, Player2, or Spectator.
     */
    String UserRole;
    String WhoPlayer1;
    String WhoPlayer2;

    /**
     * SingletonUser instance which stores the currently logged in user.
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
        Player1Name = findViewById(R.id.Player1NameText);
        Player2Name = findViewById(R.id.Player2NameText);
        SelectMoveText = findViewById(R.id.SelectMoveText);
        /*CurrRound = findViewById(R.id.RoundNumberText);
        Player1Wins = findViewById(R.id.Player1Wins);
        Player2Wins = findViewById(R.id.Player2Wins);*/

        //LinearLayout initializations
        OptionsLayout = findViewById(R.id.OptionsLayout);
        GameOverLayout = findViewById(R.id.GameOverLayout);

        //String initializations
        GameMode = getIntent().getExtras().getString("Gamemode");
        UserRole = getIntent().getExtras().getString("UserRole");
        WhoPlayer1 = getIntent().getExtras().getString("Player1");
        WhoPlayer2 = getIntent().getExtras().getString("Player2");

        /*//Int initializations
        Player1GamesWon = getIntent().getExtras().getInt("Player1Wins");
        Player2GamesWon = getIntent().getExtras().getInt("Player2Wins");
        RoundNum = getIntent().getExtras().getInt("RoundNumber");
        //Increments round number to current.
        RoundNum += 1;

        //Display round number
        CurrRound.setText("Round " + RoundNum);
         */

        //Hides excess starting hearts according to initial health
        displayStartingOpponentHealth();
        displayStartingUserHealth();

        //Hides buttons for spectators
        if (UserRole.equals("Spectator")) {
            hideButtons();
        }

        String URLConcatenation = "";
        URLConcatenation += currUser.getUsername();

        //Display player names on screen for spectators and for the case of user being Player 1
        if (UserRole.equals("Spectator") || WhoPlayer1.equals(currUser.getUsername())) {
            Player1Name.setText(WhoPlayer1);
            Player2Name.setText(WhoPlayer2);
            /*Player1Wins.setText("Wins: " + Integer.toString(Player1GamesWon));
            Player2Wins.setText("Wins: " + Integer.toString(Player2GamesWon));*/
        }

        //Display player names on screen for the case of user being Player 2
        if (WhoPlayer2.equals(currUser.getUsername())) {
            Player1Name.setText(currUser.getUsername());
            Player2Name.setText(WhoPlayer1);
            /*Player1Wins.setText("Wins: " + Integer.toString(Player2GamesWon));
            Player2Wins.setText("Wins: " + Integer.toString(Player1GamesWon));*/
        }

        /*if (!GameMode.equals("ChessBoxing")) {
            Player1Wins.setVisibility(View.INVISIBLE);
            Player2Wins.setVisibility(View.INVISIBLE);
            CurrRound.setVisibility(View.INVISIBLE);
        }*/

        Draft[] drafts = {
                new Draft_6455()
        };

        //Connect to WebSocket
        try {
            WebSocket = new WebSocketClient(new URI(Const.URL_SERVER_WEBSOCKETBOXING + URLConcatenation), (Draft)drafts[0]) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d("OPEN", "run() returned: " + "is connecting");
                    System.out.println("Boxing onOpen returned");

                    //Creates a boxing game with the correct players if in ChessBoxing
                    if (GameMode.equals("ChessBoxing") && UserRole.equals("Player1")) {
                        WebSocket.send("Start " + WhoPlayer1 + " " + WhoPlayer2);
                    }

                    if (UserRole.equals("Spectator")) {
                        WebSocket.send("Join " + WhoPlayer1 + " " + currUser.getUsername());
                    }
                }

                @Override
                public void onMessage(String message) {
                    Log.d("", "run() returned: " + message);
                    String[] strings = message.split(" ");

                    switch (strings[0]) {
                        //Player Switch Cases
                        ////////////////////////////////////////////////////////////////////
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
                            //Updates user's stats
                            WebSocket.send("GameType " + GameMode + " win");

                            //Closes websocket
                            WebSocket.close();

                            //Increments number of user game wins.
                            if (UserRole.equals("Player1")) {
                                displayGameResult("You win!");

                                /*Player1GamesWon += 1;

                                if ((Player1GamesWon >= 3) || (!GameMode.equals("ChessBoxing"))) {
                                    displayGameResult("You win!");
                                } else {
                                    //Returns user to Chess
                                    Intent intent = new Intent(BoxingActivity.this, ChessActivity.class);
                                    intent.putExtra("Gamemode", GameMode);
                                    intent.putExtra("UserRole", UserRole);
                                    intent.putExtra("RoundNumber", RoundNum);
                                    intent.putExtra("Player1Wins", Player1GamesWon);
                                    intent.putExtra("Player2Wins", Player2GamesWon);
                                    intent.putExtra("Player1", WhoPlayer1);
                                    intent.putExtra("Player2", WhoPlayer2);

                                    startActivity(intent);
                                } */
                            } else

                            if (UserRole.equals("Player2")) {
                                displayGameResult(WhoPlayer2 + " won!");

                                /*Player2GamesWon += 1;

                                if ((Player2GamesWon >= 3) || (!GameMode.equals("ChessBoxing"))) {
                                    displayGameResult(WhoPlayer2 + " won!");
                                } else {
                                    //Returns user to Chess
                                    Intent intent = new Intent(BoxingActivity.this, ChessActivity.class);
                                    intent.putExtra("Gamemode", GameMode);
                                    intent.putExtra("UserRole", UserRole);
                                    intent.putExtra("RoundNumber", RoundNum);
                                    intent.putExtra("Player1Wins", Player1GamesWon);
                                    intent.putExtra("Player2Wins", Player2GamesWon);

                                    startActivity(intent);
                                }*/
                            }

                            //Exit switch statement
                            break;


                        case "GameLoss":
                            //Updates user's stats
                            WebSocket.send("GameType " + GameMode + " loss");

                            //Closes websocket
                            WebSocket.close();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    OptionsBtn.setClickable(false);
                                }
                            });

                            //Increments number of opponent game wins.
                            if (UserRole.equals("Player1")) {
                                displayGameResult("You lost. :(");

                                /*Player2GamesWon += 1;

                                if ((Player2GamesWon >= 3) || (!GameMode.equals("ChessBoxing"))) {
                                    displayGameResult("You lost. :(");
                                } else {
                                    //Returns user to Chess
                                    Intent intent = new Intent(BoxingActivity.this, ChessActivity.class);
                                    intent.putExtra("Gamemode", "ChessBoxing");
                                    intent.putExtra("UserRole", UserRole);
                                    intent.putExtra("RoundNumber", RoundNum);
                                    intent.putExtra("Player1Wins", Player1GamesWon);
                                    intent.putExtra("Player2Wins", Player2GamesWon);

                                    startActivity(intent);
                                }*/
                            } else

                            if (UserRole.equals("Player2")) {
                                displayGameResult("You lost. :(");

                                /*Player1GamesWon += 1;

                                if ((Player1GamesWon >= 3) || (!GameMode.equals("ChessBoxing"))) {
                                    displayGameResult("You lost. :(");
                                } else {
                                    //Returns user to Chess
                                    Intent intent = new Intent(BoxingActivity.this, ChessActivity.class);
                                    intent.putExtra("Gamemode", "ChessBoxing");
                                    intent.putExtra("UserRole", UserRole);
                                    intent.putExtra("RoundNumber", RoundNum);
                                    intent.putExtra("Player1Wins", Player1GamesWon);
                                    intent.putExtra("Player2Wins", Player2GamesWon);

                                    startActivity(intent);
                                }*/
                            }

                            //Exit switch statement
                            break;


                        case "OpponentLeft":
                            //Closes websocket
                            WebSocket.close();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    OptionsBtn.setClickable(false);
                                }
                            });

                            //Displays game over popup
                            displayGameResult("Opponent conceded.");

                            //Exit switch statement
                            break;
                        ////////////////////////////////////////////////////////////////////



                        //Spectator Switch Cases
                        ////////////////////////////////////////////////////////////////////
                        case "RoundOver":
                            if (UserRole.equals("Spectator")) {
                                //If player 1 won the round
                                if (strings[1].equals("Player1")) {
                                    //Shows both players' moves
                                    showPlayer1Move(strings[2]);
                                    showOpponentMove(strings[4]);

                                    //Waits 3 seconds
                                    waitTime(3.0);

                                    //Lowers health of opponent
                                    OpponentHealth -= 1;
                                    lowerOpponentHealth(OpponentHealth);

                                    //Hides both players' moves
                                    showDefaultStance();
                                    hideOpponentMove();

                                } else

                                    //If player 2 won the round
                                    if (strings[1].equals("Player2")) {
                                        //Shows both players' moves
                                        showPlayer1Move(strings[4]);
                                        showOpponentMove(strings[2]);

                                        //Waits 3 seconds
                                        waitTime(3.0);

                                        //Lowers health of opponent
                                        UserHealth -= 1;
                                        lowerUserHealth(UserHealth);

                                        //Hides both players' moves
                                        showDefaultStance();
                                        hideOpponentMove();
                                    }
                            }

                            //Exit switch statement
                            break;


                        case "RoundTie":
                            if (UserRole.equals("Spectator")) {
                                //Shows both players' moves
                                showPlayer1Move(strings[1]);
                                showOpponentMove(strings[1]);

                                //Waits 3 seconds
                                waitTime(3.0);

                                //Hides both players' moves
                                showDefaultStance();
                                hideOpponentMove();
                            }

                            //Exit switch statement
                            break;


                        case "GameOver":
                            if (UserRole.equals("Spectator")) {
                                WebSocket.close();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        OptionsBtn.setClickable(false);
                                    }
                                });

                                //Displays game result layout and which player won.
                                if (strings[1].equals("Player1")) {
                                    //Player1GamesWon += 1;

                                    displayGameResult(WhoPlayer1 + " won!");

                                    /*if ((Player1GamesWon >= 3) || (!GameMode.equals("ChessBoxing"))) {
                                        displayGameResult(WhoPlayer1 + " won!");
                                    } else {
                                        //Returns user to Chess
                                        Intent intent = new Intent(BoxingActivity.this, ChessActivity.class);
                                        intent.putExtra("Gamemode", "ChessBoxing");
                                        intent.putExtra("RoundNumber", RoundNum);
                                        intent.putExtra("Player1Wins", Player1GamesWon);
                                        intent.putExtra("Player2Wins", Player2GamesWon);

                                        startActivity(intent);
                                    }*/
                                } else

                                if (strings[1].equals("Player2")) {
                                    displayGameResult(WhoPlayer2 + " won!");

                                    /*Player2GamesWon += 1;

                                    if ((Player2GamesWon >= 3) || (!GameMode.equals("ChessBoxing"))) {
                                        displayGameResult(WhoPlayer2 + " won!");
                                    } else {
                                        //Returns user to Chess
                                        Intent intent = new Intent(BoxingActivity.this, ChessActivity.class);
                                        intent.putExtra("Gamemode", "ChessBoxing");
                                        intent.putExtra("RoundNumber", RoundNum);
                                        intent.putExtra("Player1Wins", Player1GamesWon);
                                        intent.putExtra("Player2Wins", Player2GamesWon);

                                        startActivity(intent);
                                    } */
                                }
                            }

                            //Exit switch statement
                            break;


                        case "PlayerLeft":
                            if (UserRole.equals("Spectator")) {
                                WebSocket.close();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        OptionsBtn.setClickable(false);
                                    }
                                });

                                displayGameResult("A player has left the game.");
                            }

                            //Exit switch statement
                            break;
                        ////////////////////////////////////////////////////////////////////
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("CLOSE", "onClose() returned: " + reason);
                    System.out.println("Boxing onClose returned");
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
                System.out.println("Sent " + SelectedMove);

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
                View inflatedLayout = getLayoutInflater().inflate(R.layout.game_menu_layout, null, false);
                Button LeaveGameBtn = (Button) inflatedLayout.findViewById(R.id.LeaveBoxingBtn);
                Button CloseOptionsBtn = (Button) inflatedLayout.findViewById(R.id.BackToGameBtn);
                TextView LeaveGameText = (TextView) inflatedLayout.findViewById(R.id.LeaveBoxingText);

                //Set leave game prompt depending on UserRole
                if (UserRole.equals("Spectator")) {
                    LeaveGameText.setText("Stop watching?");
                } else

                if (UserRole.equals("Player1") || UserRole.equals("Player2")) {
                    LeaveGameText.setText("Concede?");
                }

                //Leaves game and returns user to main menu
                LeaveGameBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Send leave message if user was a player
                        if (UserRole.equals("Player1") || UserRole.equals("Player2")) {
                            WebSocket.send("leave");
                        }

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
                //Enables buttons if user is a player.
                if (UserRole.equals("Player1") || (UserRole.equals("Player2"))) {
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
            }
        });
    }


    /**
     * Hides all buttons from view of spectator.
     */
    private void hideButtons() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Hides buttons from view
                BlockBtn.setVisibility(View.INVISIBLE);
                KickBtn.setVisibility(View.INVISIBLE);
                JabBtn.setVisibility(View.INVISIBLE);
                ConfirmMoveBtn.setVisibility(View.INVISIBLE);
                SelectMoveText.setVisibility(View.INVISIBLE);

                //Makes buttons un-clickable.
                BlockBtn.setClickable(false);
                KickBtn.setClickable(false);
                JabBtn.setClickable(false);
                ConfirmMoveBtn.setClickable(false);
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
                //disables buttons if user is a player.
                if (UserRole.equals("Player1") || (UserRole.equals("Player2"))) {
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
     * Shows player 1's move on screen.
     * @param move
     */
    private void showPlayer1Move(String move) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Show opponent's move
                switch (move) {
                    case "kick":
                        //Hides block and shows kick
                        Player1Block.setVisibility(View.INVISIBLE);
                        Player1Kick.setVisibility(View.VISIBLE);
                        break;

                    case "jab":
                        //Hides block and shows jab
                        Player1Block.setVisibility(View.INVISIBLE);
                        Player1Jab.setVisibility(View.VISIBLE);
                        break;

                    default:
                        //Do nothing because default stance is block
                        break;
                }
            }
        });
    }



    //Displays user's health on screen
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
                Button BoxingToMenuBtn = (Button) inflatedLayout.findViewById(R.id.ReturnToMenuBtn);

                //Displays win message on screen
                resultText.setText(result);

                GameOverLayout.addView(inflatedLayout);

                BoxingToMenuBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
     * Displays a number of hearts equal the the players' starting health
     */
    private void displayStartingUserHealth() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (UserHealth == 5) {
                    UserHeart6.setVisibility(View.INVISIBLE);
                } else

                if (UserHealth == 4) {
                    UserHeart6.setVisibility(View.INVISIBLE);
                    UserHeart5.setVisibility(View.INVISIBLE);
                } else

                if (UserHealth == 3) {
                    UserHeart6.setVisibility(View.INVISIBLE);
                    UserHeart5.setVisibility(View.INVISIBLE);
                    UserHeart4.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    /**
     * Displays a number of hearts equal the the players' starting health
     */
    private void displayStartingOpponentHealth() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (OpponentHealth == 5) {
                    OpponentHeart6.setVisibility(View.INVISIBLE);
                } else

                if (UserHealth == 4) {
                    OpponentHeart6.setVisibility(View.INVISIBLE);
                    OpponentHeart5.setVisibility(View.INVISIBLE);
                } else

                if (UserHealth == 3) {
                    OpponentHeart6.setVisibility(View.INVISIBLE);
                    OpponentHeart5.setVisibility(View.INVISIBLE);
                    OpponentHeart4.setVisibility(View.INVISIBLE);
                }
            }
        });
    }



    //Wait time seconds
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
