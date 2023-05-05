package TotalKnockoutChess.Chess;

import TotalKnockoutChess.Lobby.LobbyRepository;
import TotalKnockoutChess.Users.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(value = "ChessGameController", description = "Controller used to manage ChessGame entities")
@RestController
@RequestMapping("/chess")
public class ChessGameController {

    @Autowired
    LobbyRepository lobbyRepository;

    @Autowired
    ChessGameRepository chessGameRepository;

    @Autowired
    UserRepository userRepository;

    //Messages to return to frontend
    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Creates a new ChessGame instance and saves it to the repository")
    @PostMapping("/{whitePlayer}/{blackPlayer}")
    public String createChessGame(@PathVariable String whitePlayer, @PathVariable String blackPlayer){

        ChessGame game = new ChessGame(whitePlayer, blackPlayer, null);
        chessGameRepository.save(game);
        chessGameRepository.flush();

//        game.displayBoard();

        // Check if game was successfully saved to database
        for(ChessGame g : chessGameRepository.findAll()) {
            // Find exact game match by id
            if (g.getId() == game.getId()) {
                return success;
            }
        }

        return failure;
    }

    @ApiOperation(value = "Deletes a ChessGame entity by the given id")
    @Transactional
    @DeleteMapping("/delete/{id}")
    public String deleteById(@PathVariable int id){

        // Check for game with matching id
        for(ChessGame g : chessGameRepository.findAll()){
            if(g.getId() == id){
                chessGameRepository.deleteById(id);
                chessGameRepository.flush();

                return "{\"message\":\"success\"}";
            }
        }

        return "{\"message\":\"failure\"}";
    }

    @ApiOperation(value = "Returns a list of all chess games")
    @GetMapping(path = "/getAllGames")
    public List<ChessGame> getAllGames() {
        return chessGameRepository.findAll();
    }
}
