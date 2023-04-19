package TotalKnockoutChess.Chess;

import TotalKnockoutChess.Chess.Pieces.*;
import TotalKnockoutChess.Lobby.Lobby;
import TotalKnockoutChess.Users.User;

import javax.persistence.*;

@Entity
public class ChessGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private ChessGameTile[][] tiles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lobby")
    Lobby lobby;

    private final int BOARD_WIDTH = 8;
    private final int BOARD_HEIGHT = 8;

    private final String TOP_COLOR = "black";
    private final String BOTTOM_COLOR = "white";

    public ChessGame(Lobby lobby) {
        this.lobby = lobby;
        initializeGame();
    }

    // Helper method to initialize the game
    private void initializeGame() {
        tiles = new ChessGameTile[BOARD_HEIGHT][BOARD_WIDTH];

        // Create ChessGameTile's to fill "tiles" 2d array
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                tiles[col][row] = new ChessGameTile();
            }
        }

        // Fill bottom row of the board (row 1)
        createDefaultTopOrBottomRow(0, BOTTOM_COLOR);

        // Fill middle rows of the board (rows 2 - 7)
        for (int row = 1; row < BOARD_HEIGHT - 1; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                tiles[col][row].piece = new Empty(); // Empty piece type has no color
            }
        }

        // Fill top row of the board (row 8)
        createDefaultTopOrBottomRow(7, TOP_COLOR);
    }

    // Helper method to generate default edge rows
    private void createDefaultTopOrBottomRow(int row, String color) {
        tiles[0][row].piece = new Rook(color);
        tiles[1][row].piece = new Knight(color);
        tiles[2][row].piece = new Bishop(color);
        tiles[3][row].piece = new Queen(color);
        tiles[4][row].piece = new King(color);
        tiles[5][row].piece = new Bishop(color);
        tiles[6][row].piece = new Knight(color);
        tiles[7][row].piece = new Rook(color);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters and setters for lobby field.
    public Lobby getLobby() { return lobby; }
    public void setLobby(Lobby lobby) { this.lobby = lobby; }

    /**
     * Method to update the ChessGameTiles array
     * @param startX - original x coordinate of the moving piece
     * @param startY - original y coordinate of the moving piece
     * @param endX   - new x coordinate of the moving piece
     * @param endY   - new y coordinate of the moving piece
     */
    public void makeMove(int startX, int startY, int endX, int endY){
        // If coordinates are out of bounds, return without updating the board
        if(startX < 0 || startY < 0 || endX < 0 || endY < 0 || startX >= BOARD_WIDTH || startY >= BOARD_HEIGHT || endX >= BOARD_WIDTH || endY >= BOARD_HEIGHT){
            return;
        }

        ChessGameTile movingPiece = tiles[startX][startY];
        if(movingPiece.getClass().equals(ChessGameTile.class)){

        }
    }
}
