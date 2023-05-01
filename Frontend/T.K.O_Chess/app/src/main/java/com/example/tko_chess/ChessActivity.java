package com.example.tko_chess;

import android.content.Intent;
import android.media.Image;
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
    int BoxingRound;

    /**
     * boolean stores if the player has selected a piece and is ready to move it.
     */
    boolean pieceSelected = false;

    /**
     * LinearLayout holding the frontend display of the board.
     */
    LinearLayout ChessBoard;

    LinearLayout OptionsLayout;

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

        TurnsRemaining = findViewById(R.id.TurnsLeftText);
        WhoseMove = findViewById(R.id.WhoseMoveText);

        /////////////////////////////////////////////////////////////////
        UserRole = getIntent().getExtras().getString("UserRole");
        GameMode = getIntent().getExtras().getString("Gamemode");
        WhoPlayer1 = getIntent().getExtras().getString("Player1");
        WhoPlayer2 = getIntent().getExtras().getString("Player2");
        Player1Wins = getIntent().getExtras().getInt("Player1Wins");
        Player2Wins = getIntent().getExtras().getInt("Player2Wins");
        BoxingRound = getIntent().getExtras().getInt("RoundNumber");
        /////////////////////////////////////////////////////////////////

        if(!GameMode.equals("ChessBoxing")) {
            TurnsRemaining.setVisibility(View.INVISIBLE);
        }

        //Initiates the hashmap with all the chess piece images.
        initiatePiecesHashMap();

        Draft[] drafts = {new Draft_6455()};

        URLConcatenation = currUser.getUsername(); // Sets URLConcatenation equal to the current user's name

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

//                            //Display board for spectators and player 1
//                            if (UserRole.equals("Player1") || UserRole.equals("Spectator")) {
//                                displayBoard(strings[1]);
//                            } else
//
//                            //Display board for player 2
//                            if (UserRole.equals("Player2")) {
//                                displayBoardForBlack(strings[1]);
//                            }
                            displayBoard(strings[1]);
                            setClickListeners();
                            break;

                        case "PieceSelected":
                            selectedPiece = strings[1];
                            if (!selectedPiece.equals("--------")) {
                                pieceSelected = true;
                            }
                            break;

                        case "Player1Moved":
                            if (UserRole.equals("Spectator")) {

                            }
                            break;




                        case "UserMoved":
                            tile = strings[1];
                            selectedPiece = strings[2];

                            //Sets specified tile to specifed piece.
                            setSquareImageTo();

                            //Clears tile and selectedPiece for future use.
                            tile = "";
                            selectedPiece = "";

                            disableButtons(); //disables buttons after user's turn
                            break;

                        case "OpponentMoved":
                            if (WhoPlayer1.equals(currUser.getUsername())) {

                            }

                            enableButtons(); //enables buttons after opponent's turn
                            break;

                        case "UserWin":
                            System.out.println("You Won!");
                            break;

                        case "UserLoss":
                            System.out.println("You Lost!");
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

        if (ChessWebSocket == null) {
            ChessWebSocket.connect(); // Connects to websocket
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
                        //Send leave message if user was a player
                        if (UserRole.equals("Player1") || UserRole.equals("Player2")) {
                            ChessWebSocket.send("leave");
                        }

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
                            ChessWebSocket.send("EndGame");

                            ChessWebSocket.close();

                            //Returns user to main menu
                            Intent intent = new Intent(ChessActivity.this, MainMenuActivity.class);
                            startActivity(intent);
                        }
                    });
                }

                OptionsLayout.addView(inflatedLayout);
            }
        });
    }



    /**
     * This method sets the previous tile clicked become transparent
     */
    public void setTransparent(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                board.get(tile).setImageResource(chessPieces.get("--------"));
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
    public void disableButtons(){
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
     * Sends either a move select piece message or move piece message to backend.
     */
    private void moveOrSelect(String tile, String selectedPiece) {
        //If a piece is not selected, select the piece on that tile, if there is one
        if (!pieceSelected) {
            ChessWebSocket.send("select " + tile);
        } else

        //If a piece is selected, move the piece if possible.
        if (pieceSelected) {
            ChessWebSocket.send("move " + tile + " " + selectedPiece);
        }
    }



    /**
     * Displays the current state of the board on the screen.
     */
    private void displayBoard(String boardSetup) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String[] rows = boardSetup.split("#");
                int row = 8;

                for (int i = 0; i < rows.length; i ++) {
                    LinearLayout NewRank = (LinearLayout) getLayoutInflater().inflate(R.layout.chess_board_row, null, false);

                    String[] col = rows[i].split("[.]");

                    for (int j = 0; j < col.length; j++) {
                        ImageButton newTile = (ImageButton) NewRank.getChildAt(j);
                        newTile.setImageResource(chessPieces.get(col[j]));

                        String file = "A";
                        switch (j) {
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

                        tile = file + row;
                        board.put(tile, newTile);
                    }

                    row--;
                    ChessBoard.addView(NewRank);
                }
            }
        });
    }



    /**
     * Sets each button on board to correct piece image
     */
    private void setSquareImageTo() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Sets the current square to the correct image.
                board.get(tile).setImageResource(chessPieces.get(selectedPiece));
            }
        });
    }



    /**
     * Sets the click functionality for all the buttons.
     */
    private void setClickListeners() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Sets click functionality for all buttons on the board using buttons hashmap.
                int row = 8;
                for (int i = 0; i < 8; i++) {
                    for (int col = 0; col < 8; col++) {
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

                        tile = file + row;
                        board.get(tile).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                moveOrSelect(tile, selectedPiece);
                            }
                        });

                    }
                    row--;
                }
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