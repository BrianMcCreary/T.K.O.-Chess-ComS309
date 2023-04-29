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

    private Coordinate whiteKingPosition, blackKingPosition;

    private boolean running;

    //List of spectators in the game
    @ElementCollection
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
        whiteKingPosition = Coordinate.E1;

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
        blackKingPosition = Coordinate.E8;

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

        // Tile with King on same side as the moving piece
        ChessGameTile king = getKingTile(moving.piece.color);

        // Get the available moves for the piece attempting to move
        String availableMoves = moving.piece.calculateAvailableMoves(tiles, startCoordinate, (King) king.piece);

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

//        String checkOrCheckmate = previewMoveForCheckOrCheckMate(moving, endCoordinate);

        // Update the destination tile with the moved piece
        tiles[endCoordinate.x][endCoordinate.y].piece = moving.piece;

        // Update the starting tile with an empty piece
        tiles[startCoordinate.x][startCoordinate.y].piece = new Empty();

        ChessPiece movedPiece = tiles[endCoordinate.x][endCoordinate.y].piece;

        // If the moved piece was a king, update its fields accordingly
        if (movedPiece instanceof King) {
            ((King) movedPiece).setCanCastle(false);
            ((King) movedPiece).setCoordinate(endCoordinate);

            // Update the board's king location data
            if (movedPiece.color.equals("white")) {
                whiteKingPosition = endCoordinate;
            } else if (movedPiece.color.equals("black")) {
                blackKingPosition = endCoordinate;
            }
        }
        // If the moved piece was a rook, update its 'canCastle' field accordingly
        else if (movedPiece instanceof Rook) {
            ((Rook) movedPiece).setCanCastle(false);
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

    // Getters for players
    public String getWhitePlayer() {
        return whitePlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }

    // Getter for spectators
    public List<String> getSpectators() {
        return spectators;
    }

    // Getter for king's tile on the board
    public ChessGameTile getKingTile(String color) {
        // If prompted for white king's tile
        if (color.equals("white")) {
            return tiles[whiteKingPosition.x][whiteKingPosition.y];
        }
        // If prompted for black king's tile
        else if (color.equals("black")) {
            return tiles[blackKingPosition.x][blackKingPosition.y];
        }
        // If color is not white or black
        else {
            return null;
        }
    }

    // Getter for game state
    public boolean isRunning() {
        return running;
    }
}

