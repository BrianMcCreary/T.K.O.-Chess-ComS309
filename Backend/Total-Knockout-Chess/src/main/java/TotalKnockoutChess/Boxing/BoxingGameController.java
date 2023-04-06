package TotalKnockoutChess.Boxing;

import TotalKnockoutChess.Users.User;
import TotalKnockoutChess.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final String trueMessage = "{\"message\":\"true\"}";
    private final String falseMessage = "{\"message\":\"false\"}";

    @PostMapping(path = "/boxingGame/{player1}/{player2}")
    public String createBoxingGame(String player1, String player2) {
        User p1;
        User p2;
        int tester = 0;
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
        return null;
    }

}
