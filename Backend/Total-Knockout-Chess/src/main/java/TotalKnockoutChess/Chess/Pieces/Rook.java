package TotalKnockoutChess.Chess.Pieces;

import TotalKnockoutChess.Chess.ChessGameTile;

public class Rook extends ChessPiece {
    private static final long serialVersionUID = 0L;

    private boolean canCastle;

    public Rook(String color) {
        super(color);
        canCastle = true;
    }

    @Override
    public String calculateAvailableMoves(ChessGameTile[][] board, Coordinate currentPosition) {
        String moves = "";
        String sideColor = board[currentPosition.x][currentPosition.y].getPiece().color;

        Coordinate  up      = Coordinate.fromString(currentPosition.toString()),
                    right   = Coordinate.fromString(currentPosition.toString()),
                    left    = Coordinate.fromString(currentPosition.toString()),
                    down    = Coordinate.fromString(currentPosition.toString());


        // Initialize for up
        int col = currentPosition.x;
        int row = currentPosition.y + 1;

        // Checks squares above the current position
        // until the edge of the board or a piece is hit
        while (row < board.length) {
            up = shiftCoordinate(up, 0, 1);
            ChessPiece piece = board[up.x][up.y].getPiece();

            // If the coordinate holds another piece of the same color, break out of the loop
            if (!(piece instanceof Empty) && sideColor.equals(piece.color)) {
                break;
            }

            // Add the coordinate as an available move
            moves += up + " ";

            // If the coordinate has an opponent's piece, break after adding to available moves
            if (!(piece instanceof Empty)) {
                break;
            }

            // Shift tile coordinate up one
            row++;
        }

        // Initialize for to the right
        col = currentPosition.x + 1;
        row = currentPosition.y;

        // Checks squares to the right of the current position
        // until the edge of the board or a piece is hit
        while (col < board.length) {
            right = shiftCoordinate(right, 1, 0);
            ChessPiece piece = board[right.x][right.y].getPiece();

            // If move is a castle
            if ((piece instanceof King) && sideColor.equals(piece.color) && this.canCastle && ((King) piece).canCastle()){
                switch(((King) piece).getCoordinate().toString()){
                    // White King
                    case "E1":
                        // This piece is on A1
                        if(currentPosition.equals(Coordinate.A1)) {
                            moves += Coordinate.D1 + " "; // Long castle on the queen's side for white
                            continue;
                        }
                        break;
                    // Black King
                    case "E8":
                        // This piece is on A8
                        if(currentPosition.equals(Coordinate.A8)) {
                            moves += Coordinate.D8 + " "; // Long castle on the queen's side for black
                            continue;
                        }
                        break;
                }
            }

            // If the coordinate holds another piece of the same color, break out of the loop
            if (!(piece instanceof Empty) && sideColor.equals(piece.color)) {
                break;
            }

            // Add the coordinate as an available move
            moves += right + " ";

            // If the coordinate has an opponent's piece, break after adding to available moves
            if (!(piece instanceof Empty)) {
                break;
            }

            // Shift tile coordinate right one
            col++;
        }

        // Initialize for down
        col = currentPosition.x;
        row = currentPosition.y - 1;

        // Checks squares below the current position
        // until the edge of the board or a piece is hit
        while (row >= 0) {
            down = shiftCoordinate(down, 0, -1);
            ChessPiece piece = board[down.x][down.y].getPiece();

            // If the coordinate holds another piece of the same color, break out of the loop
            if (!(piece instanceof Empty) && sideColor.equals(piece.color)) {
                break;
            }

            // Add the coordinate as an available move
            moves += down + " ";

            // If the coordinate has an opponent's piece, break after adding to available moves
            if (!(piece instanceof Empty)) {
                break;
            }

            // Shift tile coordinate down one
            row--;
        }

        // Initialize for to the left
        col = currentPosition.x - 1;
        row = currentPosition.y;

        // Checks squares to the left of the current position
        // until the edge of the board or a piece is hit
        while (col >= 0) {
            left = shiftCoordinate(left, -1, 0);
            ChessPiece piece = board[left.x][left.y].getPiece();

            // If move is a castle
            if ((piece instanceof King) && sideColor.equals(piece.color) && this.canCastle && ((King) piece).canCastle()){
                switch(((King) piece).getCoordinate().toString()){
                    // White King
                    case "E1":
                        // This piece is on H1
                        if(currentPosition.equals(Coordinate.H1)){
                            moves += Coordinate.F1 + " "; // Short castle on the king's side for white
                            continue;
                        }
                        break;
                    // Black King
                    case "E8":
                        // This piece is on H8
                        if(currentPosition.equals(Coordinate.H8)){
                            moves += Coordinate.F8 + " "; // Short castle on the king's side for black
                            continue;
                        }
                        break;
                }
            }

            // If the coordinate holds another piece of the same color, break out of the loop
            if (!(piece instanceof Empty) && sideColor.equals(piece.color)) {
                break;
            }

            // Add the coordinate as an available move
            moves += left + " ";

            // If the coordinate has an opponent's piece, break after adding to available moves
            if (!(piece instanceof Empty)) {
                break;
            }

            // Shift tile coordinate left one
            col--;
        }
        System.out.println("Piece " + this + " at " + currentPosition + " has the following available moves.\n" + moves);
        return moves;
    }

    @Override
    public final String toString() {
        return color + "Rook";
    }

    // Getter/Setter for canCastle field
    public boolean canCastle() { return canCastle; }
    public void setCanCastle(boolean canCastle) { this.canCastle = canCastle; }
}
