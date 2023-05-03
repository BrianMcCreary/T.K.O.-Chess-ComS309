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
        System.out.println("--Setting-- Col: " + col + ". Row: " + row);

        // Checks squares above the current position
        // until the edge of the board or a piece is hit
        while (row < board.length) {
            up = shiftCoordinate(up, 0, 1);
            ChessPiece piece = board[up.x][up.y].getPiece();

            // If the coordinate holds another piece of the same color, break out of the loop
            if (!(piece instanceof Empty) && sideColor.equals(piece.color)) {
                System.out.println("Piece is taken by another friendly piece");
                break;
            }
            System.out.println("Piece is not taken by another friendly piece");

            // Add the coordinate as an available move
            moves += up + " ";

            // If the coordinate has an opponent's piece, break after adding to available moves
            if (!(piece instanceof Empty)) {
                System.out.println("Piece is taken by the opponent's piece");
                break;
            }
            System.out.println("Piece is not taken by the opponent's piece");

            // Shift tile coordinate up one
            row++;
            System.out.println("Col: " + col + ". Row: " + row);
        }

        // Initialize for to the right
        col = currentPosition.x + 1;
        row = currentPosition.y;
        System.out.println("--Setting-- Col: " + col + ". Row: " + row);

        // Checks squares to the right of the current position
        // until the edge of the board or a piece is hit
        while (col < board.length) {
            right = shiftCoordinate(right, 1, 0);
            ChessPiece piece = board[right.x][right.y].getPiece();

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
            System.out.println("Col: " + col + ". Row: " + row);
        }

        // Initialize for down
        col = currentPosition.x;
        row = currentPosition.y - 1;
        System.out.println("--Setting-- Col: " + col + ". Row: " + row);

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
            System.out.println("Col: " + col + ". Row: " + row);
        }

        // Initialize for to the left
        col = currentPosition.x - 1;
        row = currentPosition.y;
        System.out.println("--Setting-- Col: " + col + ". Row: " + row);

        // Checks squares to the left of the current position
        // until the edge of the board or a piece is hit
        while (col >= 0) {
            left = shiftCoordinate(left, -1, 0);
            ChessPiece piece = board[left.x][left.y].getPiece();

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
            System.out.println("Col: " + col + ". Row: " + row);
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
