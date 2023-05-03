package TotalKnockoutChess.Chess.Pieces;

import TotalKnockoutChess.Chess.ChessGameTile;

public class Bishop extends ChessPiece {
    private static final long serialVersionUID = 0L;

    public Bishop(String color) {
        super(color);
    }

    @Override
    public String calculateAvailableMoves(ChessGameTile[][] board, Coordinate currentPosition) {
        String moves = "";
        String sideColor = board[currentPosition.x][currentPosition.y].getPiece().color;

        Coordinate  upLeft    = Coordinate.fromString(currentPosition.toString()),
                    upRight   = Coordinate.fromString(currentPosition.toString()),
                    downLeft  = Coordinate.fromString(currentPosition.toString()),
                    downRight = Coordinate.fromString(currentPosition.toString());


        // Initialize for up to the left
        int col = currentPosition.x - 1;
        int row = currentPosition.y + 1;

        // Checks squares diagonally up and to the left of the current position
        // until the edge of the board or a piece is hit
        while (col >= 0 && row < board.length) {
            upLeft = shiftCoordinate(upLeft, -1, 1);
            ChessPiece piece = board[upLeft.x][upLeft.y].getPiece();

            // If the coordinate holds another piece of the same color, break out of the loop
            if (!(piece instanceof Empty) && sideColor.equals(piece.color)) {
                break;
            }

            // Add the coordinate as an available move
            moves += upLeft + " ";

            // If the coordinate has an opponent's piece, break after adding to available moves
            if (!(piece instanceof Empty)) {
                break;
            }

            // Shift tile coordinate up one and left one
            col--;
            row++;
        }

        // Initialize for up to the right
        col = currentPosition.x + 1;
        row = currentPosition.y + 1;

        // Checks squares diagonally up and to the right of the current position
        // until the edge of the board or a piece is hit
        while (col < board.length && row < board.length) {
            upRight = shiftCoordinate(upRight, 1, 1);
            ChessPiece piece = board[upRight.x][upRight.y].getPiece();

            // If the coordinate holds another piece of the same color, break out of the loop
            if (!(piece instanceof Empty) && sideColor.equals(piece.color)) {
                break;
            }

            // Add the coordinate as an available move
            moves += upRight + " ";

            // If the coordinate has an opponent's piece, break after adding to available moves
            if (!(piece instanceof Empty)) {
                break;
            }

            // Shift tile coordinate up one and right one
            col++;
            row++;
        }

        // Initialize for down to the left
        col = currentPosition.x - 1;
        row = currentPosition.y - 1;

        // Checks squares diagonally down and to the left of the current position
        // until the edge of the board or a piece is hit
        while (col >= 0 && row >= 0) {
            downLeft = shiftCoordinate(downLeft, -1, -1);
            ChessPiece piece = board[downLeft.x][downLeft.y].getPiece();

            // If the coordinate holds another piece of the same color, break out of the loop
            if (!(piece instanceof Empty) && sideColor.equals(piece.color)) {
                break;
            }

            // Add the coordinate as an available move
            moves += downLeft + " ";

            // If the coordinate has an opponent's piece, break after adding to available moves
            if (!(piece instanceof Empty)) {
                break;
            }

            // Shift tile coordinate down one and left one
            col--;
            row--;
        }

        // Initialize for down to the right
        col = currentPosition.x + 1;
        row = currentPosition.y - 1;

        // Checks squares diagonally down and to the right of the current position
        // until the edge of the board or a piece is hit
        while (col < board.length && row >= 0) {
            downRight = shiftCoordinate(downRight, 1, -1);
            ChessPiece piece = board[downRight.x][downRight.y].getPiece();

            // If the coordinate holds another piece of the same color, break out of the loop
            if (!(piece instanceof Empty) && sideColor.equals(piece.color)) {
                break;
            }

            // Add the coordinate as an available move
            moves += downRight + " ";

            // If the coordinate has an opponent's piece, break after adding to available moves
            if (!(piece instanceof Empty)) {
                break;
            }

            // Shift tile coordinate down one and right one
            col++;
            row--;
        }
        System.out.println("Piece " + this + " at " + currentPosition + " has the following available moves.\n" + moves);
        return moves;
    }


    @Override
    public final String toString() {
        return color + "Bishop";
    }
}
