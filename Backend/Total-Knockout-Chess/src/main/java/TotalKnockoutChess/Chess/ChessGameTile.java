package TotalKnockoutChess.Chess;

import TotalKnockoutChess.Chess.Pieces.ChessPiece;

import java.io.Serializable;

public class ChessGameTile implements Serializable {
    private static final long serialVersionUID = 0L;

    ChessPiece piece;

    public ChessGameTile(){
    }

    /**
     * Getter method that returns the ChessPiece object on the tile
     * @return - the ChessPiece object on the tile
     */
    public ChessPiece getPiece() {
        return piece;
    }
}
