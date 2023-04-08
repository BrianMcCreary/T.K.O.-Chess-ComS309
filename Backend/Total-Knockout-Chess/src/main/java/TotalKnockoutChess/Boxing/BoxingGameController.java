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

    //Method for creating the boxing game
    @PostMapping(path = "/boxingGame/{player1}/{player2}")
    public String createBoxingGame(@PathVariable String player1, @PathVariable String player2) {
        int tester = 0;

        //Loop through all users and find the two players
        for (User user : userRepository.findAll()) {
            if (user.getUsername().equals(player1)) {
                tester++;
            }
            else if (user.getUsername().equals(player2)) {
                tester++;
            }
        }
        if (tester != 2) {
            return failure;     //Users not found
        }
        BoxingGame game = new BoxingGame(player1, player2);
        boxingGameRepository.save(game);
        return success;
    }

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
<<<<<<< HEAD
=======

>>>>>>> main
}
