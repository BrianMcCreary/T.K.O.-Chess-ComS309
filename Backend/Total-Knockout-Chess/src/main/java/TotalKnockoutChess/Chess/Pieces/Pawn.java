package TotalKnockoutChess.Chess.Pieces;

import TotalKnockoutChess.Chess.ChessGameTile;

public class Pawn extends ChessPiece{
    private static final long serialVersionUID = 0L;
    public boolean canPromote = false, enPassantOccured = true;

    public Pawn(String color) {
        super(color);
    }

    @Override
    public String calculateAvailableMoves(ChessGameTile[][] board, Coordinate currentPosition, String opponentsPreviousMove) {
        String moves = "";

        switch(color){
            case "white":
                moves += whitePawnMovement(board, currentPosition, opponentsPreviousMove);
                break;
            case "black":
                moves += blackPawnMovement(board, currentPosition, opponentsPreviousMove);
                break;
        }
        System.out.println("Piece " + this + " at " + currentPosition + " has the following available moves.\n" + moves);
        return moves;
    }

    // Helper method to distinguish black pawn movement
    private String blackPawnMovement(ChessGameTile[][] board, Coordinate currentPosition, String opponentsPreviousMove) {
        String moves = "";
        ChessPiece piece;
        Coordinate  below, enPassant;

        // Opponents piece information split up into specific variables
        String[] opponentsPreviousMoveInfo       = opponentsPreviousMove.split(" ");
        String   opponentsPreviousMovePiece      = opponentsPreviousMoveInfo[0],
                 opponentsPreviousMoveCoordinate = opponentsPreviousMoveInfo[1];

        // Coordinate object for opponent's previous move coordinates
        Coordinate opponentsPreviousMoveCoordinates = Coordinate.fromString(opponentsPreviousMoveCoordinate);

        // Pawn hasn't moved, so it can move forward two squares in one move
        if(currentPosition.y == 6){
            moves += shiftCoordinate(currentPosition, 0, -2) + " ";
        }

        // If the opponent moved one of their pawns up two, and is next to this pawn (En passant rule)
        if(!opponentsPreviousMovePiece.equals("") && opponentsPreviousMovePiece.substring(5).equals("Pawn") && nextTo(currentPosition, opponentsPreviousMoveCoordinates)){
            enPassant = shiftCoordinate(opponentsPreviousMoveCoordinates, 0, -1);
            moves += enPassant + " ";
            enPassantOccured = true;
        }

        // Potential coordinates on the board. Down one from current position
        int x = currentPosition.x, y = currentPosition.y - 1;

        // Make sure indexes are within the bounds of the board
        if((x >= 0 && x < board.length) && (y >= 0 && y < board.length)) {

            // If the tile directly below this piece is empty
            piece = board[x][y].getPiece();
            if (piece instanceof Empty) {
                below = shiftCoordinate(currentPosition, 0, -1);
                moves += below + " ";
            }
        }

        // Potential coordinates on the board. Down one, left one from current position
        x = currentPosition.x - 1; y = currentPosition.y - 1;

        // Make sure indexes are within the bounds of the board
        if((x >= 0 && x < board.length) && (y >= 0 && y < board.length)) {

            // If tile 1 below and 1 left of this pawn, is an opponent's piece
            piece = board[x][y].getPiece();
            if (board[x][y].getPiece().color != color && !(board[x][y].getPiece() instanceof Empty)) {
                below = shiftCoordinate(currentPosition, -1, -1);
                moves += below + " ";
            }
        }

        // Potential coordinates on the board. Down one, right one from current position
        x = currentPosition.x + 1; y = currentPosition.y - 1;

        // Make sure indexes are within the bounds of the board
        if((x >= 0 && x < board.length) && (y >= 0 && y < board.length)) {

            // If tile 1 below and 1 right of this pawn, is an opponent's piece
            if (board[x][y].getPiece().color != color && !(board[x][y].getPiece() instanceof Empty)) {
                below = shiftCoordinate(currentPosition, 1, -1);
                moves += below + " ";
            }
        }

        return moves;
    }

    // Helper method to distinguish white pawn movement
    private String whitePawnMovement(ChessGameTile[][] board, Coordinate currentPosition, String opponentsPreviousMove) {
        String moves = "";
        Coordinate  above, enPassant;
        ChessPiece piece;

        // Opponents piece information split up into specific variables
        String[] opponentsPreviousMoveInfo       = opponentsPreviousMove.split(" ");
        String   opponentsPreviousMovePiece      = opponentsPreviousMoveInfo[0];
        String opponentsPreviousMoveCoordinate = "";
        if(opponentsPreviousMoveInfo.length > 1) {
            opponentsPreviousMoveCoordinate = opponentsPreviousMoveInfo[1];
        }

        // Coordinate object for opponent's previous move coordinates
        Coordinate opponentsPreviousMoveCoordinates = Coordinate.fromString(opponentsPreviousMoveCoordinate);

        // Pawn hasn't moved, so it can move forward two squares in one move
        if(currentPosition.y == 1){
            moves += shiftCoordinate(currentPosition, 0, 2) + " ";
        }

        // If the opponent moved one of their pawns up two, and is next to this pawn (En passant rule)
        if(!opponentsPreviousMovePiece.equals("") && opponentsPreviousMovePiece.substring(5).equals("Pawn") && nextTo(currentPosition, opponentsPreviousMoveCoordinates)){
            enPassant = shiftCoordinate(opponentsPreviousMoveCoordinates, 0, 1);
            moves += enPassant + " ";
            enPassantOccured = true;
        }

        // Potential coordinates on the board. Up one from current position
        int x = currentPosition.x, y = currentPosition.y + 1;

        // Make sure indexes are within the bounds of the board
        if((x >= 0 && x < board.length) && (y >= 0 && y < board.length)) {

            // If the tile directly above this piece is empty
            piece = board[x][y].getPiece();
            if (piece instanceof Empty) {
                above = shiftCoordinate(currentPosition, 0, 1);
                moves += above + " ";
            }
        }

        // Potential coordinates on the board. Up one, left one from current position
        x = currentPosition.x - 1; y = currentPosition.y + 1;

        // Make sure indexes are within the bounds of the board
        if((x >= 0 && x < board.length) && (y >= 0 && y < board.length)) {

            // If tile 1 above and 1 left of this pawn, is an opponent's piece
            piece = board[x][y].getPiece();
            if (board[x][y].getPiece().color != color && !(board[x][y].getPiece() instanceof Empty)) {
                above = shiftCoordinate(currentPosition, -1, 1);
                moves += above + " ";
            }
        }

        // Potential coordinates on the board. Up one, right one from current position
        x = currentPosition.x + 1; y = currentPosition.y + 1;

        // Make sure indexes are within the bounds of the board
        if((x >= 0 && x < board.length) && (y >= 0 && y < board.length)) {

            // If tile 1 above and 1 right of this pawn, is an opponent's piece
            if (board[x][y].getPiece().color != color && !(board[x][y].getPiece() instanceof Empty)) {
                above = shiftCoordinate(currentPosition, 1, 1);
                moves += above + " ";
            }
        }

        return moves;
    }

    // Helper method that returns whether the currentPosition is horizontally adjacent to the opponentsPreviousMoveCoordinate
    private boolean nextTo(Coordinate currentPosition, Coordinate opponentsPreviousMoveCoordinate) {
        // Pieces are not on the same row
        if(currentPosition.y != opponentsPreviousMoveCoordinate.y){
            return false;
        }

        // Pieces are on the same row and directly next each other in terms of columns
        if(Math.abs(currentPosition.x - opponentsPreviousMoveCoordinate.x) == 1){
            return true;
        }

        // Pieces are on the same row, but not directly one column away
        return false;
    }

    @Override
    public final String toString() {
        return color + "Pawn";
    }
}
