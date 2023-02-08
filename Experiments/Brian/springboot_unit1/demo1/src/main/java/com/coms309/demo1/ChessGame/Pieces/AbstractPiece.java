package com.coms309.demo1.ChessGame.Pieces;

import java.util.ArrayList;

public abstract class AbstractPiece {
    /** Coordinate object type representing the position of the piece on the board.*/
    private Coordinate position;
    /** ArrayList of Coordinate object types representing positions on the Chess Board.*/
    private ArrayList<Coordinate> availableMoves;

    /** Method to calculate all possible moves based on this piece's position.*/
    abstract ArrayList<int[][]> findAvailableMoves();

    /** Method that provides the piece's position.*/
    Coordinate getPosition(){
        return position;
    }

    /** Method that updates the position of the piece.
     * @param position - Coordinate object type
     */
    void setPosition(Coordinate position){
        this.position = position;
    }



}
