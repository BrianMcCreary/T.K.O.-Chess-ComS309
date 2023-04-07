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

    @PutMapping("chess") // modify to update a specific game board based off an id
    public void makeMove(@RequestBody ChessPiece selectedPiece){

    }


}
