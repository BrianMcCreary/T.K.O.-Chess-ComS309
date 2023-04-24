package TotalKnockoutChess.Chess.Pieces;

import java.util.List;

public class Empty extends ChessPiece{

    public Empty() {
        super("");
    }

    List<Coordinate> calculateAvailableMoves() {
        return null;
    }

    boolean move(int fromX, int fromY, int toX, int toY) {
        return false;
    }
}
