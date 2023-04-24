package TotalKnockoutChess.Chess.Pieces;

import TotalKnockoutChess.Chess.ChessGameTile;

public class Pawn extends ChessPiece{

    public Pawn(String color) {
        super(color);
    }

    public String calculateAvailableMoves(ChessGameTile[][] board, Coordinate currentPosition) {
        return null;
    }

    @Override
    public final String toString() {
        return color.charAt(0) + "P";
    }
}
