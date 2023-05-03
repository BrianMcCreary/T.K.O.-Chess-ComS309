package TotalKnockoutChess.Chess.Pieces;

import TotalKnockoutChess.Chess.ChessGameTile;

public class Knight extends ChessPiece {
    private static final long serialVersionUID = 0L;

    public Knight(String color) {
        super(color);
    }

    @Override
    public String calculateAvailableMoves(ChessGameTile[][] board, Coordinate currentPosition) {
        String moves = "";
//        String sideColor = board[currentPosition.x][currentPosition.y].getPiece().color;
//
//        Coordinate  upLeft    = Coordinate.fromString(currentPosition.toString()),
//                    upRight   = Coordinate.fromString(currentPosition.toString()),
//                    downLeft  = Coordinate.fromString(currentPosition.toString()),
//                    downRight = Coordinate.fromString(currentPosition.toString());
//
//
//        // Initialize for up one, to the left two
//        int col = currentPosition.x - 2;
//        int row = currentPosition.y + 1;
//
//        // Checks square up one square and to the left two squares
//        upLeft = shiftCoordinate(upLeft, -2, 1);
//        ChessPiece piece = board[upLeft.x][upLeft.y].getPiece();
//
//        // If the coordinate holds an empty piece or a piece of the opposite color
//        if (isEmptyOrOpponentPiece(piece, sideColor)) {
//            // Add the coordinate as an available move
//            moves += upLeft + " ";
//        }
//
//        // Initialize for up one to the right two
//        col = currentPosition.x + 2;
//        row = currentPosition.y + 1;
//
//        upRight = shiftCoordinate(upRight, 2, 1);
//        piece = board[upRight.x][upRight.y].getPiece();
//
//        // If the coordinate holds an empty piece or a piece of the opposite color
//        if(isEmptyOrOpponentPiece(piece, sideColor)) {
//            // Add the coordinate as an available move
//            moves += upRight + " ";
//        }
//
//        // Initialize for down one to the left two
//        col = currentPosition.x - 1;
//        row = currentPosition.y - 1;
//
//        // Checks squares diagonally down and to the left of the current position
//        // until the edge of the board or a piece is hit
//        while (col >= 0 && row >= 0) {
//            downLeft = shiftCoordinate(downLeft, -1, -1);
//            ChessPiece piece = board[downLeft.x][downLeft.y].getPiece();
//
//            // If the coordinate holds another piece of the same color, break out of the loop
//            if (!(piece instanceof Empty) && sideColor.equals(piece.color)) {
//                break;
//            }
//
//            // Add the coordinate as an available move
//            moves += downLeft + " ";
//
//            // If the coordinate has an opponent's piece, break after adding to available moves
//            if (!(piece instanceof Empty)) {
//                break;
//            }
//
//            // Shift tile coordinate down one and left one
//            col--;
//            row--;
//        }
//
//        // Initialize for down to the right
//        col = currentPosition.x + 1;
//        row = currentPosition.y - 1;
//
//        // Checks squares diagonally down and to the right of the current position
//        // until the edge of the board or a piece is hit
//        while (col < board.length && row >= 0) {
//            downRight = shiftCoordinate(downRight, 1, -1);
//            ChessPiece piece = board[downRight.x][downRight.y].getPiece();
//
//            // If the coordinate holds another piece of the same color, break out of the loop
//            if (!(piece instanceof Empty) && sideColor.equals(piece.color)) {
//                break;
//            }
//
//            // Add the coordinate as an available move
//            moves += downRight + " ";
//
//            // If the coordinate has an opponent's piece, break after adding to available moves
//            if (!(piece instanceof Empty)) {
//                break;
//            }
//
//            // Shift tile coordinate down one and right one
//            col++;
//            row--;
//        }
//        System.out.println("Piece " + this + " at " + currentPosition + " has the following available moves.\n" + moves);
        return "";
    }

    @Override
    public final String toString() {
        return color + "Knight";
    }

    private boolean isEmptyOrOpponentPiece(ChessPiece piece, String sideColor){
        // If the coordinate holds an empty piece or a piece of the opposite color, return true
        if (piece instanceof Empty || !sideColor.equals(piece.color)) {
            return true;
        }

        // Otherwise, return false
        return false;
    }
}
