package coms309.demo1Chess.Chess;

public class Board {
    private Tile[][] chessBoard = new Tile[8][8];

    public Board() {
        chessBoard[0][0] = new Tile("WRook");
        chessBoard[1][0] = new Tile("WKnight");
        chessBoard[2][0] = new Tile("WBishop");
        chessBoard[3][0] = new Tile("WQueen");       //first white row
        chessBoard[4][0] = new Tile("WKing");
        chessBoard[5][0] = new Tile("WBishop");
        chessBoard[6][0] = new Tile("WKnight");
        chessBoard[7][0] = new Tile("WRook");

        for (int i = 0; i < 8; ++i) {
            chessBoard[i][1] = new Tile("WPawn");    //white pawns
        }

        chessBoard[0][7] = new Tile("BRook");
        chessBoard[1][7] = new Tile("BKnight");
        chessBoard[2][7] = new Tile("BBishop");
        chessBoard[3][7] = new Tile("BQueen");
        chessBoard[4][7] = new Tile("BKing");        //first black row
        chessBoard[5][7] = new Tile("BBishop");
        chessBoard[6][7] = new Tile("BKnight");
        chessBoard[7][7] = new Tile("BRook");

        for (int i = 0; i < 8; ++i) {
            chessBoard[i][6] = new Tile("BPawn");    //black pawns
        }

        for (int i = 0; i < 8; ++i) {
            for (int j = 2; j < 6; ++j) {           //rest of empty board
                chessBoard[i][j] = new Tile();
            }
        }
    }
    public void movePiece (Tile origin, Tile destination) {
        if (destination.getPieceType().equals("BKing") || destination.getPieceType().equals("WKing")) {
            return;
        }
        destination.setPieceType(origin.getPieceType());
        destination.setHasPiece(true);
        origin.setPieceType("Empty");
        origin.setHasPiece(false);
    }

    public Tile getTile(int x, int y) {
        return chessBoard[x - 1][y - 1];
    }

    @Override
    public String toString() {
        String board = "";
        for (int i = 7; i > -1; --i) {
            for (int j = 0; j < 8; ++j) {
                board += "[" + this.chessBoard[j][i].getPieceType() + "] ";
            }
            board += "\n";
        }
        return board;
    }
}
