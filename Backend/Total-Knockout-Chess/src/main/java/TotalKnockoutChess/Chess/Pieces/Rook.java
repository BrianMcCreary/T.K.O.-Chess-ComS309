package TotalKnockoutChess.Chess.Pieces;

import java.util.List;

public class Rook extends ChessPiece {

    public Rook(String color) {
        super(color);
    }

    List<Coordinate> calculateAvailableMoves() {
        return null;
    }

    boolean move(int fromX, int fromY, int toX, int toY) {
        return false;
    }
}
