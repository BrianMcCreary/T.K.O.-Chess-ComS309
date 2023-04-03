package TotalKnockoutChess.Chess.Pieces;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChessPieceController {
    /**
     * timer
     * calculateAvailableMoves
     * makeMove
     */


    @GetMapping("chess")
    public List<Coordinate> calculateAvailableMoves(@RequestBody ChessPiece selectedPiece){
        return selectedPiece.calculateAvailableMoves();
    }

    @PutMapping("chess")
    public void makeMove(@PathVariable int gameId, @RequestBody ChessPiece selectedPiece){
        //TODO - need to create lobby system
    }


}
