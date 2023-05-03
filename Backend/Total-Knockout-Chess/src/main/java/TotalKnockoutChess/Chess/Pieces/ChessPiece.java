package TotalKnockoutChess.Chess.Pieces;

import TotalKnockoutChess.Chess.ChessGameTile;

import java.io.Serializable;

import static java.lang.Integer.parseInt;

public abstract class ChessPiece implements Serializable {

    private static final long serialVersionUID = 0L;
    public String color;

    public ChessPiece(String color) {
        this.color = color;
    }

    /**
     * Method that, when implemented, determines a piece's available moves
     * @param board - the board where this piece resides
     * @param currentPosition - the current position of the piece (as a Coordinate object) relative to the board
     * @param opponentsPreviousMove - string value with element at index 0 as the piece type and element at index 1 as the destination coordinate
     * @return String that is space separated for each available move
     */
    public abstract String calculateAvailableMoves(ChessGameTile[][] board, Coordinate currentPosition, String opponentsPreviousMove);

    /**
     * Method that, when implemented, returns a string of an abbreviated version of the piece's type
     * @return String - abbreviated piece type in the format "W/B{firstLetterOrTwoOfPieceType}". For empty piece, "-". For black king, "BK". For white knight, "WKn", etc.
     */
    public abstract String toString();

    public Coordinate shiftCoordinate(Coordinate coord, int shiftX, int shiftY) {
        String coordinate = coord.toString();

        char letter = coordinate.charAt(0);
        int number = parseInt(coordinate.substring(1));

        if(shiftX != 0) {
            letter += shiftX;
        }

        if(shiftY != 0) {
           number += shiftY;
         }

        return Coordinate.fromString(letter + "" + number);
    }
}
