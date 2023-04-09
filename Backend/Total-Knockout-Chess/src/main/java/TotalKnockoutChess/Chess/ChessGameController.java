package TotalKnockoutChess.Chess;

import TotalKnockoutChess.Lobby.Lobby;
import TotalKnockoutChess.Lobby.LobbyRepository;
import TotalKnockoutChess.Users.User;
import TotalKnockoutChess.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{lobbyCode}")
    public void createChessGame(@PathVariable Long lobbyCode){
        Lobby lobby = lobbyRepository.getByCode(lobbyCode);

        ChessGame game = new ChessGame(lobby);
        chessGameRepository.save(game);
        chessGameRepository.flush();



    }

}
