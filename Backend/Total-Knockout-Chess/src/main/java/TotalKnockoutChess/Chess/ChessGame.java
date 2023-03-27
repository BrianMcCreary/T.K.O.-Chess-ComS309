package TotalKnockoutChess.Chess;

import TotalKnockoutChess.Chess.Pieces.*;
import TotalKnockoutChess.Users.User;

public class ChessGame {

    ChessGameTile[][] tiles;
    User white, black;

    private final int BOARD_WIDTH = 8;
    private final int BOARD_HEIGHT = 8;

    private final String TOP_COLOR = "black";
    private final String BOTTOM_COLOR = "white";

    public ChessGame(User white, User black){
        this.white = white;
        this.black = black;
        initializeGame();
    }

    // Helper method to initialize the game
    private void initializeGame() {
        // Create ChessGameTile's to fill "tiles" 2d array
        for(int row = 0; row < BOARD_HEIGHT; row++){
            for(int col = 0; col < BOARD_WIDTH; col++){
                tiles[col][row] = new ChessGameTile();
            }
        }

        // Fill bottom row of the board (row 1)
        createDefaultTopOrBottomRow(1, BOTTOM_COLOR);

        // Fill middle rows of the board (rows 2 - 7)
        for (int row = 1; row < BOARD_HEIGHT - 1; row++){
            for (int col = 0; col < BOARD_WIDTH; col++){
                tiles[col][row].piece = new Empty(); // Empty piece type has no color
            }
        }

        // Fill top row of the board (row 8)
        createDefaultTopOrBottomRow(7, TOP_COLOR);
    }

    // Helper method to generate default edge rows
    private void createDefaultTopOrBottomRow(int row, String color){
        tiles[0][row].piece = new Rook(color);
        tiles[1][row].piece = new Knight(color);
        tiles[2][row].piece = new Bishop(color);
        tiles[3][row].piece = new Queen(color);
        tiles[4][row].piece = new King(color);
        tiles[5][row].piece = new Bishop(color);
        tiles[6][row].piece = new Knight(color);
        tiles[7][row].piece = new Rook(color);
    }
}
