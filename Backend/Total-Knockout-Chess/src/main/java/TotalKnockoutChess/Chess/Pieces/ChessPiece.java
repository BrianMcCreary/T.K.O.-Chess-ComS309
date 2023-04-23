package TotalKnockoutChess.Chess.Pieces;

import java.io.Serializable;
import java.util.List;

public abstract class ChessPiece implements Serializable {
    String color;

    public ChessPiece(String color) {
        this.color = color;
    }

    abstract List<Coordinate> calculateAvailableMoves();

    abstract boolean move(int fromX, int fromY, int toX, int toY);
}
