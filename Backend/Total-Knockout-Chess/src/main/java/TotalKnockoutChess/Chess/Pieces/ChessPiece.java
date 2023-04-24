package TotalKnockoutChess.Chess.Pieces;

import TotalKnockoutChess.Chess.ChessGameTile;

import java.io.Serializable;
import java.util.List;

public abstract class ChessPiece implements Serializable {

    private static final long serialVersionUID = 0L;
    String color;

    public ChessPiece(String color) {
        this.color = color;
    }

    /**
     * Method that, when implemented, determines a piece's available moves
     * @param board - the board where this piece resides
     * @param currentPosition - the current position of the piece (as a Coordinate object) relative to the board
     * @return String that is space separated for each available move
     */
    public abstract String calculateAvailableMoves(ChessGameTile[][] board, Coordinate currentPosition);

    /**
     * Method that, when implemented, returns a string of an abbreviated version of the piece's type
     * @return String - abbreviated piece type in the format "W/B{firstLetterOrTwoOfPieceType}". For empty piece, "-". For black king, "BK". For white knight, "WKn", etc.
     */
    public abstract String toString();
}
