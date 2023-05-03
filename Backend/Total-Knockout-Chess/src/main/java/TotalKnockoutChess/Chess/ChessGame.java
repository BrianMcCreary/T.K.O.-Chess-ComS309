package TotalKnockoutChess.Chess;

import TotalKnockoutChess.Chess.Pieces.*;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;

@Entity
public class ChessGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private ChessGameTile[][] tiles;

    private final int BOARD_WIDTH = 8;
    private final int BOARD_HEIGHT = 8;

    private final int PROMOTION_RANK_INDEX = 7;

    private static final String TOP_COLOR = "black";
    private static final String BOTTOM_COLOR = "white";

    private String whoseMove;
    private String whiteFromSquare, blackFromSquare, whitePreviousMove, blackPreviousMove;

    private String whitePlayer, blackPlayer;

    private boolean whiteCheckMated, blackCheckMated;

    private static final HashMap<String, ChessPiece> promotionPieceTypes = new HashMap<>();

    static{
        promotionPieceTypes.put(TOP_COLOR + "Bishop", new Bishop(TOP_COLOR));    promotionPieceTypes.put(BOTTOM_COLOR + "Bishop", new Bishop(BOTTOM_COLOR));
        promotionPieceTypes.put(TOP_COLOR + "Rook", new Rook(TOP_COLOR));        promotionPieceTypes.put(BOTTOM_COLOR + "Rook", new Rook(BOTTOM_COLOR));
        promotionPieceTypes.put(TOP_COLOR + "Queen", new Queen(TOP_COLOR));      promotionPieceTypes.put(BOTTOM_COLOR + "Queen", new Queen(BOTTOM_COLOR));
        promotionPieceTypes.put(TOP_COLOR + "Knight", new Knight(TOP_COLOR));    promotionPieceTypes.put(BOTTOM_COLOR + "Knight", new Knight(BOTTOM_COLOR));
    }

    //List of spectators in the game
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> spectators;

    public ChessGame() {
    }

    public ChessGame(String whitePlayer, String blackPlayer, List<String> spectators) {
        this.spectators = spectators;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
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

        // Set default values for game variables
        whiteCheckMated = false;
        blackCheckMated = false;
        whoseMove = "white";
        whiteFromSquare = "";
        blackFromSquare = "";
        whitePreviousMove = "";
        blackPreviousMove = "";
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

        // The previous move of the opponent
        String opponentsPreviousMove = "";
        switch(tiles[startCoordinate.x][startCoordinate.y].piece.color){
            case "white":
                opponentsPreviousMove = blackPreviousMove;
                break;
            case "black":
                opponentsPreviousMove = whitePreviousMove;
                break;
        }

        // Find what tile contains the piece to move
        ChessGameTile moving = tiles[startCoordinate.x][startCoordinate.y];

        // Find the tile the piece wants to move to
        ChessGameTile destinationTile = tiles[endCoordinate.x][endCoordinate.y];

        // Get the available moves for the piece attempting to move
        String availableMoves = moving.piece.calculateAvailableMoves(tiles, startCoordinate, opponentsPreviousMove);

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

        // If move results in King being taken, update checkMated variables
        if(destinationTile.piece instanceof King){
            King king = (King)destinationTile.piece;
            king.setCheckMated(true);

            // Update game checkMated variable for appropriate king
            switch(king.color){
                case "white":
                    whiteCheckMated = true;
                    break;
                case "black":
                    blackCheckMated = true;
                    break;
            }
        }

        // Update the destination tile with the moved piece
        destinationTile.piece = moving.piece;

        // Update the starting tile with an empty piece
        tiles[startCoordinate.x][startCoordinate.y].piece = new Empty();

        ChessPiece movedPiece = destinationTile.piece;

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
        // If the moved piece was a pawn, check for en passant and check for promotion
        else if(movedPiece instanceof Pawn){
            Pawn pawn = (Pawn) movedPiece;
            String enPassantString = pawn.enPassantMove;

            // If enPassant occurred
            if( !enPassantString.equals("") ){

                // Clear the piece taken by en passant
                Coordinate enPassantMove = Coordinate.fromString(enPassantString);
                clearPiece(enPassantMove);

                // Clear enPassantMove variable
                pawn.enPassantMove = "";
            }

            // If pawn reaches promotion rank
            if(endCoordinate.y == PROMOTION_RANK_INDEX){
                pawn.canPromote = true;
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

    /**
     * Method that returns the ChessGameTile given a string version of a coordinate on the board
     * @param coordinate - String value of a coordinate on the board
     * @return - ChessGameTile corresponding to the coordinate given
     */
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

    // Getters/Setters for game state
    public boolean isWhiteCheckMated(){ return whiteCheckMated; }
    public void setWhiteCheckMated(boolean whiteCheckMated){ this.whiteCheckMated = whiteCheckMated; }
    public boolean isBlackCheckMated(){ return blackCheckMated; }
    public void setBlackCheckMated(boolean blackCheckMated){ this.blackCheckMated = blackCheckMated; }

    // Getters/Setters for previous moves
    public String getWhitePreviousMove(){ return whitePreviousMove; }
    public void setWhitePreviousMove(String whitePreviousMove){ this.whitePreviousMove = whitePreviousMove; }
    public String getBlackPreviousMove(){ return blackPreviousMove; }
    public void setBlackPreviousMove(String blackPreviousMove){ this.blackPreviousMove = blackPreviousMove; }

    // Method to set a tile to have an Empty object as its piece
    public void clearPiece(Coordinate coordinate){
        tiles[coordinate.x][coordinate.y].piece = new Empty();
    }

    // Method to set a tile to have the given piece as its piece
    public void setPiece(Coordinate coordinate, ChessPiece piece){
        tiles[coordinate.x][coordinate.y].piece = piece;
    }

    // Getter for promotion piece
    public ChessPiece getPromotionPiece(String promotionPiece){
        return promotionPieceTypes.get(promotionPiece);
    }
}

