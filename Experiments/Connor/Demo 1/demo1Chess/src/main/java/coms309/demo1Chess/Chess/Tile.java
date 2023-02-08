package coms309.demo1Chess.Chess;

public class Tile {
    private boolean hasPiece;
    private String pieceType;

    public Tile() {
        this.hasPiece = false;
        pieceType = "Empty";
    }

    public Tile(String pieceType) {
        this.hasPiece = true;
        this.pieceType = pieceType;
    }

    public boolean hasPiece () {
        return hasPiece;
    }

    public String getPieceType() {
        return pieceType;
    }

    public void setHasPiece(boolean a) {
        hasPiece = a;
    }

    public void setPieceType(String pieceType) {
        this.pieceType = pieceType;
    }
}
