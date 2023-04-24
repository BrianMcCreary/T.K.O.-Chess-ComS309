package TotalKnockoutChess.Chess;

import TotalKnockoutChess.Lobby.Lobby;
import TotalKnockoutChess.Lobby.LobbyRepository;
import TotalKnockoutChess.Users.User;
import TotalKnockoutChess.Users.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "ChessGameController", description = "Controller used to manage ChessGame entities")
@RestController
@RequestMapping("/chess")
public class ChessGameController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LobbyRepository lobbyRepository;

    @Autowired
    ChessGameRepository chessGameRepository;

    //Messages to return to frontend
    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Creates a new ChessGame instance and saves it to the repository")
    @PostMapping("/{lobbyCode}")
    public void createChessGame(@PathVariable Long lobbyCode){
        Lobby lobby = lobbyRepository.getByCode(lobbyCode);

        ChessGame game = new ChessGame(lobby);
        chessGameRepository.save(game);
        chessGameRepository.flush();



    }

}
