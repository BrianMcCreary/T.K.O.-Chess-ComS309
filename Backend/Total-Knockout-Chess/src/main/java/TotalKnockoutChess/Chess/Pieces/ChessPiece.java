package TotalKnockoutChess.Chess.Pieces;

import java.util.List;

public abstract class ChessPiece {
    String color;

    public ChessPiece(String color){
        this.color = color;
    }
    abstract List<Coordinate> calculateAvailableMoves();
}
