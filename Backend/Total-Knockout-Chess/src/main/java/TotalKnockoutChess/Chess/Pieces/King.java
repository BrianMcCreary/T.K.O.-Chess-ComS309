package TotalKnockoutChess.Chess.Pieces;

import TotalKnockoutChess.Chess.ChessGameTile;

public class King extends ChessPiece {
    private static final long serialVersionUID = 0L;

    private boolean checkMated, canCastle;

    private Coordinate coordinate;

    public King(String color, Coordinate coordinate) {
        super(color);
        checkMated = false;
        canCastle = true;
        this.coordinate = coordinate;
    }

    // Getter/Setter for the coordinate of the piece
    public Coordinate getCoordinate() {
        return coordinate;
    }
    public void setCoordinate(Coordinate updatedCoordinate) {
        coordinate = updatedCoordinate;
    }

    public String calculateAvailableMoves(ChessGameTile[][] board, Coordinate currentPosition, King king) {
        String moves = "";
//        Code to add every move as an available move for testing
        for(Coordinate c : Coordinate.values()){
            moves += c.toString() + " ";
        }


        return moves;
    }

    @Override
    public final String toString() {
        return color + "King";
    }

    // Getter/Setter for checkMated field
    public boolean isCheckMated(){ return checkMated; }
    public void setCheckMated(boolean checkMated){ this.checkMated = checkMated; }


    // Getter/Setter for canCastle field
    public boolean canCastle(){ return canCastle; }
    public void setCanCastle(boolean canCastle){ this.canCastle = canCastle; }
}
