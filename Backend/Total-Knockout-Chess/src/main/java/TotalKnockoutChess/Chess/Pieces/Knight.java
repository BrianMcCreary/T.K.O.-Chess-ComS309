package TotalKnockoutChess.Chess.Pieces;

import TotalKnockoutChess.Chess.ChessGameTile;

public class Knight extends ChessPiece {
    private static final long serialVersionUID = 0L;

    public Knight(String color) {
        super(color);
    }

    @Override
    public String calculateAvailableMoves(ChessGameTile[][] board, Coordinate currentPosition, String opponentsPreviousMove) {
        String moves = "";

        moves += movesAbove(board, currentPosition) + movesBelow(board, currentPosition);

        System.out.println("Piece " + this + " at " + currentPosition + " has the following available moves.\n" + moves);
        return moves;
    }

    @Override
    public final String toString() {
        return color + "Knight";
    }

    private boolean isEmptyOrOpponentPiece(ChessPiece piece, String sideColor){
        // If the coordinate holds an empty piece or a piece of the opposite color, return true
        if (piece instanceof Empty || !sideColor.equals(piece.color)) {
            return true;
        }

        // Otherwise, return false
        return false;
    }

    // Helper method that calculates all knight 'L' shaped moves above the current position
    private String movesAbove(ChessGameTile[][] board, Coordinate currentPosition){
        String moves = "";
        String sideColor = board[currentPosition.x][currentPosition.y].getPiece().color;

        moves += moveUp1Left2(board, currentPosition, sideColor);
        moves += moveUp1Right2(board, currentPosition, sideColor);
        moves += moveUp2Left1(board, currentPosition, sideColor);
        moves += moveUp2Right1(board, currentPosition, sideColor);

        return moves;
    }

    // Helper method to return the Coordinate up 1 and to the left 2 of the currentPosition, if the coordinate is on the board
    private String moveUp1Left2(ChessGameTile[][] board, Coordinate currentPosition, String sideColor){
        String move = "";
        ChessPiece piece;
        Coordinate up1Left2 = Coordinate.fromString(currentPosition.toString());

        // Checks square up one square and to the left two squares
        up1Left2 = shiftCoordinate(up1Left2, -2, 1);

        // If coordinate is on the board
        if(up1Left2 != null) {
            piece = board[up1Left2.x][up1Left2.y].getPiece();

            // If the coordinate holds an empty piece or a piece of the opposite color
            if (isEmptyOrOpponentPiece(piece, sideColor)) {
                // Add the coordinate as an available move
                move = up1Left2 + " ";
            }
        }

        return move;
    }

    // Helper method to return the Coordinate up 1 and to the right 2 of the currentPosition, if the coordinate is on the board
    private String moveUp1Right2(ChessGameTile[][] board, Coordinate currentPosition, String sideColor){
        String move = "";
        ChessPiece piece;
        Coordinate up1Right2 = Coordinate.fromString(currentPosition.toString());

        // Checks square up one square and to the right two squares
        up1Right2 = shiftCoordinate(up1Right2, 2, 1);

        // If coordinate is on the board
        if(up1Right2 != null) {
            piece = board[up1Right2.x][up1Right2.y].getPiece();

            // If the coordinate holds an empty piece or a piece of the opposite color
            if (isEmptyOrOpponentPiece(piece, sideColor)) {
                // Add the coordinate as an available move
                move = up1Right2 + " ";
            }
        }

        return move;
    }

    // Helper method to return the Coordinate up 2 and to the left 1 of the currentPosition, if the coordinate is on the board
    private String moveUp2Left1(ChessGameTile[][] board, Coordinate currentPosition, String sideColor){
        String move = "";
        ChessPiece piece;
        Coordinate up2Left1 = Coordinate.fromString(currentPosition.toString());

        // Checks square up two squares and to the right one square
        up2Left1 = shiftCoordinate(up2Left1, -1, 2);

        // If coordinate is on the board
        if(up2Left1 != null) {
            piece = board[up2Left1.x][up2Left1.y].getPiece();

            // If the coordinate holds an empty piece or a piece of the opposite color
            if (isEmptyOrOpponentPiece(piece, sideColor)) {
                // Add the coordinate as an available move
                move = up2Left1 + " ";
            }
        }

        return move;
    }

    // Helper method to return the Coordinate up 2 and to the right 1 of the currentPosition, if the coordinate is on the board
    private String moveUp2Right1(ChessGameTile[][] board, Coordinate currentPosition, String sideColor){
        String move = "";
        ChessPiece piece;
        Coordinate up2Right1 = Coordinate.fromString(currentPosition.toString());

        // Checks square up two squares and to the right one square
        up2Right1 = shiftCoordinate(up2Right1, 1, 2);

        // If coordinate is on the board
        if(up2Right1 != null) {
            piece = board[up2Right1.x][up2Right1.y].getPiece();

            // If the coordinate holds an empty piece or a piece of the opposite color
            if (isEmptyOrOpponentPiece(piece, sideColor)) {
                // Add the coordinate as an available move
                move = up2Right1 + " ";
            }
        }

        return move;
    }

    // Helper method that calculates all knight 'L' shaped moves below the current position
    private String movesBelow(ChessGameTile[][] board, Coordinate currentPosition){
        String moves = "";
        String sideColor = board[currentPosition.x][currentPosition.y].getPiece().color;

        moves += moveDown1Left2(board, currentPosition, sideColor);
        moves += moveDown1Right2(board, currentPosition, sideColor);
        moves += moveDown2Left1(board, currentPosition, sideColor);
        moves += moveDown2Right1(board, currentPosition, sideColor);

        return moves;
    }

    // Helper method to return the Coordinate down 1 and to the left 2 of the currentPosition, if the coordinate is on the board
    private String moveDown1Left2(ChessGameTile[][] board, Coordinate currentPosition, String sideColor){
        String move = "";
        ChessPiece piece;
        Coordinate down1Left2 = Coordinate.fromString(currentPosition.toString());

        // Checks square down one square and to the left two squares
        down1Left2 = shiftCoordinate(down1Left2, -2, -1);

        // If coordinate is on the board
        if(down1Left2 != null) {
            piece = board[down1Left2.x][down1Left2.y].getPiece();

            // If the coordinate holds an empty piece or a piece of the opposite color
            if (isEmptyOrOpponentPiece(piece, sideColor)) {
                // Add the coordinate as an available move
                move = down1Left2 + " ";
            }
        }

        return move;
    }

    // Helper method to return the Coordinate down 1 and to the right 2 of the currentPosition, if the coordinate is on the board
    private String moveDown1Right2(ChessGameTile[][] board, Coordinate currentPosition, String sideColor){
        String move = "";
        ChessPiece piece;
        Coordinate down1Right2 = Coordinate.fromString(currentPosition.toString());

        // Checks square down one square and to the right two squares
        down1Right2 = shiftCoordinate(down1Right2, 2, -1);

        // If coordinate is on the board
        if(down1Right2 != null) {
            piece = board[down1Right2.x][down1Right2.y].getPiece();

            // If the coordinate holds an empty piece or a piece of the opposite color
            if (isEmptyOrOpponentPiece(piece, sideColor)) {
                // Add the coordinate as an available move
                move = down1Right2 + " ";
            }
        }

        return move;
    }

    // Helper method to return the Coordinate down 2 and to the left 1 of the currentPosition, if the coordinate is on the board
    private String moveDown2Left1(ChessGameTile[][] board, Coordinate currentPosition, String sideColor){
        String move = "";
        ChessPiece piece;
        Coordinate down2Left1 = Coordinate.fromString(currentPosition.toString());

        // Checks square down two squares and to the left one square
        down2Left1 = shiftCoordinate(down2Left1, -1, -2);

        // If coordinate is on the board
        if(down2Left1 != null) {
            piece = board[down2Left1.x][down2Left1.y].getPiece();

            // If the coordinate holds an empty piece or a piece of the opposite color
            if (isEmptyOrOpponentPiece(piece, sideColor)) {
                // Add the coordinate as an available move
                move = down2Left1 + " ";
            }
        }

        return move;
    }

    // Helper method to return the Coordinate down 2 and to the right 1 of the currentPosition, if the coordinate is on the board
    private String moveDown2Right1(ChessGameTile[][] board, Coordinate currentPosition, String sideColor){
        String move = "";
        ChessPiece piece;
        Coordinate down2Right1 = Coordinate.fromString(currentPosition.toString());

        // Checks square down two squares and to the right one square
        down2Right1 = shiftCoordinate(down2Right1, 1, -2);

        // If coordinate is on the board
        if(down2Right1 != null) {
            piece = board[down2Right1.x][down2Right1.y].getPiece();

            // If the coordinate holds an empty piece or a piece of the opposite color
            if (isEmptyOrOpponentPiece(piece, sideColor)) {
                // Add the coordinate as an available move
                move = down2Right1 + " ";
            }
        }

        return move;
    }
}
