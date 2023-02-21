package com.coms309.demo1.ChessGame;

public class ChessGameTile {
    protected boolean hasPiece;
    protected String piece;

    protected ChessGameTile(boolean hasPiece, String piece){
        this.hasPiece = hasPiece;
        this.piece = piece;
    }
}
