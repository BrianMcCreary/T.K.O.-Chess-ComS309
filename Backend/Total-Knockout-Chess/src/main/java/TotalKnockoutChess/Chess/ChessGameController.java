package TotalKnockoutChess.Chess;

import TotalKnockoutChess.Users.User;
import TotalKnockoutChess.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessGameController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChessGameRepository chessGameRepository;

    //Messages to return to frontend
    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";

    @PostMapping("/{lobbyId}/chess")
    public void createChessGame(@PathVariable String lobbyId, @RequestBody User white, @RequestBody User black){
        ChessGame game = new ChessGame(white, black);
        chessGameRepository.save(game);
    }

}
