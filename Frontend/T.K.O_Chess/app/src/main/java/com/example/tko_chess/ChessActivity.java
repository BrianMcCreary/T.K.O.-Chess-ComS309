package com.example.tko_chess;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.net.URISyntaxException;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tko_chess.ultils.Const;

/**
 * @author Zachary Scurlock, Lex Somers
 * This is where the real meat and potatoes of chess can be found
 */
public class ChessActivity extends AppCompatActivity {

    /**
     * Declarations of every tile of the chess board
     */
    ImageButton A1, B1, C1, D1, E1, F1, G1, H1,
                A2, B2, C2, D2, E2, F2, G2, H2,
                A3, B3, C3, D3, E3, F3, G3, H3,
                A4, B4, C4, D4, E4, F4, G4, H4,
                A5, B5, C5, D5, E5, F5, G5, H5,
                A6, B6, C6, D6, E6, F6, G6, H6,
                A7, B7, C7, D7, E7, F7, G7, H7,
                A8, B8, C8, D8, E8, F8, G8, H8;
    ImageButton OptionsBtn;
    LinearLayout GameOverLayout;
    LinearLayout OptionsLayout;
    LinearLayout PromotionLayout;
    String UserRole;
    String WhoPlayer1;
    String WhoPlayer2;
    String GameMode;
    TextView WhoseMove;
    TextView movesLeftText;

    int Player1GamesWon = 0; // Int stores how many rounds of boxing Player1 has won.
    int Player2GamesWon = 0; // Int stores how many rounds of boxing Player2 has won.
    private WebSocketClient WebSocket; // Websocket Variable used for Websocket connection

    String Username = ""; // Used to append changes to the end of the URL
    SingletonUser currUser = SingletonUser.getInstance(); // used to get the user's name
    String tile = ""; // Stores the name of the pressed tile
    String piece = ""; // stores the name of the selected piece
    int tracker = 0; // Keeps track of how many times a user has clicked a tile
    int movesLeft = 8; // tracks if how many moves the player has left

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     *  Loads the chess screen and handles the movesLeft of the user
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);

        /*
         * Assigns the image buttons to their proper ID
         */
        A1 = findViewById(R.id.A1); B1 = findViewById(R.id.B1); C1 = findViewById(R.id.C1); D1 = findViewById(R.id.D1); E1 = findViewById(R.id.E1); F1 = findViewById(R.id.F1); G1 = findViewById(R.id.G1); H1 = findViewById(R.id.H1);

        A2 = findViewById(R.id.A2); B2 = findViewById(R.id.B2); C2 = findViewById(R.id.C2); D2 = findViewById(R.id.D2); E2 = findViewById(R.id.E2); F2 = findViewById(R.id.F2); G2 = findViewById(R.id.G2); H2 = findViewById(R.id.H2);

        A3 = findViewById(R.id.A3); B3 = findViewById(R.id.B3); C3 = findViewById(R.id.C3); D3 = findViewById(R.id.D3); E3 = findViewById(R.id.E3); F3 = findViewById(R.id.F3); G3 = findViewById(R.id.G3); H3 = findViewById(R.id.H3);

        A4 = findViewById(R.id.A4); B4 = findViewById(R.id.B4); C4 = findViewById(R.id.C4); D4 = findViewById(R.id.D4); E4 = findViewById(R.id.E4); F4 = findViewById(R.id.F4); G4 = findViewById(R.id.G4); H4 = findViewById(R.id.H4);

        A5 = findViewById(R.id.A5); B5 = findViewById(R.id.B5); C5 = findViewById(R.id.C5); D5 = findViewById(R.id.D5); E5 = findViewById(R.id.E5); F5 = findViewById(R.id.F5); G5 = findViewById(R.id.G5); H5 = findViewById(R.id.H5);

        A6 = findViewById(R.id.A6); B6 = findViewById(R.id.B6); C6 = findViewById(R.id.C6); D6 = findViewById(R.id.D6); E6 = findViewById(R.id.E6); F6 = findViewById(R.id.F6); G6 = findViewById(R.id.G6); H6 = findViewById(R.id.H6);

        A7 = findViewById(R.id.A7); B7 = findViewById(R.id.B7); C7 = findViewById(R.id.C7); D7 = findViewById(R.id.D7); E7 = findViewById(R.id.E7); F7 = findViewById(R.id.F7); G7 = findViewById(R.id.G7); H7 = findViewById(R.id.H7);

        A8 = findViewById(R.id.A8); B8 = findViewById(R.id.B8); C8 = findViewById(R.id.C8); D8 = findViewById(R.id.D8); E8 = findViewById(R.id.E8); F8 = findViewById(R.id.F8); G8 = findViewById(R.id.G8); H8 = findViewById(R.id.H8);

        OptionsBtn = findViewById(R.id.ChessMenuBtn);
        OptionsLayout = findViewById(R.id.OptionsLayout2);
        GameOverLayout = findViewById(R.id.GameOverLayout2);
//        PromotionLayout = findViewById(R.id.linearLayout);

        movesLeftText = findViewById(R.id.MovesLeftText);
        WhoseMove = findViewById(R.id.whoseMoveText);

        GameMode = getIntent().getExtras().getString("Gamemode");
        UserRole = getIntent().getExtras().getString("UserRole");
        WhoPlayer1 = getIntent().getExtras().getString("Player1");
        WhoPlayer2 = getIntent().getExtras().getString("Player2");

        Player1GamesWon = getIntent().getExtras().getInt("Player1Wins");
        Player2GamesWon = getIntent().getExtras().getInt("Player2Wins");

        if(GameMode.equals("ChessBoxing")){
            movesLeftText.setText("Moves Left: " + movesLeft);
        }
        if(!GameMode.equals("ChessBoxing")){
            movesLeftText.setVisibility(View.INVISIBLE);
        }
        if(UserRole.equals("Player2")){
            disableButtons();
        }

        Draft[] drafts = {new Draft_6455()};

        Username = currUser.getUsername(); // Sets Username equal to the current user's name

        try{
            WebSocket = new WebSocketClient(new URI(Const.URL_CHESS_WEBSOCKET + Username), (Draft)drafts[0] ) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    Log.d("OPEN", "run() returned: " + "is connecting");
                    System.out.println("onOpen returned");
                }
                @Override
                public void onMessage(String message) {
                    Log.d("", "run() returned: " + message);
                    String[] strings = message.split(" ");

                    switch (strings[0]){

                        case "Player1Moved":
                            if(UserRole.equals("Spectator") || UserRole.equals("Player1")){
                                setTransparent(strings[2]);
                                movePiece(strings[3], strings[1]);
                                disableButtons();
                            }
                            else if(UserRole.equals("Player2")){
                                setTransparent(strings[2]);
                                movePiece(strings[3], strings[1]);
                                enableButtons();
                            }

                            displayWhoseMove(strings[0]);

                            break;

                        case "Player2Moved":

                            movesLeft--;

                            if(GameMode.equals("ChessBoxing")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        movesLeftText.setText("Moves Left: " + movesLeft);
                                    }
                                });
                            }

                            if(UserRole.equals("Player1")){
                                setTransparent(strings[2]);
                                movePiece(strings[3], strings[1]);
                                enableButtons();
                            }
                            else if(UserRole.equals("Player2")){
                                setTransparent(strings[2]);
                                movePiece(strings[3], strings[1]);
                                disableButtons();
                            }
                            else if(UserRole.equals("Spectator")){
                                setTransparent(strings[2]);
                                movePiece(strings[3],strings[1]);
                            }

                            displayWhoseMove(strings[0]);

                            if((movesLeft == 0) && GameMode.equals("ChessBoxing")){
                                Intent intent = new Intent(ChessActivity.this, BoxingActivity.class);
                                intent.putExtra("Gamemode", GameMode);
                                intent.putExtra("UserRole", UserRole);
                                intent.putExtra("Player1", WhoPlayer1);
                                intent.putExtra("Player2", WhoPlayer2);

                                movesLeft = 40;

                                startActivity(intent);
                            }
                            break;

                        case "blackPawn":
                            piece = "blackPawn"; //sets piece to a black pawn if it is the piece being moved
                            break;

                        case "blackRook":
                            piece = "blackRook"; //sets piece to a black rook if it is the piece being moved
                            break;

                        case "blackKnight":
                            piece = "blackKnight"; //sets piece to a black knight if it is the piece being moved
                            break;

                        case "blackBishop":
                            piece = "blackBishop"; //sets piece to a black bishop if it is the piece being moved
                            break;

                        case "blackKing":
                            piece = "blackKing"; //sets piece to a black king if it is the piece being moved
                            break;

                        case "blackQueen":
                            piece = "blackQueen"; //sets piece to a black queen if it is the piece being moved
                            break;

                        case "whitePawn":
                            piece = "whitePawn"; //sets piece to a white pawn if it is the piece being moved
                            break;

                        case "whiteRook":
                            piece = "whiteRook"; //sets piece to a white rook if it is the piece being moved
                            break;

                        case "whiteKnight":
                            piece = "whiteKnight"; //sets piece to a white knight if it is the piece being moved
                            break;

                        case "whiteBishop":
                            piece = "whiteBishop"; //sets piece to a white bishop if it is the piece being moved
                            break;

                        case "whiteKing":
                            piece = "whiteKing"; //sets piece to a white king if it is the piece being moved
                            break;

                        case "whiteQueen":
                            piece = "whiteQueen"; //sets piece to a white queen if it is the piece being moved
                            break;

                        case "GameWonBy":
                            //Disables all other buttons outside of return to main menu button.
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    OptionsLayout.removeAllViews();
                                    OptionsBtn.setClickable(false);
                                    disableButtons();
                                }
                            });

                            //If user is the player who won the game
                            if (strings[1].equals(currUser.getUsername())) {
                                WebSocket.close();
                                displayGameResult("You won!");
                            } else

                                //If user is a player and they lost the game
                                if (!strings[1].equals(currUser.getUsername()) && (UserRole.equals("Player1") || UserRole.equals("Player2"))) {
                                    WebSocket.send("GameType " + GameMode + " loss");
                                    WebSocket.close();

                                    displayGameResult("You lost!");
                                } else

                                    //If user is a spectator
                                    if (UserRole.equals("Spectator")) {
                                        WebSocket.close();
                                        displayGameResult(strings[1] + " won!");
                                    }
                            break;


                        case "OpponentLeft":
                            WebSocket.close();
                            displayGameResult("Opponent Conceded.");
                            disableButtons();
                            break;

                        case "Promotion":

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
                    System.out.println(ex);
                }
            };
        } catch (URISyntaxException e){
            e.printStackTrace();
            return;
        }

        WebSocket.connect(); // Connects to websocket

        OptionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View inflatedLayout = getLayoutInflater().inflate(R.layout.game_menu_layout_chesstemp, null, false);
                Button LeaveGameBtn = (Button) inflatedLayout.findViewById(R.id.LeaveBoxingBtn);
                Button CloseOptionsBtn = (Button) inflatedLayout.findViewById(R.id.BackToGameBtn);
                Button EndGameBtn = (Button) inflatedLayout.findViewById(R.id.EndGameBtn);
                TextView LeaveGameText = (TextView) inflatedLayout.findViewById(R.id.LeaveBoxingText);

                //Set leave game prompt depending on UserRole
                if (UserRole.equals("Spectator")) {
                    LeaveGameText.setText("Stop watching?");
                    disableButtons();
                }

                else if (UserRole.equals("Player1") || UserRole.equals("Player2")) {
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
                        Intent intent = new Intent(ChessActivity.this, MainMenuActivity.class);
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

                if(!UserRole.equals("Spectator")){
                    EndGameBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            WebSocket.send("GameType " + GameMode + " win");
                        }
                    });
                }

                OptionsLayout.addView(inflatedLayout);
            }
        });

        /*
         * A1 through H8 all have nearly identical internals with the only difference being tile
         * If it is the first time the user has selected a tile, the tile string is set to the name of the tile.
         * If it is the second time the user has selected a tile, the previously selected tile is set to be transparent,
         * and the second tile pressed is updated with the piece from the previous tile
         */
        A1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "A1";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "A1";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        A2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "A2";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "A2";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        A3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if((tracker == 1)){
                    tile = "A3";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "A3";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        A4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "A4";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "A4";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        A5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "A5";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "A5";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        A6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "A6";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "A6";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        A7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "A7";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "A7";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        A8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "A8";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "A8";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "B1";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "B1";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "B2";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "B2";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        B3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "B3";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "B3";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        B4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "B4";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "B4";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        B5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "B5";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "B5";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        B6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "B6";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "B6";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        B7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "B7";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "B7";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        B8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "B8";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "B8";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        C1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "C1";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "C1";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        C2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "C2";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "C2";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        C3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "C3";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "C3";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        C4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "C4";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "C4";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        C5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "C5";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "C5";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        C6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "C6";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "C6";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        C7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "C7";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "C7";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        C8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "C8";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "C8";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        D1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "D1";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "D1";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        D2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "D2";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "D2";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        D3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "D3";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "D3";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        D4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "D4";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "D4";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        D5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "D5";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "D5";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        D6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "D6";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "D6";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        D7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "D7";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "D7";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        D8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "D8";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "D8";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        E1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "E1";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "E1";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        E2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "E2";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "E2";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        E3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "E3";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "E3";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        E4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "E4";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "E4";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        E5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "E5";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "E5";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        E6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "E6";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "E6";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        E7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "E7";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "E7";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        E8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "E8";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "E8";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        F1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "F1";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "F1";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        F2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "F2";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "F2";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        F3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "F3";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "F3";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        F4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "F4";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "F4";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        F5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "F5";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "F5";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        F6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "F6";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "F6";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        F7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "F7";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "F7";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        F8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "F8";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "F8";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        G1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "G1";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "G1";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        G2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "G2";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "G2";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        G3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "G3";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "G3";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        G4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "G4";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "G4";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        G5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "G5";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "G5";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        G6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "G6";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "G6";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        G7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "G7";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "G7";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        G8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "G8";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "G8";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        H1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "H1";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "H1";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        H2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "H2";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "H2";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        H3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "H3";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "H3";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        H4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "H4";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "H4";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        H5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "H5";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "H5";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        H6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "H6";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "H6";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        H7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "H7";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "H7";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
        H8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker += 1;
                if(tracker == 1){
                    tile = "H8";
                    WebSocket.send(tile);
                    highlight();
                } else if(tracker > 1){
                    setTransparent(tile);
                    unhighlight();
                    tile = "H8";
                    WebSocket.send(tile);
                    movePiece(tile, piece);
                    tracker = 0;
                }
            }
        });
    }

    /**
     * Highlights selected tile
     */
    public void highlight(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(tile.equals("A1")){
                    A1.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("A2")){
                    A2.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("A3")){
                    A3.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("A4")){
                    A4.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("A5")){
                    A5.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("A6")){
                    A6.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("A7")){
                    A7.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("A8")){
                    A8.setBackgroundColor(Color.GREEN);
                }

                if(tile.equals("B1")){
                    B1.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("B2")){
                    B2.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("B3")){
                    B3.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("B4")){
                    B4.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("B5")){
                    B5.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("B6")){
                    B6.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("B7")){
                    B7.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("B8")){
                    B8.setBackgroundColor(Color.GREEN);
                }

                if(tile.equals("C1")){
                    C1.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("C2")){
                    C2.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("C3")){
                    C3.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("C4")){
                    C4.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("C5")){
                    C5.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("C6")){
                    C6.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("C7")){
                    C7.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("C8")){
                    C8.setBackgroundColor(Color.GREEN);
                }

                if(tile.equals("D1")){
                    D1.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("D2")){
                    D2.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("D3")){
                    D3.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("D4")){
                    D4.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("D5")){
                    D5.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("D6")){
                    D6.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("D7")){
                    D7.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("D8")){
                    D8.setBackgroundColor(Color.GREEN);
                }

                if(tile.equals("E1")){
                    E1.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("E2")){
                    E2.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("E3")){
                    E3.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("E4")){
                    E4.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("E5")){
                    E5.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("E6")){
                    E6.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("E7")){
                    E7.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("E8")){
                    E8.setBackgroundColor(Color.GREEN);
                }

                if(tile.equals("F1")){
                    F1.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("F2")){
                    F2.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("F3")){
                    F3.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("F4")){
                    F4.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("F5")){
                    F5.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("F6")){
                    F6.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("F7")){
                    F7.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("F8")){
                    F8.setBackgroundColor(Color.GREEN);
                }

                if(tile.equals("G1")){
                    G1.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("G2")){
                    G2.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("G3")){
                    G3.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("G4")){
                    G4.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("G5")){
                    G5.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("G6")){
                    G6.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("G7")){
                    G7.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("G8")){
                    G8.setBackgroundColor(Color.GREEN);
                }

                if(tile.equals("H1")){
                    H1.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("H2")){
                    H2.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("H3")){
                    H3.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("H4")){
                    H4.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("H5")){
                    H5.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("H6")){
                    H6.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("H7")){
                    H7.setBackgroundColor(Color.GREEN);
                }
                if(tile.equals("H8")){
                    H8.setBackgroundColor(Color.GREEN);
                }
            }
        });
    }

    /**
     * Removes highlight
     */
    public void unhighlight(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(tile.equals("A1")){
                    A1.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("A2")){
                    A2.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("A3")){
                    A3.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("A4")){
                    A4.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("A5")){
                    A5.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("A6")){
                    A6.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("A7")){
                    A7.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("A8")){
                    A8.setBackgroundColor(Color.TRANSPARENT);
                }

                if(tile.equals("B1")){
                    B1.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("B2")){
                    B2.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("B3")){
                    B3.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("B4")){
                    B4.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("B5")){
                    B5.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("B6")){
                    B6.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("B7")){
                    B7.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("B8")){
                    B8.setBackgroundColor(Color.TRANSPARENT);
                }

                if(tile.equals("C1")){
                    C1.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("C2")){
                    C2.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("C3")){
                    C3.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("C4")){
                    C4.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("C5")){
                    C5.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("C6")){
                    C6.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("C7")){
                    C7.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("C8")){
                    C8.setBackgroundColor(Color.TRANSPARENT);
                }

                if(tile.equals("D1")){
                    D1.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("D2")){
                    D2.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("D3")){
                    D3.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("D4")){
                    D4.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("D5")){
                    D5.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("D6")){
                    D6.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("D7")){
                    D7.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("D8")){
                    D8.setBackgroundColor(Color.TRANSPARENT);
                }

                if(tile.equals("E1")){
                    E1.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("E2")){
                    E2.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("E3")){
                    E3.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("E4")){
                    E4.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("E5")){
                    E5.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("E6")){
                    E6.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("E7")){
                    E7.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("E8")){
                    E8.setBackgroundColor(Color.TRANSPARENT);
                }

                if(tile.equals("F1")){
                    F1.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("F2")){
                    F2.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("F3")){
                    F3.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("F4")){
                    F4.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("F5")){
                    F5.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("F6")){
                    F6.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("F7")){
                    F7.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("F8")){
                    F8.setBackgroundColor(Color.TRANSPARENT);
                }

                if(tile.equals("G1")){
                    G1.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("G2")){
                    G2.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("G3")){
                    G3.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("G4")){
                    G4.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("G5")){
                    G5.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("G6")){
                    G6.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("G7")){
                    G7.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("G8")){
                    G8.setBackgroundColor(Color.TRANSPARENT);
                }

                if(tile.equals("H1")){
                    H1.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("H2")){
                    H2.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("H3")){
                    H3.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("H4")){
                    H4.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("H5")){
                    H5.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("H6")){
                    H6.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("H7")){
                    H7.setBackgroundColor(Color.TRANSPARENT);
                }
                if(tile.equals("H8")){
                    H8.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });
    }

    /**
     * Enables buttons for the user
     */
    public void enableButtons() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                A1.setClickable(true);
                B1.setClickable(true);
                C1.setClickable(true);
                D1.setClickable(true);
                E1.setClickable(true);
                F1.setClickable(true);
                G1.setClickable(true);
                H1.setClickable(true);

                A2.setClickable(true);
                B2.setClickable(true);
                C2.setClickable(true);
                D2.setClickable(true);
                E2.setClickable(true);
                F2.setClickable(true);
                G2.setClickable(true);
                H2.setClickable(true);

                A3.setClickable(true);
                B3.setClickable(true);
                C3.setClickable(true);
                D3.setClickable(true);
                E3.setClickable(true);
                F3.setClickable(true);
                G3.setClickable(true);
                H3.setClickable(true);

                A4.setClickable(true);
                B4.setClickable(true);
                C4.setClickable(true);
                D4.setClickable(true);
                E4.setClickable(true);
                F4.setClickable(true);
                G4.setClickable(true);
                H4.setClickable(true);

                A5.setClickable(true);
                B5.setClickable(true);
                C5.setClickable(true);
                D5.setClickable(true);
                E5.setClickable(true);
                F5.setClickable(true);
                G5.setClickable(true);
                H5.setClickable(true);

                A6.setClickable(true);
                B6.setClickable(true);
                C6.setClickable(true);
                D6.setClickable(true);
                E6.setClickable(true);
                F6.setClickable(true);
                G6.setClickable(true);
                H6.setClickable(true);

                A7.setClickable(true);
                B7.setClickable(true);
                C7.setClickable(true);
                D7.setClickable(true);
                E7.setClickable(true);
                F7.setClickable(true);
                G7.setClickable(true);
                H7.setClickable(true);

                A8.setClickable(true);
                B8.setClickable(true);
                C8.setClickable(true);
                D8.setClickable(true);
                E8.setClickable(true);
                F8.setClickable(true);
                G8.setClickable(true);
                H8.setClickable(true);
            }
        });
    }

    /**
       Disables buttons for the user
     */
    public void disableButtons(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                A1.setClickable(false);
                B1.setClickable(false);
                C1.setClickable(false);
                D1.setClickable(false);
                E1.setClickable(false);
                F1.setClickable(false);
                G1.setClickable(false);
                H1.setClickable(false);

                A2.setClickable(false);
                B2.setClickable(false);
                C2.setClickable(false);
                D2.setClickable(false);
                E2.setClickable(false);
                F2.setClickable(false);
                G2.setClickable(false);
                H2.setClickable(false);

                A3.setClickable(false);
                B3.setClickable(false);
                C3.setClickable(false);
                D3.setClickable(false);
                E3.setClickable(false);
                F3.setClickable(false);
                G3.setClickable(false);
                H3.setClickable(false);

                A4.setClickable(false);
                B4.setClickable(false);
                C4.setClickable(false);
                D4.setClickable(false);
                E4.setClickable(false);
                F4.setClickable(false);
                G4.setClickable(false);
                H4.setClickable(false);

                A5.setClickable(false);
                B5.setClickable(false);
                C5.setClickable(false);
                D5.setClickable(false);
                E5.setClickable(false);
                F5.setClickable(false);
                G5.setClickable(false);
                H5.setClickable(false);

                A6.setClickable(false);
                B6.setClickable(false);
                C6.setClickable(false);
                D6.setClickable(false);
                E6.setClickable(false);
                F6.setClickable(false);
                G6.setClickable(false);
                H6.setClickable(false);

                A7.setClickable(false);
                B7.setClickable(false);
                C7.setClickable(false);
                D7.setClickable(false);
                E7.setClickable(false);
                F7.setClickable(false);
                G7.setClickable(false);
                H7.setClickable(false);

                A8.setClickable(false);
                B8.setClickable(false);
                C8.setClickable(false);
                D8.setClickable(false);
                E8.setClickable(false);
                F8.setClickable(false);
                G8.setClickable(false);
                H8.setClickable(false);
            }
        });
    }

    /**
     * This method sets the previous tile clicked become transparent
     */
    public void setTransparent(String tile){
        runOnUiThread(new Runnable() {
            @Override
            public void run(){

                if(tile.equals("A1")){
                    A1.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("A2")){
                    A2.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("A3")){
                    A3.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("A4")){
                    A4.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("A5")){
                    A5.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("A6")){
                    A6.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("A7")){
                    A7.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("A8")){
                    A8.setImageResource(R.drawable.transparent);
                }

                if(tile.equals("B1")){
                    B1.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("B2")){
                    B2.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("B3")){
                    B3.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("B4")){
                    B4.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("B5")){
                    B5.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("B6")){
                    B6.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("B7")){
                    B7.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("B8")){
                    B8.setImageResource(R.drawable.transparent);
                }

                if(tile.equals("C1")){
                    C1.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("C2")){
                    C2.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("C3")){
                    C3.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("C4")){
                    C4.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("C5")){
                    C5.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("C6")){
                    C6.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("C7")){
                    C7.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("C8")){
                    C8.setImageResource(R.drawable.transparent);
                }

                if(tile.equals("D1")){
                    D1.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("D2")){
                    D2.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("D3")){
                    D3.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("D4")){
                    D4.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("D5")){
                    D5.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("D6")){
                    D6.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("D7")){
                    D7.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("D8")){
                    D8.setImageResource(R.drawable.transparent);
                }

                if(tile.equals("E1")){
                    E1.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("E2")){
                    E2.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("E3")){
                    E3.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("E4")){
                    E4.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("E5")){
                    E5.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("E6")){
                    E6.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("E7")){
                    E7.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("E8")){
                    E8.setImageResource(R.drawable.transparent);
                }

                if(tile.equals("F1")){
                    F1.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("F2")){
                    F2.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("F3")){
                    F3.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("F4")){
                    F4.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("F5")){
                    F5.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("F6")){
                    F6.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("F7")){
                    F7.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("F8")){
                    F8.setImageResource(R.drawable.transparent);
                }

                if(tile.equals("G1")){
                    G1.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("G2")){
                    G2.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("G3")){
                    G3.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("G4")){
                    G4.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("G5")){
                    G5.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("G6")){
                    G6.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("G7")){
                    G7.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("G8")){
                    G8.setImageResource(R.drawable.transparent);
                }

                if(tile.equals("H1")){
                    H1.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("H2")){
                    H2.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("H3")){
                    H3.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("H4")){
                    H4.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("H5")){
                    H5.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("H6")){
                    H6.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("H7")){
                    H7.setImageResource(R.drawable.transparent);
                }
                if(tile.equals("H8")){
                    H8.setImageResource(R.drawable.transparent);
                }
            }
        });
    }

    /**
     * This method updates the second tile pressed with the piece from the previously selected tile
     */
    public void movePiece(String tile, String piece) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(piece.equals("blackPawn")){
                    if(tile.equals("A1")){
                        A1.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("A2")){
                        A2.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("A3")){
                        A3.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("A4")){
                        A4.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("A5")){
                        A5.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("A6")){
                        A6.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("A7")){
                        A7.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("A8")){
                        A8.setImageResource(R.drawable.black_pawn);
                    }

                    if(tile.equals("B1")){
                        B1.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("B2")){
                        B2.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("B3")){
                        B3.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("B4")){
                        B4.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("B5")){
                        B5.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("B6")){
                        B6.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("B7")){
                        B7.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("B8")){
                        B8.setImageResource(R.drawable.black_pawn);
                    }

                    if(tile.equals("C1")){
                        C1.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("C2")){
                        C2.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("C3")){
                        C3.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("C4")){
                        C4.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("C5")){
                        C5.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("C6")){
                        C6.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("C7")){
                        C7.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("C8")){
                        C8.setImageResource(R.drawable.black_pawn);
                    }

                    if(tile.equals("D1")){
                        D1.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("D2")){
                        D2.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("D3")){
                        D3.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("D4")){
                        D4.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("D5")){
                        D5.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("D6")){
                        D6.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("D7")){
                        D7.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("D8")){
                        D8.setImageResource(R.drawable.black_pawn);
                    }

                    if(tile.equals("E1")){
                        E1.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("E2")){
                        E2.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("E3")){
                        E3.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("E4")){
                        E4.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("E5")){
                        E5.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("E6")){
                        E6.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("E7")){
                        E7.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("E8")){
                        E8.setImageResource(R.drawable.black_pawn);
                    }

                    if(tile.equals("F1")){
                        F1.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("F2")){
                        F2.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("F3")){
                        F3.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("F4")){
                        F4.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("F5")){
                        F5.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("F6")){
                        F6.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("F7")){
                        F7.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("F8")){
                        F8.setImageResource(R.drawable.black_pawn);
                    }

                    if(tile.equals("G1")){
                        G1.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("G2")){
                        G2.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("G3")){
                        G3.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("G4")){
                        G4.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("G5")){
                        G5.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("G6")){
                        G6.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("G7")){
                        G7.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("G8")){
                        G8.setImageResource(R.drawable.black_pawn);
                    }

                    if(tile.equals("H1")){
                        H1.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("H2")){
                        H2.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("H3")){
                        H3.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("H4")){
                        H4.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("H5")){
                        H5.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("H6")){
                        H6.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("H7")){
                        H7.setImageResource(R.drawable.black_pawn);
                    }
                    if(tile.equals("H8")){
                        H8.setImageResource(R.drawable.black_pawn);
                    }
                }
                if(piece.equals("blackRook")){
                    if(tile.equals("A1")){
                        A1.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("A2")){
                        A2.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("A3")){
                        A3.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("A4")){
                        A4.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("A5")){
                        A5.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("A6")){
                        A6.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("A7")){
                        A7.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("A8")){
                        A8.setImageResource(R.drawable.black_bishop);
                    }

                    if(tile.equals("B1")){
                        B1.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("B2")){
                        B2.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("B3")){
                        B3.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("B4")){
                        B4.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("B5")){
                        B5.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("B6")){
                        B6.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("B7")){
                        B7.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("B8")){
                        B8.setImageResource(R.drawable.black_bishop);
                    }

                    if(tile.equals("C1")){
                        C1.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("C2")){
                        C2.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("C3")){
                        C3.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("C4")){
                        C4.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("C5")){
                        C5.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("C6")){
                        C6.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("C7")){
                        C7.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("C8")){
                        C8.setImageResource(R.drawable.black_bishop);
                    }

                    if(tile.equals("D1")){
                        D1.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("D2")){
                        D2.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("D3")){
                        D3.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("D4")){
                        D4.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("D5")){
                        D5.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("D6")){
                        D6.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("D7")){
                        D7.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("D8")){
                        D8.setImageResource(R.drawable.black_bishop);
                    }

                    if(tile.equals("E1")){
                        E1.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("E2")){
                        E2.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("E3")){
                        E3.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("E4")){
                        E4.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("E5")){
                        E5.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("E6")){
                        E6.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("E7")){
                        E7.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("E8")){
                        E8.setImageResource(R.drawable.black_bishop);
                    }

                    if(tile.equals("F1")){
                        F1.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("F2")){
                        F2.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("F3")){
                        F3.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("F4")){
                        F4.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("F5")){
                        F5.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("F6")){
                        F6.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("F7")){
                        F7.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("F8")){
                        F8.setImageResource(R.drawable.black_bishop);
                    }

                    if(tile.equals("G1")){
                        G1.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("G2")){
                        G2.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("G3")){
                        G3.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("G4")){
                        G4.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("G5")){
                        G5.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("G6")){
                        G6.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("G7")){
                        G7.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("G8")){
                        G8.setImageResource(R.drawable.black_bishop);
                    }

                    if(tile.equals("H1")){
                        H1.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("H2")){
                        H2.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("H3")){
                        H3.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("H4")){
                        H4.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("H5")){
                        H5.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("H6")){
                        H6.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("H7")){
                        H7.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("H8")){
                        H8.setImageResource(R.drawable.black_bishop);
                    }
                }
                if(piece.equals("blackBishop")){
                    if(tile.equals("A1")){
                        A1.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("A2")){
                        A2.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("A3")){
                        A3.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("A4")){
                        A4.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("A5")){
                        A5.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("A6")){
                        A6.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("A7")){
                        A7.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("A8")){
                        A8.setImageResource(R.drawable.black_bishop);
                    }

                    if(tile.equals("B1")){
                        B1.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("B2")){
                        B2.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("B3")){
                        B3.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("B4")){
                        B4.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("B5")){
                        B5.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("B6")){
                        B6.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("B7")){
                        B7.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("B8")){
                        B8.setImageResource(R.drawable.black_bishop);
                    }

                    if(tile.equals("C1")){
                        C1.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("C2")){
                        C2.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("C3")){
                        C3.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("C4")){
                        C4.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("C5")){
                        C5.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("C6")){
                        C6.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("C7")){
                        C7.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("C8")){
                        C8.setImageResource(R.drawable.black_bishop);
                    }

                    if(tile.equals("D1")){
                        D1.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("D2")){
                        D2.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("D3")){
                        D3.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("D4")){
                        D4.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("D5")){
                        D5.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("D6")){
                        D6.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("D7")){
                        D7.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("D8")){
                        D8.setImageResource(R.drawable.black_bishop);
                    }

                    if(tile.equals("E1")){
                        E1.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("E2")){
                        E2.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("E3")){
                        E3.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("E4")){
                        E4.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("E5")){
                        E5.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("E6")){
                        E6.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("E7")){
                        E7.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("E8")){
                        E8.setImageResource(R.drawable.black_bishop);
                    }

                    if(tile.equals("F1")){
                        F1.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("F2")){
                        F2.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("F3")){
                        F3.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("F4")){
                        F4.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("F5")){
                        F5.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("F6")){
                        F6.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("F7")){
                        F7.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("F8")){
                        F8.setImageResource(R.drawable.black_bishop);
                    }

                    if(tile.equals("G1")){
                        G1.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("G2")){
                        G2.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("G3")){
                        G3.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("G4")){
                        G4.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("G5")){
                        G5.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("G6")){
                        G6.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("G7")){
                        G7.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("G8")){
                        G8.setImageResource(R.drawable.black_bishop);
                    }

                    if(tile.equals("H1")){
                        H1.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("H2")){
                        H2.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("H3")){
                        H3.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("H4")){
                        H4.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("H5")){
                        H5.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("H6")){
                        H6.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("H7")){
                        H7.setImageResource(R.drawable.black_bishop);
                    }
                    if(tile.equals("H8")){
                        H8.setImageResource(R.drawable.black_bishop);
                    }
                }
                if(piece.equals("blackKnight")){
                    if(tile.equals("A1")){
                        A1.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("A2")){
                        A2.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("A3")){
                        A3.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("A4")){
                        A4.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("A5")){
                        A5.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("A6")){
                        A6.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("A7")){
                        A7.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("A8")){
                        A8.setImageResource(R.drawable.black_knight);
                    }

                    if(tile.equals("B1")){
                        B1.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("B2")){
                        B2.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("B3")){
                        B3.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("B4")){
                        B4.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("B5")){
                        B5.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("B6")){
                        B6.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("B7")){
                        B7.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("B8")){
                        B8.setImageResource(R.drawable.black_knight);
                    }

                    if(tile.equals("C1")){
                        C1.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("C2")){
                        C2.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("C3")){
                        C3.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("C4")){
                        C4.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("C5")){
                        C5.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("C6")){
                        C6.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("C7")){
                        C7.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("C8")){
                        C8.setImageResource(R.drawable.black_knight);
                    }

                    if(tile.equals("D1")){
                        D1.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("D2")){
                        D2.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("D3")){
                        D3.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("D4")){
                        D4.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("D5")){
                        D5.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("D6")){
                        D6.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("D7")){
                        D7.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("D8")){
                        D8.setImageResource(R.drawable.black_knight);
                    }

                    if(tile.equals("E1")){
                        E1.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("E2")){
                        E2.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("E3")){
                        E3.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("E4")){
                        E4.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("E5")){
                        E5.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("E6")){
                        E6.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("E7")){
                        E7.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("E8")){
                        E8.setImageResource(R.drawable.black_knight);
                    }

                    if(tile.equals("F1")){
                        F1.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("F2")){
                        F2.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("F3")){
                        F3.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("F4")){
                        F4.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("F5")){
                        F5.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("F6")){
                        F6.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("F7")){
                        F7.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("F8")){
                        F8.setImageResource(R.drawable.black_knight);
                    }

                    if(tile.equals("G1")){
                        G1.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("G2")){
                        G2.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("G3")){
                        G3.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("G4")){
                        G4.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("G5")){
                        G5.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("G6")){
                        G6.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("G7")){
                        G7.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("G8")){
                        G8.setImageResource(R.drawable.black_knight);
                    }

                    if(tile.equals("H1")){
                        H1.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("H2")){
                        H2.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("H3")){
                        H3.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("H4")){
                        H4.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("H5")){
                        H5.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("H6")){
                        H6.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("H7")){
                        H7.setImageResource(R.drawable.black_knight);
                    }
                    if(tile.equals("H8")){
                        H8.setImageResource(R.drawable.black_knight);
                    }
                }
                if(piece.equals("blackQueen")){
                    if(tile.equals("A1")){
                        A1.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("A2")){
                        A2.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("A3")){
                        A3.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("A4")){
                        A4.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("A5")){
                        A5.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("A6")){
                        A6.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("A7")){
                        A7.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("A8")){
                        A8.setImageResource(R.drawable.black_queen);
                    }

                    if(tile.equals("B1")){
                        B1.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("B2")){
                        B2.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("B3")){
                        B3.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("B4")){
                        B4.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("B5")){
                        B5.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("B6")){
                        B6.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("B7")){
                        B7.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("B8")){
                        B8.setImageResource(R.drawable.black_queen);
                    }

                    if(tile.equals("C1")){
                        C1.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("C2")){
                        C2.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("C3")){
                        C3.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("C4")){
                        C4.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("C5")){
                        C5.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("C6")){
                        C6.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("C7")){
                        C7.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("C8")){
                        C8.setImageResource(R.drawable.black_queen);
                    }

                    if(tile.equals("D1")){
                        D1.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("D2")){
                        D2.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("D3")){
                        D3.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("D4")){
                        D4.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("D5")){
                        D5.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("D6")){
                        D6.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("D7")){
                        D7.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("D8")){
                        D8.setImageResource(R.drawable.black_queen);
                    }

                    if(tile.equals("E1")){
                        E1.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("E2")){
                        E2.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("E3")){
                        E3.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("E4")){
                        E4.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("E5")){
                        E5.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("E6")){
                        E6.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("E7")){
                        E7.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("E8")){
                        E8.setImageResource(R.drawable.black_queen);
                    }

                    if(tile.equals("F1")){
                        F1.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("F2")){
                        F2.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("F3")){
                        F3.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("F4")){
                        F4.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("F5")){
                        F5.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("F6")){
                        F6.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("F7")){
                        F7.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("F8")){
                        F8.setImageResource(R.drawable.black_queen);
                    }

                    if(tile.equals("G1")){
                        G1.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("G2")){
                        G2.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("G3")){
                        G3.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("G4")){
                        G4.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("G5")){
                        G5.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("G6")){
                        G6.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("G7")){
                        G7.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("G8")){
                        G8.setImageResource(R.drawable.black_queen);
                    }

                    if(tile.equals("H1")){
                        H1.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("H2")){
                        H2.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("H3")){
                        H3.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("H4")){
                        H4.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("H5")){
                        H5.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("H6")){
                        H6.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("H7")){
                        H7.setImageResource(R.drawable.black_queen);
                    }
                    if(tile.equals("H8")){
                        H8.setImageResource(R.drawable.black_queen);
                    }
                }
                if(piece.equals("blackKing")){
                    if(tile.equals("A1")){
                        A1.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("A2")){
                        A2.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("A3")){
                        A3.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("A4")){
                        A4.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("A5")){
                        A5.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("A6")){
                        A6.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("A7")){
                        A7.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("A8")){
                        A8.setImageResource(R.drawable.black_king);
                    }

                    if(tile.equals("B1")){
                        B1.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("B2")){
                        B2.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("B3")){
                        B3.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("B4")){
                        B4.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("B5")){
                        B5.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("B6")){
                        B6.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("B7")){
                        B7.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("B8")){
                        B8.setImageResource(R.drawable.black_king);
                    }

                    if(tile.equals("C1")){
                        C1.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("C2")){
                        C2.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("C3")){
                        C3.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("C4")){
                        C4.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("C5")){
                        C5.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("C6")){
                        C6.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("C7")){
                        C7.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("C8")){
                        C8.setImageResource(R.drawable.black_king);
                    }

                    if(tile.equals("D1")){
                        D1.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("D2")){
                        D2.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("D3")){
                        D3.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("D4")){
                        D4.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("D5")){
                        D5.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("D6")){
                        D6.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("D7")){
                        D7.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("D8")){
                        D8.setImageResource(R.drawable.black_king);
                    }

                    if(tile.equals("E1")){
                        E1.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("E2")){
                        E2.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("E3")){
                        E3.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("E4")){
                        E4.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("E5")){
                        E5.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("E6")){
                        E6.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("E7")){
                        E7.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("E8")){
                        E8.setImageResource(R.drawable.black_king);
                    }

                    if(tile.equals("F1")){
                        F1.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("F2")){
                        F2.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("F3")){
                        F3.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("F4")){
                        F4.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("F5")){
                        F5.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("F6")){
                        F6.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("F7")){
                        F7.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("F8")){
                        F8.setImageResource(R.drawable.black_king);
                    }

                    if(tile.equals("G1")){
                        G1.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("G2")){
                        G2.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("G3")){
                        G3.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("G4")){
                        G4.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("G5")){
                        G5.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("G6")){
                        G6.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("G7")){
                        G7.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("G8")){
                        G8.setImageResource(R.drawable.black_king);
                    }

                    if(tile.equals("H1")){
                        H1.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("H2")){
                        H2.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("H3")){
                        H3.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("H4")){
                        H4.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("H5")){
                        H5.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("H6")){
                        H6.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("H7")){
                        H7.setImageResource(R.drawable.black_king);
                    }
                    if(tile.equals("H8")){
                        H8.setImageResource(R.drawable.black_king);
                    }
                }

                if(piece.equals("whitePawn")){
                    if(tile.equals("A1")){
                        A1.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("A2")){
                        A2.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("A3")){
                        A3.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("A4")){
                        A4.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("A5")){
                        A5.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("A6")){
                        A6.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("A7")){
                        A7.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("A8")){
                        A8.setImageResource(R.drawable.white_pawn);
                    }

                    if(tile.equals("B1")){
                        B1.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("B2")){
                        B2.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("B3")){
                        B3.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("B4")){
                        B4.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("B5")){
                        B5.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("B6")){
                        B6.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("B7")){
                        B7.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("B8")){
                        B8.setImageResource(R.drawable.white_pawn);
                    }

                    if(tile.equals("C1")){
                        C1.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("C2")){
                        C2.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("C3")){
                        C3.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("C4")){
                        C4.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("C5")){
                        C5.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("C6")){
                        C6.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("C7")){
                        C7.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("C8")){
                        C8.setImageResource(R.drawable.white_pawn);
                    }

                    if(tile.equals("D1")){
                        D1.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("D2")){
                        D2.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("D3")){
                        D3.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("D4")){
                        D4.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("D5")){
                        D5.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("D6")){
                        D6.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("D7")){
                        D7.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("D8")){
                        D8.setImageResource(R.drawable.white_pawn);
                    }

                    if(tile.equals("E1")){
                        E1.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("E2")){
                        E2.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("E3")){
                        E3.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("E4")){
                        E4.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("E5")){
                        E5.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("E6")){
                        E6.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("E7")){
                        E7.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("E8")){
                        E8.setImageResource(R.drawable.white_pawn);
                    }

                    if(tile.equals("F1")){
                        F1.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("F2")){
                        F2.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("F3")){
                        F3.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("F4")){
                        F4.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("F5")){
                        F5.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("F6")){
                        F6.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("F7")){
                        F7.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("F8")){
                        F8.setImageResource(R.drawable.white_pawn);
                    }

                    if(tile.equals("G1")){
                        G1.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("G2")){
                        G2.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("G3")){
                        G3.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("G4")){
                        G4.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("G5")){
                        G5.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("G6")){
                        G6.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("G7")){
                        G7.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("G8")){
                        G8.setImageResource(R.drawable.white_pawn);
                    }

                    if(tile.equals("H1")){
                        H1.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("H2")){
                        H2.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("H3")){
                        H3.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("H4")){
                        H4.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("H5")){
                        H5.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("H6")){
                        H6.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("H7")){
                        H7.setImageResource(R.drawable.white_pawn);
                    }
                    if(tile.equals("H8")){
                        H8.setImageResource(R.drawable.white_pawn);
                    }
                }
                if(piece.equals("whiteRook")){
                    if(tile.equals("A1")){
                        A1.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("A2")){
                        A2.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("A3")){
                        A3.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("A4")){
                        A4.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("A5")){
                        A5.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("A6")){
                        A6.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("A7")){
                        A7.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("A8")){
                        A8.setImageResource(R.drawable.white_rook);
                    }

                    if(tile.equals("B1")){
                        B1.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("B2")){
                        B2.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("B3")){
                        B3.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("B4")){
                        B4.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("B5")){
                        B5.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("B6")){
                        B6.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("B7")){
                        B7.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("B8")){
                        B8.setImageResource(R.drawable.white_rook);
                    }

                    if(tile.equals("C1")){
                        C1.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("C2")){
                        C2.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("C3")){
                        C3.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("C4")){
                        C4.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("C5")){
                        C5.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("C6")){
                        C6.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("C7")){
                        C7.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("C8")){
                        C8.setImageResource(R.drawable.white_rook);
                    }

                    if(tile.equals("D1")){
                        D1.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("D2")){
                        D2.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("D3")){
                        D3.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("D4")){
                        D4.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("D5")){
                        D5.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("D6")){
                        D6.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("D7")){
                        D7.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("D8")){
                        D8.setImageResource(R.drawable.white_rook);
                    }

                    if(tile.equals("E1")){
                        E1.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("E2")){
                        E2.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("E3")){
                        E3.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("E4")){
                        E4.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("E5")){
                        E5.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("E6")){
                        E6.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("E7")){
                        E7.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("E8")){
                        E8.setImageResource(R.drawable.white_rook);
                    }

                    if(tile.equals("F1")){
                        F1.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("F2")){
                        F2.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("F3")){
                        F3.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("F4")){
                        F4.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("F5")){
                        F5.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("F6")){
                        F6.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("F7")){
                        F7.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("F8")){
                        F8.setImageResource(R.drawable.white_rook);
                    }

                    if(tile.equals("G1")){
                        G1.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("G2")){
                        G2.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("G3")){
                        G3.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("G4")){
                        G4.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("G5")){
                        G5.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("G6")){
                        G6.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("G7")){
                        G7.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("G8")){
                        G8.setImageResource(R.drawable.white_rook);
                    }

                    if(tile.equals("H1")){
                        H1.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("H2")){
                        H2.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("H3")){
                        H3.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("H4")){
                        H4.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("H5")){
                        H5.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("H6")){
                        H6.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("H7")){
                        H7.setImageResource(R.drawable.white_rook);
                    }
                    if(tile.equals("H8")){
                        H8.setImageResource(R.drawable.white_rook);
                    }
                }
                if(piece.equals("whiteBishop")){
                    if(tile.equals("A1")){
                        A1.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("A2")){
                        A2.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("A3")){
                        A3.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("A4")){
                        A4.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("A5")){
                        A5.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("A6")){
                        A6.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("A7")){
                        A7.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("A8")){
                        A8.setImageResource(R.drawable.white_bishop);
                    }

                    if(tile.equals("B1")){
                        B1.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("B2")){
                        B2.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("B3")){
                        B3.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("B4")){
                        B4.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("B5")){
                        B5.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("B6")){
                        B6.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("B7")){
                        B7.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("B8")){
                        B8.setImageResource(R.drawable.white_bishop);
                    }

                    if(tile.equals("C1")){
                        C1.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("C2")){
                        C2.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("C3")){
                        C3.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("C4")){
                        C4.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("C5")){
                        C5.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("C6")){
                        C6.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("C7")){
                        C7.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("C8")){
                        C8.setImageResource(R.drawable.white_bishop);
                    }

                    if(tile.equals("D1")){
                        D1.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("D2")){
                        D2.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("D3")){
                        D3.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("D4")){
                        D4.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("D5")){
                        D5.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("D6")){
                        D6.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("D7")){
                        D7.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("D8")){
                        D8.setImageResource(R.drawable.white_bishop);
                    }

                    if(tile.equals("E1")){
                        E1.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("E2")){
                        E2.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("E3")){
                        E3.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("E4")){
                        E4.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("E5")){
                        E5.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("E6")){
                        E6.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("E7")){
                        E7.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("E8")){
                        E8.setImageResource(R.drawable.white_bishop);
                    }

                    if(tile.equals("F1")){
                        F1.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("F2")){
                        F2.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("F3")){
                        F3.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("F4")){
                        F4.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("F5")){
                        F5.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("F6")){
                        F6.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("F7")){
                        F7.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("F8")){
                        F8.setImageResource(R.drawable.white_bishop);
                    }

                    if(tile.equals("G1")){
                        G1.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("G2")){
                        G2.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("G3")){
                        G3.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("G4")){
                        G4.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("G5")){
                        G5.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("G6")){
                        G6.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("G7")){
                        G7.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("G8")){
                        G8.setImageResource(R.drawable.white_bishop);
                    }

                    if(tile.equals("H1")){
                        H1.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("H2")){
                        H2.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("H3")){
                        H3.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("H4")){
                        H4.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("H5")){
                        H5.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("H6")){
                        H6.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("H7")){
                        H7.setImageResource(R.drawable.white_bishop);
                    }
                    if(tile.equals("H8")){
                        H8.setImageResource(R.drawable.white_bishop);
                    }
                }
                if(piece.equals("whiteKnight")){
                    if(tile.equals("A1")){
                        A1.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("A2")){
                        A2.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("A3")){
                        A3.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("A4")){
                        A4.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("A5")){
                        A5.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("A6")){
                        A6.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("A7")){
                        A7.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("A8")){
                        A8.setImageResource(R.drawable.white_knight);
                    }

                    if(tile.equals("B1")){
                        B1.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("B2")){
                        B2.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("B3")){
                        B3.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("B4")){
                        B4.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("B5")){
                        B5.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("B6")){
                        B6.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("B7")){
                        B7.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("B8")){
                        B8.setImageResource(R.drawable.white_knight);
                    }

                    if(tile.equals("C1")){
                        C1.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("C2")){
                        C2.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("C3")){
                        C3.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("C4")){
                        C4.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("C5")){
                        C5.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("C6")){
                        C6.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("C7")){
                        C7.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("C8")){
                        C8.setImageResource(R.drawable.white_knight);
                    }

                    if(tile.equals("D1")){
                        D1.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("D2")){
                        D2.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("D3")){
                        D3.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("D4")){
                        D4.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("D5")){
                        D5.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("D6")){
                        D6.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("D7")){
                        D7.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("D8")){
                        D8.setImageResource(R.drawable.white_knight);
                    }

                    if(tile.equals("E1")){
                        E1.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("E2")){
                        E2.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("E3")){
                        E3.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("E4")){
                        E4.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("E5")){
                        E5.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("E6")){
                        E6.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("E7")){
                        E7.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("E8")){
                        E8.setImageResource(R.drawable.white_knight);
                    }

                    if(tile.equals("F1")){
                        F1.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("F2")){
                        F2.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("F3")){
                        F3.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("F4")){
                        F4.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("F5")){
                        F5.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("F6")){
                        F6.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("F7")){
                        F7.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("F8")){
                        F8.setImageResource(R.drawable.white_knight);
                    }

                    if(tile.equals("G1")){
                        G1.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("G2")){
                        G2.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("G3")){
                        G3.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("G4")){
                        G4.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("G5")){
                        G5.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("G6")){
                        G6.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("G7")){
                        G7.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("G8")){
                        G8.setImageResource(R.drawable.white_knight);
                    }

                    if(tile.equals("H1")){
                        H1.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("H2")){
                        H2.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("H3")){
                        H3.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("H4")){
                        H4.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("H5")){
                        H5.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("H6")){
                        H6.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("H7")){
                        H7.setImageResource(R.drawable.white_knight);
                    }
                    if(tile.equals("H8")){
                        H8.setImageResource(R.drawable.white_knight);
                    }
                }
                if(piece.equals("whiteQueen")){
                    if(tile.equals("A1")){
                        A1.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("A2")){
                        A2.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("A3")){
                        A3.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("A4")){
                        A4.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("A5")){
                        A5.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("A6")){
                        A6.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("A7")){
                        A7.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("A8")){
                        A8.setImageResource(R.drawable.white_queen);
                    }

                    if(tile.equals("B1")){
                        B1.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("B2")){
                        B2.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("B3")){
                        B3.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("B4")){
                        B4.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("B5")){
                        B5.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("B6")){
                        B6.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("B7")){
                        B7.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("B8")){
                        B8.setImageResource(R.drawable.white_queen);
                    }

                    if(tile.equals("C1")){
                        C1.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("C2")){
                        C2.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("C3")){
                        C3.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("C4")){
                        C4.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("C5")){
                        C5.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("C6")){
                        C6.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("C7")){
                        C7.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("C8")){
                        C8.setImageResource(R.drawable.white_queen);
                    }

                    if(tile.equals("D1")){
                        D1.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("D2")){
                        D2.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("D3")){
                        D3.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("D4")){
                        D4.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("D5")){
                        D5.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("D6")){
                        D6.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("D7")){
                        D7.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("D8")){
                        D8.setImageResource(R.drawable.white_queen);
                    }

                    if(tile.equals("E1")){
                        E1.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("E2")){
                        E2.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("E3")){
                        E3.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("E4")){
                        E4.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("E5")){
                        E5.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("E6")){
                        E6.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("E7")){
                        E7.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("E8")){
                        E8.setImageResource(R.drawable.white_queen);
                    }

                    if(tile.equals("F1")){
                        F1.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("F2")){
                        F2.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("F3")){
                        F3.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("F4")){
                        F4.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("F5")){
                        F5.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("F6")){
                        F6.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("F7")){
                        F7.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("F8")){
                        F8.setImageResource(R.drawable.white_queen);
                    }

                    if(tile.equals("G1")){
                        G1.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("G2")){
                        G2.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("G3")){
                        G3.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("G4")){
                        G4.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("G5")){
                        G5.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("G6")){
                        G6.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("G7")){
                        G7.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("G8")){
                        G8.setImageResource(R.drawable.white_queen);
                    }

                    if(tile.equals("H1")){
                        H1.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("H2")){
                        H2.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("H3")){
                        H3.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("H4")){
                        H4.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("H5")){
                        H5.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("H6")){
                        H6.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("H7")){
                        H7.setImageResource(R.drawable.white_queen);
                    }
                    if(tile.equals("H8")){
                        H8.setImageResource(R.drawable.white_queen);
                    }
                }
                if(piece.equals("whiteKing")){
                    if(tile.equals("A1")){
                        A1.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("A2")){
                        A2.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("A3")){
                        A3.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("A4")){
                        A4.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("A5")){
                        A5.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("A6")){
                        A6.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("A7")){
                        A7.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("A8")){
                        A8.setImageResource(R.drawable.white_king);
                    }

                    if(tile.equals("B1")){
                        B1.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("B2")){
                        B2.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("B3")){
                        B3.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("B4")){
                        B4.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("B5")){
                        B5.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("B6")){
                        B6.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("B7")){
                        B7.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("B8")){
                        B8.setImageResource(R.drawable.white_king);
                    }

                    if(tile.equals("C1")){
                        C1.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("C2")){
                        C2.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("C3")){
                        C3.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("C4")){
                        C4.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("C5")){
                        C5.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("C6")){
                        C6.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("C7")){
                        C7.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("C8")){
                        C8.setImageResource(R.drawable.white_king);
                    }

                    if(tile.equals("D1")){
                        D1.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("D2")){
                        D2.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("D3")){
                        D3.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("D4")){
                        D4.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("D5")){
                        D5.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("D6")){
                        D6.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("D7")){
                        D7.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("D8")){
                        D8.setImageResource(R.drawable.white_king);
                    }

                    if(tile.equals("E1")){
                        E1.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("E2")){
                        E2.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("E3")){
                        E3.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("E4")){
                        E4.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("E5")){
                        E5.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("E6")){
                        E6.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("E7")){
                        E7.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("E8")){
                        E8.setImageResource(R.drawable.white_king);
                    }

                    if(tile.equals("F1")){
                        F1.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("F2")){
                        F2.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("F3")){
                        F3.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("F4")){
                        F4.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("F5")){
                        F5.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("F6")){
                        F6.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("F7")){
                        F7.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("F8")){
                        F8.setImageResource(R.drawable.white_king);
                    }

                    if(tile.equals("G1")){
                        G1.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("G2")){
                        G2.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("G3")){
                        G3.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("G4")){
                        G4.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("G5")){
                        G5.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("G6")){
                        G6.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("G7")){
                        G7.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("G8")){
                        G8.setImageResource(R.drawable.white_king);
                    }

                    if(tile.equals("H1")){
                        H1.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("H2")){
                        H2.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("H3")){
                        H3.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("H4")){
                        H4.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("H5")){
                        H5.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("H6")){
                        H6.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("H7")){
                        H7.setImageResource(R.drawable.white_king);
                    }
                    if(tile.equals("H8")){
                        H8.setImageResource(R.drawable.white_king);
                    }
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
                Button ChessToMenuBtn = (Button) inflatedLayout.findViewById(R.id.ReturnToMenuBtn);

                //Displays win message on screen
                resultText.setText(result);

                GameOverLayout.addView(inflatedLayout);

                ChessToMenuBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Returns user to main menu
                        Intent intent = new Intent(ChessActivity.this, MainMenuActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void displayWhoseMove(String playerWhoJustMoved) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (playerWhoJustMoved.equals("Player1Moved")) {
                    WhoseMove.setText("Player 2's move.");
                } else

                if (playerWhoJustMoved.equals("Player2Moved")) {
                    WhoseMove.setText("Player 1's move.");
                }
            }
        });
    }

//    private void displayPiecePromotion(String tile){
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                PromotionLayout.removeAllViews();
//                PromotionLayout.setVisibility(View.VISIBLE);
//
//                View inflatedLayout = getLayoutInflater().inflate(R.layout.piece_promotion_layout, null, false);
//                ImageButton King = (ImageButton) inflatedLayout.findViewById(R.id.King);
//                ImageButton Queen = (ImageButton) inflatedLayout.findViewById(R.id.Queen);
//                ImageButton Knight = (ImageButton) inflatedLayout.findViewById(R.id.Knight);
//                ImageButton Rook = (ImageButton) inflatedLayout.findViewById(R.id.Rook);
//                ImageButton Bishop = (ImageButton) inflatedLayout.findViewById(R.id.Bishop);
//
//                King.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(UserRole.equals("Player1")){
//                            piece = "whiteKnight";
//                            movePiece(tile, piece);
//                        }
//                        if(UserRole.equals("Player2")){
//                            piece = "whiteKnight";
//                            movePiece(tile, piece);
//                        }
//                    }
//                });
//                Queen.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(UserRole.equals("Player1")){
//                            piece = "whiteQueen";
//                            movePiece(tile, piece);
//                        }
//                        if(UserRole.equals("Player2")){
//                            piece = "whiteQueen";
//                            movePiece(tile, piece);
//                        }
//                    }
//                });
//                Knight.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(UserRole.equals("Player1")){
//                            piece = "whiteKnight";
//                            movePiece(tile, piece);
//                        }
//                        if(UserRole.equals("Player2")){
//                            piece = "whiteKnight";
//                            movePiece(tile, piece);
//                        }
//                    }
//                });
//                Rook.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(UserRole.equals("Player1")){
//                            piece = "whiteRook";
//                            movePiece(tile, piece);
//                        }
//                        if(UserRole.equals("Player2")){
//                            piece = "whiteRook";
//                            movePiece(tile, piece);
//                        }
//                    }
//                });
//                Bishop.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(UserRole.equals("Player1")){
//                            piece = "whiteBishop";
//                            movePiece(tile, piece);
//                        }
//                        if(UserRole.equals("Player2")){
//                            piece = "whiteBishop";
//                            movePiece(tile, piece);
//                        }
//                    }
//                });
//            }
//        });
//    }

}