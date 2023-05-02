package TotalKnockoutChess.Boxing;

import TotalKnockoutChess.Users.User;
import TotalKnockoutChess.Users.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value = "BoxingGameController", description = "Controller used to manage BoxingGame entities")
@RestController
public class BoxingGameController {

    @Autowired
    BoxingGameRepository boxingGameRepository;

    @Autowired
    UserRepository userRepository;

    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Deletes a boxing game from the repository given the two players' usernames")
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

    @ApiOperation(value = "Creates a boxing game and saves it to the repository given the two players' usernames")
    @PostMapping(path = "/boxingGame/{player1}/{player2}")
    public String createBoxingGame(@PathVariable String player1, @PathVariable String player2) {
        BoxingGame bg = new BoxingGame(player1, player2, new ArrayList<String>());
        if (bg != null) {
            boxingGameRepository.save(bg);
            return success;
        }
        return failure;
    }

    @ApiOperation(value = "Returns a list of all boxing games")
    @GetMapping(path = "/getBoxingGames")
    public List<BoxingGame> getBoxingGames() {
        return boxingGameRepository.findAll();
    }
}
