package TotalKnockoutChess.Chess.Pieces;

import TotalKnockoutChess.Chess.ChessGameTile;

public class King extends ChessPiece {
    private static final long serialVersionUID = 0L;

    private boolean checkMated, canCastle;

    private Coordinate coordinate;

    private String castleRook;

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
                    downRight = shiftCoordinate(currentPosition,  1, -1),
                    // Coordinates for castling
                    right2    = shiftCoordinate(currentPosition, 2, 0),
                    left2     = shiftCoordinate(currentPosition, -2, 0),
                    right3    = shiftCoordinate(currentPosition, 3, 0),
                    left3     = shiftCoordinate(currentPosition, -3, 0),
                    left4     = shiftCoordinate(currentPosition, -4, 0);

        // Check for castle
        switch(color){
            case "white":
                if(currentPosition.equals(Coordinate.E1) && canCastle){
                    // King side castle
                    ChessPiece f1Piece = board[right.x][right.y].getPiece(),
                               g1Piece = board[right2.x][right2.y].getPiece(),
                               h1Piece = board[right3.x][right3.y].getPiece();

                    // If two squares to the right are empty and the h1 rook is available to castle
                    if(f1Piece instanceof Empty && g1Piece instanceof Empty && (h1Piece instanceof Rook && h1Piece.color.equals(color) && ((Rook)h1Piece).canCastle())){
                        moves += right2 + " ";
                        castleRook = "whiteRook " + Coordinate.H1 + " " + right;
                    }

                    // Queen side castle
                    ChessPiece d1Piece = board[left.x][left.y].getPiece(),
                               c1Piece = board[left2.x][left2.y].getPiece(),
                               b1Piece = board[left3.x][left3.y].getPiece(),
                               a1Piece = board[left4.x][left4.y].getPiece();

                    // If 3 squares to the left are empty and the a1 rook is available to castle
                    if(d1Piece instanceof Empty && c1Piece instanceof Empty && b1Piece instanceof Empty && (a1Piece instanceof Rook && a1Piece.color.equals(color) && ((Rook)a1Piece).canCastle())){
                        moves += left2 + " ";
                        castleRook = "whiteRook " + Coordinate.A1 + " " + left;
                    }
                }
                break;
            case "black":
                if(currentPosition.equals(Coordinate.E8) && canCastle){
                    // King side castle
                    ChessPiece  f8Piece = board[right.x][right.y].getPiece(),
                                g8Piece = board[right2.x][right2.y].getPiece(),
                                h8Piece = board[right3.x][right3.y].getPiece();

                    // If two squares to the right are empty and the h1 rook is available to castle
                    if(f8Piece instanceof Empty && g8Piece instanceof Empty && (h8Piece instanceof Rook && h8Piece.color.equals(color) && ((Rook)h8Piece).canCastle())){
                        moves += right2 + " ";
                        castleRook = "blackRook " + Coordinate.H8 + " " + right;
                    }

                    // Queen side castle
                    ChessPiece  d8Piece = board[left.x][left.y].getPiece(),
                                c8Piece = board[left2.x][left2.y].getPiece(),
                                b8Piece = board[left3.x][left3.y].getPiece(),
                                a8Piece = board[left4.x][left4.y].getPiece();

                    // If 3 squares to the left are empty and the a1 rook is available to castle
                    if(d8Piece instanceof Empty && c8Piece instanceof Empty && b8Piece instanceof Empty && (a8Piece instanceof Rook && a8Piece.color.equals(color) && ((Rook)a8Piece).canCastle())){
                        moves += left2 + " ";
                        castleRook = "blackRook " + Coordinate.A8 + " " + left;
                    }
                }
        }

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

    // Getter for castleRook
    public String castleRook(){ return castleRook; }
}
