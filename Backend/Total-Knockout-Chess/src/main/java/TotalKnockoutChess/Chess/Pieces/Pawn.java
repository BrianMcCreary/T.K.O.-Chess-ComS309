package TotalKnockoutChess.Chess.Pieces;

import TotalKnockoutChess.Chess.ChessGameTile;

public class Pawn extends ChessPiece{
    private static final long serialVersionUID = 0L;

    public Pawn(String color) {
        super(color);
    }

    public String calculateAvailableMoves(ChessGameTile[][] board, Coordinate currentPosition, King king) {
        String moves = "";
        for(Coordinate c : Coordinate.values()){
            moves += c.toString() + " ";
        }
        return moves;
    }

    @Override
    public final String toString() {
        return color + "Pawn";
    }
}
