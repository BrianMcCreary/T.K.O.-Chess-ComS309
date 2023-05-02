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
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tko_chess.ultils.Const;

/**
 * @author Lex Somers
 * @author Zachary Scurlock
 *  Chess activity where players can play or spectate a game of chess.
 */
public class ChessActivity extends AppCompatActivity {
    //////////////////////////////////////////////////////////////////////////
    ImageButton ChessOptions;

    TextView TurnsRemaining;
    TextView WhoseMove;

    /**
     * String stores the user's player type.
     * Player types are Player1, Player2, or Spectator.
     */
    String UserRole = "";

    /**
     * String holds what gamemode a user is playing.
     * Gamemodes are chess, chessboxing, or boxing.
     */
    String GameMode = "";

    /**
     * int tracks how many moves have been played so far. Used for knowing when to switch to boxing in "ChessBoxing" game mode.
     */
    int TurnTracker = 0;

    /**
     * String tracks who is player 1
     */
    String WhoPlayer1 = "";

    /**
     * String tracks who is player 2
     */
    String WhoPlayer2 = "";

    /**
     * String tracks what tile is currently selected for an operation.
     */
    String tile = "";

    /**
     * String stores the name of the selected piece.
     */
    String selectedPiece = "";

    /**
     * String stores the ending of the URL path mapping for the websocket.
     */
    String URLConcatenation = "";

    /**
     * int stores how many boxing game wins player 1 has
     */
    int Player1Wins;

    /**
     * int stores how many boxing game wins player 2 has
     */
    int Player2Wins;

    /**
     * int stores what round of boxing the Chess Boxing game is currently on.
     */
    int BoxingRoundNum;

    /**
     * boolean tracks if its player 1's turn or player 2's turn
     * true is player 1. false is player 2.
     */
    boolean Player1Turn = true;

    /**
     * LinearLayout holding the frontend display of the board.
     */
    LinearLayout ChessBoard;

    LinearLayout OptionsLayout;
    LinearLayout GameOverLayout;

    /**
     * SingletonUser instance which stores the currently logged in user.
     */
    SingletonUser currUser = SingletonUser.getInstance();

    /**
     * WebSocket used for joining, leaving and playing chess game.
     * Static variable for when game mode is ChessBoxing.
     */
    static WebSocketClient ChessWebSocket = null;

    /**
     * Hashmap storing all the buttons making up the board.
     */
    Map<String, ImageButton> board = new HashMap<String, ImageButton>();

    /**
     * Hashmap storing all the chess piece drawables.
     */
    Map<String, Integer> chessPieces = new HashMap<String, Integer>();
    //////////////////////////////////////////////////////////////////////////



    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     *  Loads the chess screen and handles the moves of the user
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_activity_chess);

        ChessOptions = findViewById(R.id.ChessOptionsBtn);

        ChessBoard = findViewById(R.id.ChessBoardLinearLayout);
        OptionsLayout = findViewById(R.id.ChessOptionsLayout);
        GameOverLayout = findViewById(R.id.GameOverLayout);

        TurnsRemaining = findViewById(R.id.TurnsLeftText);
        WhoseMove = findViewById(R.id.WhoseMoveText);

        /////////////////////////////////////////////////////////////////
        UserRole = getIntent().getExtras().getString("UserRole");
        GameMode = getIntent().getExtras().getString("Gamemode");
        WhoPlayer1 = getIntent().getExtras().getString("Player1");
        WhoPlayer2 = getIntent().getExtras().getString("Player2");
        Player1Wins = getIntent().getExtras().getInt("Player1Wins");
        Player2Wins = getIntent().getExtras().getInt("Player2Wins");
        BoxingRoundNum = getIntent().getExtras().getInt("RoundNumber");
        /////////////////////////////////////////////////////////////////

        if(!GameMode.equals("ChessBoxing")) {
            TurnsRemaining.setVisibility(View.INVISIBLE);
        }

        if(UserRole.equals("Player2")) {
            disableButtons();
        }

        //Initiates the hashmap with all the chess piece images.
        initiatePiecesHashMap();

        Draft[] drafts = {new Draft_6455()};

        URLConcatenation = currUser.getUsername(); // Sets URLConcatenation equal to the current user's name

        if (ChessWebSocket == null) {
            try{
                ChessWebSocket = new WebSocketClient(new URI(Const.URL_CHESS_WEBSOCKET + URLConcatenation), (Draft)drafts[0] ) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        Log.d("OPEN", "run() returned: " + "is connecting");
                        System.out.println("onOpen returned");
                        //Gets state of the board to display it on screen (mainly for ChessBoxing, not Chess)
                        ChessWebSocket.send("GetBoard");
                        if (UserRole.equals("Spectator")) {
                            disableButtons();
                        }
                    }

                    @Override
                    public void onMessage(String message) {
                        Log.d("", "run() returned: " + message);
                        String[] strings = message.split(" ");

                        switch (strings[0]){
                            case "GameBoard":
                                displayBoard(strings[1]);

                                if (UserRole.equals("Player2")) {
                                    disableButtons();
                                }
                                break;


                            //TileSelected <tile>
                            case "TileSelected":
                                highlightTile(strings[1]);
                                break;


                            //Player1Moved <pieceType> <from> <to>
                            case "Player1Moved":
                                //Moves piece for Player 1 and spectators, reset variables, and disables buttons until next move.
                                if (UserRole.equals("Spectator") || UserRole.equals("Player1")) {
                                    //Sets new square to correct piece image.
                                    setSquareImageTo(strings[3], strings[1]);

                                    //Clears old square of piece image.
                                    setSquareImageTo(strings[2], "--------");

                                    //Disables buttons until other user moves.
                                    disableButtons();
                                } else

                                //Moves piece for Player2 resets variables, and enables buttons.
                                if (UserRole.equals("Player2")) {
                                    //Sets new square to correct piece image.
                                    setSquareImageTo(strings[3], strings[1]);

                                    //Clears old square of piece image.
                                    setSquareImageTo(strings[2], "--------");

                                    //Disables buttons until other user moves.
                                    enableButtons();
                                }

                                //Sets it to player 2's turn.
                                Player1Turn = false;

                                unhighlightAll();
                                break;


                            //Player2Moved <pieceType> <from> <to>
                            case "Player2Moved":
                                //Increments number of total turns so far.
                                TurnTracker++;

                                //Moves piece for Player 1 and spectators, reset variables, and disables buttons until next move.
                                if (UserRole.equals("Player1")) {
                                    //Sets new square to correct piece image.
                                    setSquareImageTo(strings[3], strings[1]);

                                    //Clears old square of piece image.
                                    setSquareImageTo(strings[2], "--------");

                                    //Disables buttons until other user moves.
                                    enableButtons();
                                } else

                                //Moves piece for spectators
                                if (UserRole.equals("Spectator")) {
                                    //Sets new square to correct piece image.
                                    setSquareImageTo(strings[3], strings[1]);

                                    //Clears old square of piece image.
                                    setSquareImageTo(strings[2], "--------");

                                } else

                                //Moves piece for Player2 resets variables, and disables buttons.
                                if (UserRole.equals("Player2")) {
                                    //Sets new square to correct piece image.
                                    setSquareImageTo(strings[3], strings[1]);

                                    //Clears old square of piece image.
                                    setSquareImageTo(strings[2], "--------");

                                    disableButtons();
                                }

                                //Sets it to player 1's turn
                                Player1Turn = true;

                                //Unhighlights all the tiles on the board
                                unhighlightAll();

                                //If playing ChessBoxing and 8 moves have occurred, go to boxing.
                                if (((TurnTracker % 8) == 0) && GameMode.equals("ChessBoxing")) {
                                    Intent intent = new Intent(ChessActivity.this, ChessActivity.class);
                                    intent.putExtra("Gamemode", GameMode);
                                    intent.putExtra("RoundNumber", BoxingRoundNum);
                                    intent.putExtra("Player1Wins", Player1Wins);
                                    intent.putExtra("Player2Wins", Player2Wins);
                                    intent.putExtra("Player1", WhoPlayer1);
                                    intent.putExtra("Player2", WhoPlayer2);

                                    startActivity(intent);
                                    TurnTracker = 0;
                                }
                                break;


                            //GameWonBy <winnerUsername>
                            case "GameWonBy":
                                //Disables all other buttons outside of return to main menu button.
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ChessOptions.setClickable(false);
                                        disableButtons();
                                    }
                                });

                                //If user is the player who won the game
                                if (strings[1].equals(currUser.getUsername())) {
                                    ChessWebSocket.close();
                                    displayGameResult("You won!");
                                } else

                                //If user is a player and they lost the game
                                if (!strings[1].equals(currUser.getUsername()) && (UserRole.equals("Player1") || UserRole.equals("Player2"))) {
                                    ChessWebSocket.send("GameType " + GameMode + " loss");
                                    ChessWebSocket.close();

                                    displayGameResult("You lost. :(");
                                } else

                                //If user is a spectator
                                if (UserRole.equals("Spectator")) {
                                    ChessWebSocket.close();
                                    displayGameResult(strings[1] + " won!");
                                }
                                break;


                            //PlayerLeft <username>
                            case "PlayerLeft":
                                //Disables all other buttons outside of return to main menu button.
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ChessOptions.setClickable(false);
                                        disableButtons();
                                    }
                                });

                                //Displays who left the game
                                displayGameResult(strings[1] + " left.");
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
            } catch (URISyntaxException e){
                e.printStackTrace();
                return;
            }

            //Connects to the websocket
            ChessWebSocket.connect();
        }

        //Opens leave game menu
        ChessOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View inflatedLayout;
                Button LeaveGameBtn;
                Button CloseOptionsBtn;
                Button EndGameBtn;
                TextView LeaveGameText;

                //Select the correct menu layout. chesstemp has the EndGameBtn for players.
                if (!UserRole.equals("Spectator")) {
                    inflatedLayout = getLayoutInflater().inflate(R.layout.game_menu_layout_chesstemp, null, false);
                    LeaveGameBtn = (Button) inflatedLayout.findViewById(R.id.LeaveBoxingBtn);
                    CloseOptionsBtn = (Button) inflatedLayout.findViewById(R.id.BackToGameBtn);
                    LeaveGameText = (TextView) inflatedLayout.findViewById(R.id.LeaveBoxingText);
                } else {
                    inflatedLayout = getLayoutInflater().inflate(R.layout.game_menu_layout, null, false);
                    LeaveGameBtn = (Button) inflatedLayout.findViewById(R.id.LeaveBoxingBtn);
                    CloseOptionsBtn = (Button) inflatedLayout.findViewById(R.id.BackToGameBtn);
                    LeaveGameText = (TextView) inflatedLayout.findViewById(R.id.LeaveBoxingText);
                }

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
                        ChessWebSocket.close();

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

                if (!UserRole.equals("Spectator")) {
                    EndGameBtn = (Button) inflatedLayout.findViewById(R.id.EndGameBtn);

                    //Ends game
                    EndGameBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Alerts backend of player winning game.
                            ChessWebSocket.send("GameType " + GameMode + " win");
                        }
                    });
                }

                OptionsLayout.addView(inflatedLayout);
            }
        });
    }



    /**
     * Highlights the specified tile with a light green background.
     */
    public void highlightTile(String whichTile) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                board.get(whichTile).setBackgroundColor(Color.GREEN);
            }
        });
    }



    /**
     * Unhighlights all tiles on the chess board.
     */
    public void unhighlightAll() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (ImageButton button : board.values()) {
                    button.setBackgroundColor(R.drawable.transparent);
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
                for (ImageButton button : board.values()) {
                    button.setClickable(true);
                }
            }
        });
    }



    /**
     Disables buttons for the user
     */
    public void disableButtons() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (ImageButton button : board.values()) {
                    button.setClickable(false);
                }
            }
        });
    }





    /**
     * Displays the current state of the board on the screen.
     */
    private void displayBoard(String boardSetup) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String[] rows = boardSetup.split("#");

                for (int row = 0; row < rows.length; row++) {
                    View NewRank = getLayoutInflater().inflate(R.layout.chess_board_row, null, false);

                    ImageButton[] tiles = {
                            (ImageButton) NewRank.findViewById(R.id.colA),
                            (ImageButton) NewRank.findViewById(R.id.colB),
                            (ImageButton) NewRank.findViewById(R.id.colC),
                            (ImageButton) NewRank.findViewById(R.id.colD),
                            (ImageButton) NewRank.findViewById(R.id.colE),
                            (ImageButton) NewRank.findViewById(R.id.colF),
                            (ImageButton) NewRank.findViewById(R.id.colG),
                            (ImageButton) NewRank.findViewById(R.id.colH)
                    };

                    String[] columns = rows[row].split("[.]");

                    for (int col = 0; col < columns.length; col++) {

                        //Sets column "j" of rank "row" to the correct image.
                        tiles[col].setImageResource(chessPieces.get(columns[col]));

                        if (UserRole.equals("Player2")) {
                            tiles[col].setRotation(180);
                        }

                        String file = "A";
                        switch (col) {
                            case 1:
                                file = "B";
                                break;
                            case 2:
                                file = "C";
                                break;
                            case 3:
                                file = "D";
                                break;
                            case 4:
                                file = "E";
                                break;
                            case 5:
                                file = "F";
                                break;
                            case 6:
                                file = "G";
                                break;
                            case 7:
                                file = "H";
                                break;
                        }

                        String whichTile = file + (row + 1);
                        board.put(whichTile, tiles[col]);
                        board.get(whichTile).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if ((UserRole.equals("Player1") && Player1Turn) || (UserRole.equals("Player2") && !Player1Turn)) {
                                    ChessWebSocket.send(whichTile);
                                    System.out.println("Sent move or select: " + whichTile);
                                }

                            }
                        });
                    }
                    ChessBoard.addView(NewRank);
                }

                if (UserRole.equals("Player2")) {
                    ChessBoard.setRotation(180);
                }
            }
        });
    }



    /**
     * Sets each button on board to correct piece image
     */
    private void setSquareImageTo(String square, String piece) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Sets the current square to the correct image.
                board.get(square).setImageResource(chessPieces.get(piece));
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
                        Intent intent = new Intent(ChessActivity.this, MainMenuActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }



    /**
     * Initiates hashmap that stores all the buttons
     */
    private void initiatePiecesHashMap() {
        //Transparent
        chessPieces.put("--------", R.drawable.transparent);

        //White pieces
        chessPieces.put("whitePawn", R.drawable.white_pawn);
        chessPieces.put("whiteKnight", R.drawable.white_knight);
        chessPieces.put("whiteBishop", R.drawable.white_bishop);
        chessPieces.put("whiteRook", R.drawable.white_rook);
        chessPieces.put("whiteQueen", R.drawable.white_queen);
        chessPieces.put("whiteKing", R.drawable.white_king);

        //Black pieces
        chessPieces.put("blackPawn", R.drawable.black_pawn);
        chessPieces.put("blackKnight", R.drawable.black_knight);
        chessPieces.put("blackBishop", R.drawable.black_bishop);
        chessPieces.put("blackRook", R.drawable.black_rook);
        chessPieces.put("blackQueen", R.drawable.black_queen);
        chessPieces.put("blackKing", R.drawable.black_king);
    }
}