package TotalKnockoutChess.Chess.Pieces;

import TotalKnockoutChess.Chess.ChessGameTile;

public class Bishop extends ChessPiece {
    private static final long serialVersionUID = 0L;

    public Bishop(String color) {
        super(color);
    }

    public String calculateAvailableMoves(ChessGameTile[][] board, Coordinate currentPosition, King king) {
        String moves = "";
        String sideColor = board[currentPosition.x][currentPosition.y].getPiece().color;

        // TODO Replace with bishop movements

        Coordinate  upLeft      = currentPosition,
                    upRight     = currentPosition,
                    downLeft    = currentPosition,
                    downRight   = currentPosition;

        // Checks squares diagonally up and to the left of the current position
        // until the edge of the board or a piece is hit
        while(upLeft.x < board.length && upLeft.y < board.length){
            ChessPiece piece = board[upLeft.y][upLeft.x].getPiece();

            // If the coordinate holds another piece off the same color, break out of the loop
            if( !(piece instanceof Empty) && sideColor.equals(piece.color) ){
                break;
            }

            // Add the coordinate as an available move
            moves += upLeft + " ";

            // Shift tile coordinate up one and left one
            upLeft = Coordinate.shiftCoordinate(upLeft, 1, 1);
        }

        return moves;
    }
    
    @Override
    public final String toString() {
        return color + "Bishop";
    }
}
