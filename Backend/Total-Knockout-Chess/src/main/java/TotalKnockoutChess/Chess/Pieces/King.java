package TotalKnockoutChess.Chess.Pieces;

import java.util.List;

public class King extends ChessPiece {

    boolean checked, checkMated;

    public King(String color) {
        super(color);
        checked = false;
        checkMated = false;
    }

    List<Coordinate> calculateAvailableMoves() {

        return null;
    }

    boolean move(int fromX, int fromY, int toX, int toY){
        // TODO
        return true;
    }
}
