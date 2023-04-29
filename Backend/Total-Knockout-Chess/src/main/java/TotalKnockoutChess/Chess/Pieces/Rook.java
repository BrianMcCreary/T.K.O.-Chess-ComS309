package TotalKnockoutChess.Chess.Pieces;

import TotalKnockoutChess.Chess.ChessGameTile;

public class Rook extends ChessPiece {
    private static final long serialVersionUID = 0L;

    private boolean canCastle;

    public Rook(String color) {
        super(color);
        canCastle = true;
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
        return color + "Rook";
    }

    // Getter/Setter for canCastle field
    public boolean canCastle() { return canCastle; }
    public void setCanCastle(boolean canCastle) { this.canCastle = canCastle; }
}
