package com.coms309.demo1.ChessGame;


import com.coms309.demo1.Players.Player;

import java.util.ArrayList;

public class ChessGame {

    protected int id;
//    protected String whitePlayer;
//    protected String blackPlayer;

    ArrayList<ChessGameTile> gameTiles;

    public ChessGame(int id/**, String whitePlayer, String blackPlayer*/) {
        this.id = id;
//        this.whitePlayer = whitePlayer;
//        this.blackPlayer = blackPlayer;
        initialize();
    }

    public void initialize() {
        gameTiles = new ArrayList<ChessGameTile>();

        gameTiles.add(new ChessGameTile(true, "rook"));
        gameTiles.add(new ChessGameTile(true, "knight"));
        gameTiles.add(new ChessGameTile(true, "bishop"));
        gameTiles.add(new ChessGameTile(true, "queen"));
        gameTiles.add(new ChessGameTile(true, "king"));
        gameTiles.add(new ChessGameTile(true, "bishop"));
        gameTiles.add(new ChessGameTile(true, "knight"));
        gameTiles.add(new ChessGameTile(true, "rook"));
        for(int i = 0; i < 8; i++){
            gameTiles.add(new ChessGameTile(true, "pawn"));
        }
        for(int i = 0; i < 32; i++){
            gameTiles.add(new ChessGameTile(false, "empty"));
        }
        for(int i = 0; i < 8; i++){
            gameTiles.add(new ChessGameTile(true, "pawn"));
        }
        gameTiles.add(new ChessGameTile(true, "rook"));
        gameTiles.add(new ChessGameTile(true, "knight"));
        gameTiles.add(new ChessGameTile(true, "bishop"));
        gameTiles.add(new ChessGameTile(true, "queen"));
        gameTiles.add(new ChessGameTile(true, "king"));
        gameTiles.add(new ChessGameTile(true, "bishop"));
        gameTiles.add(new ChessGameTile(true, "knight"));
        gameTiles.add(new ChessGameTile(true, "rook"));
    }

    @Override
    public String toString(){
        String board = "";
        int tileIndex = 0;

        // Convert ArrayList of ChessGameTiles to String
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                board += gameTiles.get(tileIndex).piece;
                tileIndex++;

                // Add a space inbetween each piece until the last piece of the row
                if(col < 7){
                    board += "\t";
                }
            }
            // Jump to new line for the next row
            board += "\n";
        }
        return board;
    }
}
