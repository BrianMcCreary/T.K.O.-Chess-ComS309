package TotalKnockoutChess.Boxing;

import TotalKnockoutChess.Users.User;
import TotalKnockoutChess.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
        User p1 = null;
        User p2 = null;
        int tester = 0;

        //Loop through all users and find the two players
        for (User user : userRepository.findAll()) {
            if (user.getUsername().equals(player1)) {
                p1 = user;
                tester++;
            }
            else if (user.getUsername().equals(player2)) {
                p2 = user;
                tester++;
            }
        }
        if (tester != 2) {
            return failure;     //Users not found
        }
        BoxingGame game = new BoxingGame(p1, p2);
        boxingGameRepository.save(game);
        return success;
    }

}
