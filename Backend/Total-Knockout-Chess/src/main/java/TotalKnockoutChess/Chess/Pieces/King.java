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

    @Override
    public String calculateAvailableMoves(ChessGameTile[][] board, Coordinate currentPosition, String opponentsPreviousMove) {
        String moves = "";
        Coordinate  up        = shiftCoordinate(currentPosition,  0,  1),
                    down      = shiftCoordinate(currentPosition,  0, -1),
                    left      = shiftCoordinate(currentPosition, -1,  0),
                    right     = shiftCoordinate(currentPosition,  1,  0),
                    upLeft    = shiftCoordinate(currentPosition, -1,  1),
                    upRight   = shiftCoordinate(currentPosition,  1,  1),
                    downLeft  = shiftCoordinate(currentPosition, -1, -1),
                    downRight = shiftCoordinate(currentPosition,  1, -1);

        Coordinate[] nearby = {up, down, left, right, upLeft, upRight, downLeft, downRight};

        for(Coordinate c : nearby){
            // If the coordinate is on the board and the tile at the coordinates has no piece on it, or it has an opponent's piece on it.
            if(c != null && !board[c.x][c.y].getPiece().color.equals(color)){
                moves += c + " ";
            }
        }

        System.out.println("Piece " + this + " at " + currentPosition + " has the following available moves.\n" + moves);
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
