package TotalKnockoutChess.Chess.Pieces;

import TotalKnockoutChess.Chess.ChessGameTile;

public class Empty extends ChessPiece{
    private static final long serialVersionUID = 0L;

    public Empty() {
        super("");
    }

    @Override
    public String calculateAvailableMoves(ChessGameTile[][] board, Coordinate currentPosition) {
        return "";
    }

    @Override
    public final String toString() {
        return color + "--------";
    }
}
