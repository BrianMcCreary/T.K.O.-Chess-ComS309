package com.example.tko_chess;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

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
 * @author Zachary Scurlock
 * This is where the real meat and potatoes of chess can be found
 */
public class ChessActivity extends AppCompatActivity {
    //////////////////////////////////////////////////////////////////////////
    /**
     * ImageButton representing each square of the board.
     */
    ImageButton A1, B1, C1, D1, E1, F1, G1, H1,
                A2, B2, C2, D2, E2, F2, G2, H2,
                A3, B3, C3, D3, E3, F3, G3, H3,
                A4, B4, C4, D4, E4, F4, G4, H4,
                A5, B5, C5, D5, E5, F5, G5, H5,
                A6, B6, C6, D6, E6, F6, G6, H6,
                A7, B7, C7, D7, E7, F7, G7, H7,
                A8, B8, C8, D8, E8, F8, G8, H8;

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
    String piece = "";

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
     * int stores a drawable resource piece image
     */
    int pieceDrawable;

    /**
     * int stores how many moves are left before it switches to boxing if in ChessBoxing game mode.
     */
    int movesLeft;

    /**
     * boolean stores if the player has selected a piece and is ready to move it.
     */
    boolean pieceSelected = false;

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
    Map<String, ImageButton> buttons = new HashMap<String, ImageButton>();
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
        setContentView(R.layout.activity_chess);

        /*
         * Assigns the image buttons to their proper ID
         */
        A1 = findViewById(R.id.A1);
        B1 = findViewById(R.id.B1);
        C1 = findViewById(R.id.C1);
        D1 = findViewById(R.id.D1);
        E1 = findViewById(R.id.E1);
        F1 = findViewById(R.id.F1);
        G1 = findViewById(R.id.G1);
        H1 = findViewById(R.id.H1);

        A2 = findViewById(R.id.A2);
        B2 = findViewById(R.id.B2);
        C2 = findViewById(R.id.C2);
        D2 = findViewById(R.id.D2);
        E2 = findViewById(R.id.E2);
        F2 = findViewById(R.id.F2);
        G2 = findViewById(R.id.G2);
        H2 = findViewById(R.id.H2);

        A3 = findViewById(R.id.A3);
        B3 = findViewById(R.id.B3);
        C3 = findViewById(R.id.C3);
        D3 = findViewById(R.id.D3);
        E3 = findViewById(R.id.E3);
        F3 = findViewById(R.id.F3);
        G3 = findViewById(R.id.G3);
        H3 = findViewById(R.id.H3);

        A4 = findViewById(R.id.A4);
        B4 = findViewById(R.id.B4);
        C4 = findViewById(R.id.C4);
        D4 = findViewById(R.id.D4);
        E4 = findViewById(R.id.E4);
        F4 = findViewById(R.id.F4);
        G4 = findViewById(R.id.G4);
        H4 = findViewById(R.id.H4);

        A5 = findViewById(R.id.A5);
        B5 = findViewById(R.id.B5);
        C5 = findViewById(R.id.C5);
        D5 = findViewById(R.id.D5);
        E5 = findViewById(R.id.E5);
        F5 = findViewById(R.id.F5);
        G5 = findViewById(R.id.G5);
        H5 = findViewById(R.id.H5);

        A6 = findViewById(R.id.A6);
        B6 = findViewById(R.id.B6);
        C6 = findViewById(R.id.C6);
        D6 = findViewById(R.id.D6);
        E6 = findViewById(R.id.E6);
        F6 = findViewById(R.id.F6);
        G6 = findViewById(R.id.G6);
        H6 = findViewById(R.id.H6);

        A7 = findViewById(R.id.A7);
        B7 = findViewById(R.id.B7);
        C7 = findViewById(R.id.C7);
        D7 = findViewById(R.id.D7);
        E7 = findViewById(R.id.E7);
        F7 = findViewById(R.id.F7);
        G7 = findViewById(R.id.G7);
        H7 = findViewById(R.id.H7);

        A8 = findViewById(R.id.A8);
        B8 = findViewById(R.id.B8);
        C8 = findViewById(R.id.C8);
        D8 = findViewById(R.id.D8);
        E8 = findViewById(R.id.E8);
        F8 = findViewById(R.id.F8);
        G8 = findViewById(R.id.G8);
        H8 = findViewById(R.id.H8);

        /////////////////////////////////////////////////////////////////
        UserRole = getIntent().getExtras().getString("UserRole");
        GameMode = getIntent().getExtras().getString("Gamemode");
        WhoPlayer1 = getIntent().getExtras().getString("Player1");
        WhoPlayer2 = getIntent().getExtras().getString("Player2");
        Player1Wins = getIntent().getExtras().getInt("Player1Wins");
        Player2Wins = getIntent().getExtras().getInt("Player2Wins");
        BoxingRound = getIntent().getExtras().getInt("RoundNumber");
        /////////////////////////////////////////////////////////////////

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
                            displayBoard(strings[1]);
                            break;

                        case "OpponentMoved":
                            if (WhoPlayer1.equals(currUser.getUsername())) {

                            }

                            enableButtons(); //enables buttons after opponent's turn
                            break;

                        case "UserMoved":

                            disableButtons(); //disables buttons after user's turn
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

        //Initiates the hashmap with all the buttons.
        initiateHashMap();

        //Sets click functionality for all buttons on the board.
        for (int row = 0; row < 8; row++) {
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
                buttons.get(tile).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        moveOrSelect();
                    }
                });
            }
        }
    }



    /**
     * This method sets the previous tile clicked become transparent
     */
    public void setTransparent(){
        buttons.get(tile).setImageResource(R.drawable.transparent);
    }



    /**
     * Enables buttons for the user
     */
    public void enableButtons() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (ImageButton button : buttons.values()) {
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
                for (ImageButton button : buttons.values()) {
                    button.setClickable(false);
                }
            }
        });
    }



    /**
     * Sends either a move select piece message or move piece message to backend.
     */
    private void moveOrSelect() {
        //If a piece is not selected, select the piece on that tile, if there is one
        if (!pieceSelected) {
            ChessWebSocket.send("select " + tile);
        } else

        //If a piece is selected, move the piece if possible.
        if (pieceSelected) {
            ChessWebSocket.send("move " + tile);
        }
    }



    /**
     * Displays the current state of the board on the screen.
     */
    private void displayBoard(String board) {
        String[] rows = board.split("#");
        for (int i = 0; i < rows.length; i ++) {
            String[] col = rows[i].split("[.]");
            for (int j = 0; j < col.length; j++) {
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

                setPiece(file, i + 1, col[j]);
            }
        }
    }



    /**
     * Sets each button on board to correct piece image
     */
    private void setPiece (String col, int row, String piece) {
        String pieceType = piece.substring(5);
        //Example: A1, E5, H7...
        tile = col + row;
        pieceDrawable = R.drawable.transparent;

        //Sets pieceDrawable to the correct white piece.
        if (piece.substring(0, 5).equals("white")) {
            switch (pieceType) {
                case "Pawn":
                    pieceDrawable = R.drawable.white_pawn;
                    break;

                case "Knight":
                    pieceDrawable = R.drawable.white_knight;
                    break;

                case "Bishop":
                    pieceDrawable = R.drawable.white_bishop;
                    break;

                case "Rook":
                    pieceDrawable = R.drawable.white_rook;
                    break;

                case "Queen":
                    pieceDrawable = R.drawable.white_queen;
                    break;

                case "King":
                    pieceDrawable = R.drawable.white_king;
                    break;
            }
        } else

        //Sets pieceDrawable to the correct black piece.
        if (piece.substring(0, 5).equals("black")) {
            switch (pieceType) {
                case "Pawn":
                    pieceDrawable = R.drawable.black_pawn;
                    break;

                case "Knight":
                    pieceDrawable = R.drawable.black_knight;
                    break;

                case "Bishop":
                    pieceDrawable = R.drawable.black_bishop;
                    break;

                case "Rook":
                    pieceDrawable = R.drawable.black_rook;
                    break;

                case "Queen":
                    pieceDrawable = R.drawable.black_queen;
                    break;

                case "King":
                    pieceDrawable = R.drawable.black_king;
                    break;
            }
        }

        //Sets the current square to the correct image.
        buttons.get(tile).setImageResource(pieceDrawable);
    }



    /**
     * This method updates the second tile pressed with the piece from the previously selected tile
     */
    public void movePiece(){
        switch (piece) {
            //Black pieces
            case "blackPawn":
                buttons.get(tile).setImageResource(R.drawable.black_pawn);
                break;
            case "blackBishop":
                buttons.get(tile).setImageResource(R.drawable.black_bishop);
                break;
            case "blackKnight":
                buttons.get(tile).setImageResource(R.drawable.black_knight);
                break;
            case "blackQueen":
                buttons.get(tile).setImageResource(R.drawable.black_queen);
                break;
            case "blackRook":
                buttons.get(tile).setImageResource(R.drawable.black_rook);
                break;
            case "blackKing":
                buttons.get(tile).setImageResource(R.drawable.black_king);
                break;

            //White pieces
            case "whitePawn":
                buttons.get(tile).setImageResource(R.drawable.white_pawn);
                break;
            case "whiteBishop":
                buttons.get(tile).setImageResource(R.drawable.white_bishop);
                break;
            case "whiteKnight":
                buttons.get(tile).setImageResource(R.drawable.white_knight);
                break;
            case "whiteQueen":
                buttons.get(tile).setImageResource(R.drawable.white_queen);
                break;
            case "whiteRook":
                buttons.get(tile).setImageResource(R.drawable.white_rook);
                break;
            case "whiteKing":
                buttons.get(tile).setImageResource(R.drawable.white_king);
                break;
        }
    }



    /**
     * Initiates hashmap that stores all the buttons
     */
    private void initiateHashMap() {
        //Row 1
        buttons.put("A1", A1);
        buttons.put("A2", A2);
        buttons.put("A3", A3);
        buttons.put("A4", A4);
        buttons.put("A5", A5);
        buttons.put("A6", A6);
        buttons.put("A7", A7);
        buttons.put("A8", A8);

        //Row 2
        buttons.put("B1", B1);
        buttons.put("B2", B2);
        buttons.put("B3", B3);
        buttons.put("B4", B4);
        buttons.put("B5", B5);
        buttons.put("B6", B6);
        buttons.put("B7", B7);
        buttons.put("B8", B8);

        //Row 3
        buttons.put("C1", C1);
        buttons.put("C2", C2);
        buttons.put("C3", C3);
        buttons.put("C4", C4);
        buttons.put("C5", C5);
        buttons.put("C6", C6);
        buttons.put("C7", C7);
        buttons.put("C8", C8);

        //Row 4
        buttons.put("D1", D1);
        buttons.put("D2", D2);
        buttons.put("D3", D3);
        buttons.put("D4", D4);
        buttons.put("D5", D5);
        buttons.put("D6", D6);
        buttons.put("D7", D7);
        buttons.put("D8", D8);

        //Row 5
        buttons.put("E1", E1);
        buttons.put("E2", E2);
        buttons.put("E3", E3);
        buttons.put("E4", E4);
        buttons.put("E5", E5);
        buttons.put("E6", E6);
        buttons.put("E7", E7);
        buttons.put("E8", E8);

        //Row 6
        buttons.put("F1", F1);
        buttons.put("F2", F2);
        buttons.put("F3", F3);
        buttons.put("F4", F4);
        buttons.put("F5", F5);
        buttons.put("F6", F6);
        buttons.put("F7", F7);
        buttons.put("F8", F8);

        //Row 7
        buttons.put("G1", G1);
        buttons.put("G2", G2);
        buttons.put("G3", G3);
        buttons.put("G4", G4);
        buttons.put("G5", G5);
        buttons.put("G6", G6);
        buttons.put("G7", G7);
        buttons.put("G8", G8);

        //Row 8
        buttons.put("H1", H1);
        buttons.put("H2", H2);
        buttons.put("H3", H3);
        buttons.put("H4", H4);
        buttons.put("H5", H5);
        buttons.put("H6", H6);
        buttons.put("H7", H7);
        buttons.put("H8", H8);
    }
}