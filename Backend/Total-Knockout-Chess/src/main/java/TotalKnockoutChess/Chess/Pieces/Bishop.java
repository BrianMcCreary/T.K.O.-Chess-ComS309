package TotalKnockoutChess.Chess.Pieces;

import TotalKnockoutChess.Chess.ChessGameTile;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends ChessPiece {
    private static final long serialVersionUID = 0L;

    public Bishop(String color) {
        super(color);
    }

    public String calculateAvailableMoves(ChessGameTile[][] board, Coordinate currentPosition, King king) {
        List<String> moves = new ArrayList<>();

        // TODO Replace with bishop movements
        int row = currentPosition.y + 1, col = currentPosition.x + 1;

        // Movements up and to the right
        while(row < board.length && col < board.length){
            ChessPiece piece = board[col][row].getPiece();
            if(piece.color.equals(this.color)){
                break;
            }
            moves.add(Coordinate.fromInteger(new int[]{row, col}).toString());
            row++;
            col++;
        }

        // Movements up and to the left
        while(row < board.length && col < board.length){
            ChessPiece piece = board[col][row].getPiece();
            if(piece.color.equals(this.color)){
                break;
            }
            moves.add(Coordinate.fromInteger(new int[]{row, col}).toString());
            row++;
            col++;
        }
//        for(Coordinate c : Coordinate.values()){
//            moves.add(c.toString());
//        }

        for(String move : moves) {
            Coordinate coord = Coordinate.fromString(move);
            ChessGameTile tile = board[coord.x][coord.y];
            ChessPiece originalPiece = tile.getPiece();

            // Look ahead and see if the move has our king in check
            tile.setPiece(this);
            king.scan();
            if(king.isChecked()){
                moves.remove(move);
            }

            tile.setPiece(originalPiece);
        }

        // Parse List into single string of space separated items
        String stringMoves = "";
        for(String move: moves){
            stringMoves += move + " ";
        }

        return stringMoves;
    }

    @Override
    public final String toString() {
        return color + "Bishop";
    }
}
