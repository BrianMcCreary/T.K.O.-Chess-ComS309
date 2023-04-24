package TotalKnockoutChess.Chess.Pieces;

import TotalKnockoutChess.Chess.ChessGameTile;

public class Rook extends ChessPiece {

    public Rook(String color) {
        super(color);
    }

    public String calculateAvailableMoves(ChessGameTile[][] board, Coordinate currentPosition) {
        return "B2";
    }

    @Override
    public final String toString() {
        return color.charAt(0) + "R";
    }
}
