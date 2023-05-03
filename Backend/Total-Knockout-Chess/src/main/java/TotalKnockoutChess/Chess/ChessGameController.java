package TotalKnockoutChess.Chess;

import TotalKnockoutChess.Chess.Pieces.Coordinate;
import TotalKnockoutChess.Lobby.Lobby;
import TotalKnockoutChess.Lobby.LobbyRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@Api(value = "ChessGameController", description = "Controller used to manage ChessGame entities")
@RestController
@RequestMapping("/chess")
public class ChessGameController {

    @Autowired
    LobbyRepository lobbyRepository;

    @Autowired
    ChessGameRepository chessGameRepository;

    //Messages to return to frontend
    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Creates a new ChessGame instance and saves it to the repository")
    @PostMapping("/{whitePlayer}/{blackPlayer}")
    public void createChessGame(@PathVariable String whitePlayer, @PathVariable String blackPlayer){

        ChessGame game = new ChessGame(whitePlayer, blackPlayer, null);
        chessGameRepository.save(game);
        chessGameRepository.flush();

        game.displayBoard();
    }

    @ApiOperation(value = "Updates the board if the move is successful")
    @PutMapping("/{id}/move/{from}/{to}")
    public String move(@PathVariable int id, @PathVariable String from, @PathVariable String to){
        ChessGame game = chessGameRepository.findById(id);
        Coordinate fromCoordinate = Coordinate.fromString(from);
        Coordinate toCoordinate = Coordinate.fromString(to);

        boolean success = game.makeMove(fromCoordinate, toCoordinate);

        // Ensure the board is updated in the database
        chessGameRepository.flush();

        // Backend text output for debugging
        game.displayBoard();

        return success + ". Game with id " + game.getId() + " updated";
    }

    @ApiOperation(value = "Deletes a ChessGame entity by the given id")
    @Transactional
    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable int id){

        chessGameRepository.deleteById(id);
        chessGameRepository.flush();
    }
}
