package coms309.demo1Chess.Chess;

public class BoardTest {
    public static void main(String[] args) {
        Board b = new Board();
        System.out.println(b.toString());
        b.movePiece(b.getTile(6, 2), b.getTile(6, 3));
        System.out.println(b.toString());
        b.movePiece(b.getTile(5, 7), b.getTile(5, 5));
        System.out.println(b.toString());
        b.movePiece(b.getTile(7, 2), b.getTile(7, 4));
        System.out.println(b.toString());
        b.movePiece(b.getTile(4, 8), b.getTile(8, 4));
        System.out.println(b.toString());
    }
}
