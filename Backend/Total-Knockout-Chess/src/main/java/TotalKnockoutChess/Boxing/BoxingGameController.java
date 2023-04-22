package TotalKnockoutChess.Boxing;

import TotalKnockoutChess.Users.User;
import TotalKnockoutChess.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BoxingGameController {

    @Autowired
    BoxingGameRepository boxingGameRepository;

    @Autowired
    UserRepository userRepository;

    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";

    @PutMapping(path = "/boxingGame/{player1}/{player2}")
    public String deleteBoxingGame(@PathVariable String player1, @PathVariable String player2) {
        for (BoxingGame bg : boxingGameRepository.findAll()) {
            if ((bg.getPlayer1().equals(player1) && bg.getPlayer2().equals(player2)) || (bg.getPlayer1().equals(player2) && bg.getPlayer2().equals(player1))) {
                boxingGameRepository.delete(bg);
                return success;
            }
        }
        return failure;
    }

    @GetMapping(path = "/getBoxingGames")
    public List<BoxingGame> getBoxingGames() {
        return boxingGameRepository.findAll();
    }
}
