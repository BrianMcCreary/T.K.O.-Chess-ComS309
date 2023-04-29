package TotalKnockoutChess.Chess;

import TotalKnockoutChess.Chess.Pieces.ChessPiece;

import java.io.Serializable;

public class ChessGameTile implements Serializable {
    // boolean hasWhite, hasBlack; ChessPiece has a string "color" variable for this.
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

    /**
     * Setter method that updates the ChessPiece object on the tile
     */
    public void setPiece(ChessPiece piece) {
        this.piece = piece;
    }
}
