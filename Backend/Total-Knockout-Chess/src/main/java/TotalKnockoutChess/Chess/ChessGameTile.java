package TotalKnockoutChess.Chess;

import TotalKnockoutChess.Chess.Pieces.ChessPiece;

import java.io.Serializable;

public class ChessGameTile implements Serializable {
    // boolean hasWhite, hasBlack; ChessPiece has a string "color" variable for this.
    ChessPiece piece;

    public ChessGameTile(){
    }
}
