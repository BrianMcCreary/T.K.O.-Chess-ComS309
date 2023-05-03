package TotalKnockoutChess.Chess;

import TotalKnockoutChess.Chess.Pieces.*;
import TotalKnockoutChess.Lobby.Lobby;
import TotalKnockoutChess.Users.User;

import javax.persistence.*;
import java.util.List;

@Entity
public class ChessGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private ChessGameTile[][] tiles;

    private final int BOARD_WIDTH = 8;
    private final int BOARD_HEIGHT = 8;

    private final String TOP_COLOR = "black";
    private final String BOTTOM_COLOR = "white";

    private String whoseMove;
    private String whiteFromSquare, blackFromSquare;

    private String whitePlayer, blackPlayer;

    private boolean running;

    //List of spectators in the game
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> spectators;

    public ChessGame() {
    }

    public ChessGame(String whitePlayer, String blackPlayer, List<String> spectators) {
        this.spectators = spectators;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        whoseMove = "white";
        whiteFromSquare = "";
        blackFromSquare = "";
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

        // Fill second bottommost row of the board (row 2)
        createPawnRow(1, BOTTOM_COLOR);

        // Fill middle rows of the board (rows 3 - 6)
        for (int row = 2; row < BOARD_HEIGHT - 2; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                tiles[col][row].piece = new Empty(); // Empty piece type has no color
            }
        }

        // Fill second topmost row of the board (row 2)
        createPawnRow(6, TOP_COLOR);


        // Fill top row of the board (row 8)
        createDefaultTopOrBottomRow(7, TOP_COLOR);

        running = true;
    }

    // Helper method to generate default pawn rows
    private void createPawnRow(int row, String color) {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            tiles[i][row].piece = new Pawn(color);
        }
    }

    // Helper method to generate default edge rows
    private void createDefaultTopOrBottomRow(int row, String color) {
        tiles[0][row].piece = new Rook(color);
        tiles[1][row].piece = new Knight(color);
        tiles[2][row].piece = new Bishop(color);
        tiles[3][row].piece = new Queen(color);


        // For the king piece, provide the constructor with information of starting position
        if (row == 0) {
            tiles[4][row].piece = new King(color, Coordinate.E1);
        } else if (row == 7) {
            tiles[4][row].piece = new King(color, Coordinate.E8);
        }

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

    /**
     * Method to update the ChessGameTiles array
     *
     * @param startCoordinate - original Coordinate of the moving piece
     * @param endCoordinate   - destination Coordinate of the moving piece
     * @return true if the update was successful, false otherwise
     */
    public boolean makeMove(Coordinate startCoordinate, Coordinate endCoordinate) {
        // If coordinates are out of bounds, return without updating the board
        if (startCoordinate.x < 0 || startCoordinate.y < 0 || endCoordinate.x < 0 || endCoordinate.y < 0 || startCoordinate.x > BOARD_WIDTH - 1 || startCoordinate.y > BOARD_HEIGHT - 1 || endCoordinate.x > BOARD_WIDTH - 1 || endCoordinate.y > BOARD_HEIGHT - 1) {
            return false;
        }

        // Find what tile contains the piece to move
        ChessGameTile moving = tiles[startCoordinate.x][startCoordinate.y];

        // Get the available moves for the piece attempting to move
        String availableMoves = moving.piece.calculateAvailableMoves(tiles, startCoordinate);

        // Check if the move matches any of the available moves for the piece
        boolean validMove = false;

        for (String move : availableMoves.split(" ")) {
            if (move.equals(endCoordinate.toString())) {
                validMove = true;
            }
        }

        // If the move was not found in the piece's available moves, return false
        if (!validMove) {
            return false;
        }

        // Update the destination tile with the moved piece
        tiles[endCoordinate.x][endCoordinate.y].piece = moving.piece;

        // Update the starting tile with an empty piece
        tiles[startCoordinate.x][startCoordinate.y].piece = new Empty();

        ChessPiece movedPiece = tiles[endCoordinate.x][endCoordinate.y].piece;

        // If the moved piece was a king, update its fields accordingly
        if (movedPiece instanceof King) {
            ((King) movedPiece).setCanCastle(false);
            ((King) movedPiece).setCoordinate(endCoordinate);

            // If move was a castle, update the rook's position
            switch(startCoordinate.toString()){
                // White King
                case "E1":
                    // This piece went on C1 (white queen's side castle)
                    if(endCoordinate.equals(Coordinate.C1)) {
                        tiles[Coordinate.D1.x][Coordinate.D1.y].piece = tiles[Coordinate.A1.x][Coordinate.A1.y].piece; // Move rook with castle
                        tiles[Coordinate.A1.x][Coordinate.A1.y].piece = new Empty(); // Set where the castled rook was to empty
                    }
                    // This piece went on G1 (white king's side castle)
                    else if(endCoordinate.equals(Coordinate.G1)){
                        tiles[Coordinate.F1.x][Coordinate.F1.y].piece = tiles[Coordinate.H1.x][Coordinate.H1.y].piece; // Move rook with castle
                        tiles[Coordinate.H1.x][Coordinate.H1.y].piece = new Empty(); // Set where the castled rook was to empty
                    }
                    break;
                // Black King
                case "E8":
                    // This piece went on C8 (black queen's side castle)
                    if(endCoordinate.equals(Coordinate.C8)) {
                        tiles[Coordinate.D8.x][Coordinate.D8.y].piece = tiles[Coordinate.A8.x][Coordinate.A8.y].piece; // Move rook with castle
                        tiles[Coordinate.A8.x][Coordinate.A8.y].piece = new Empty(); // Set where the castled rook was to empty
                    }
                    // This piece went on G8 (black king's side castle)
                    else if(endCoordinate.equals(Coordinate.G8)){
                        tiles[Coordinate.F8.x][Coordinate.F8.y].piece = tiles[Coordinate.H8.x][Coordinate.H8.y].piece; // Move rook with castle
                        tiles[Coordinate.H8.x][Coordinate.H8.y].piece = new Empty(); // Set where the castled rook was to empty
                    }
                    break;
            }

        }
        // If the moved piece was a rook, update its 'canCastle' field accordingly
        else if (movedPiece instanceof Rook) {
            ((Rook) movedPiece).setCanCastle(false);

            // If move was a castle, update the king's position
            switch(startCoordinate.toString()){
                // White Rook
                case "A1":
                    // This piece went on D1 (white queen's side castle)
                    if(endCoordinate.equals(Coordinate.D1)) {
                        tiles[Coordinate.C1.x][Coordinate.C1.y].piece = tiles[Coordinate.E1.x][Coordinate.E1.y].piece; // Move king with castle
                        tiles[Coordinate.E1.x][Coordinate.E1.y].piece = new Empty(); // Set where the castled king was to empty
                    }
                case "H1":
                    // This piece went on G1 (white king's side castle)
                    if(endCoordinate.equals(Coordinate.F1)){
                        tiles[Coordinate.G1.x][Coordinate.G1.y].piece = tiles[Coordinate.E1.x][Coordinate.E1.y].piece; // Move king with castle
                        tiles[Coordinate.E1.x][Coordinate.E1.y].piece = new Empty(); // Set where the castled king was to empty
                    }
                    break;
                // Black Rook
                case "A8":
                    // This piece went on D8 (black queen's side castle)
                    if(endCoordinate.equals(Coordinate.D8)) {
                        tiles[Coordinate.C8.x][Coordinate.C8.y].piece = tiles[Coordinate.C8.x][Coordinate.C8.y].piece; // Move king with castle
                        tiles[Coordinate.C8.x][Coordinate.C8.y].piece = new Empty(); // Set where the castled king was to empty
                    }
                    break;
                case "H8":
                    // This piece went on F8 (black king's side castle)
                    if(endCoordinate.equals(Coordinate.F8)) {
                        tiles[Coordinate.G8.x][Coordinate.G8.y].piece = tiles[Coordinate.E8.x][Coordinate.E8.y].piece; // Move king with castle
                        tiles[Coordinate.E8.x][Coordinate.E8.y].piece = new Empty(); // Set where the castled king was to empty
                    }
                    break;
                }
            }

        return true;
    }

    // Getter for the current board
    public ChessGameTile[][] getBoard() {
        return tiles;
    }

    // Method to display a text version of the current board
    public void displayBoard() {
        System.out.println("\t\ta\t\t\tb\t\t\tc\t\t\td\t\t\te\t\t\tf\t\t\tg\t\t\th");
        System.out.print("------------------------------------------------");
        System.out.println("------------------------------------------------");

        for (int row = BOARD_HEIGHT - 1; row >= 0; row--) {
            System.out.print(row + 1);
            for (int col = 0; col < BOARD_WIDTH; col++) {
                System.out.print("\t" + tiles[col][row].piece.toString());
            }
            System.out.println("\t" + (row + 1));
        }

        System.out.print("------------------------------------------------");
        System.out.println("------------------------------------------------");
        System.out.println("\t\ta\t\t\tb\t\t\tc\t\t\td\t\t\te\t\t\tf\t\t\tg\t\t\th");
    }

    // Getters/Setters for player moves
    public String getWhoseMove() {
        return whoseMove;
    }

    public void setWhoseMove(String whoseMove) {
        this.whoseMove = whoseMove;
    }

    public String getWhiteFromSquare() {
        return whiteFromSquare;
    }

    public void setWhiteFromSquare(String whiteFromSquare) {
        this.whiteFromSquare = whiteFromSquare;
    }

    public String getBlackFromSquare() {
        return blackFromSquare;
    }

    public void setBlackFromSquare(String blackFromSquare) {
        this.blackFromSquare = blackFromSquare;
    }

    // Getter for a specific tile on board
    public ChessGameTile getTile(String coordinate) {
        Coordinate coord = Coordinate.fromString(coordinate);

        return tiles[coord.x][coord.y];
    }

    // Getters/Setters for players
    public String getWhitePlayer() {
        return whitePlayer;
    }
    public String getBlackPlayer() {
        return blackPlayer;
    }
    public void setWhitePlayer(String whitePlayer){ this.whitePlayer = whitePlayer; }
    public void setBlackPlayer(String blackPlayer){ this.blackPlayer = blackPlayer; }

    // Getter for spectators
    public List<String> getSpectators() {
        return spectators;
    }

    // Getter/Setter for game state
    public boolean isRunning(){ return running; }
    public void setRunning(boolean running){ this.running = running; }
}

