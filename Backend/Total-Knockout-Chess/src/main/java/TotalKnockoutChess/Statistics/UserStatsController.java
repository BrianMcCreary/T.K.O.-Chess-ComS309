package TotalKnockoutChess.Statistics;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@Api(value = "UserStatsController", description = "Controller used to manage UserStats entities")
@RestController
public class UserStatsController {

    @Autowired
    UserStatsRepository userStatsRepository;

    private final String success = "{\"message\":\"success\"}";     //Messages to return to frontend
    private final String failure = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Returns a UserStats object by their username")
    @GetMapping(path = "/userStats/{username}")
    public UserStats getUserStats(@PathVariable String username) {
        for (UserStats userStats : userStatsRepository.findAll()) {
            if (userStats.getUser().getUsername().equals(username)) {
                return userStats;
            }
        }
        return null;    //UserStats not found
    }

    @ApiOperation(value = "Returns a string detailing a User's stats given their username")
    @GetMapping(path = "/getUserStats/{username}")
    public String getUserStatsString(@PathVariable String username) {
        String message;
        for (UserStats userStats : userStatsRepository.findAll()) {
            if (userStats.getUser().getUsername().equals(username)) {
                message = String.format("ChessBoxing %d %.1f Chess %d %.1f Boxing %d %.1f", userStats.getChessBoxingGames(), userStats.getChessBoxingWinRate(),
                        userStats.getChessGames(), userStats.getChessWinRate(), userStats.getBoxingGames(), userStats.getBoxingWinRate());
                return message;
            }
        }
        return "";
    }

    @ApiOperation(value = "Adds a Chess win to stats given their username")
    @PutMapping(path = "/userStats/chessWin/{username}")
    public String chessW(@PathVariable String username) {
        UserStats userStats = null;
        boolean userStatsFound = false;
        for (UserStats us : userStatsRepository.findAll()) {
            if (us.getUser().getUsername().equals(username)) {
                userStats = us;
                userStatsFound = true;
                break;
            }
        }
        if (!userStatsFound) {
            return failure;     //Couldn't find user
        }
        userStats.chessWin();
        userStatsRepository.flush();
        return success;
    }

    @ApiOperation(value = "Adds a Chess loss to stats given their username")
    @PutMapping(path = "/userStats/chessLoss/{username}")
    public String chessL(@PathVariable String username) {
        UserStats userStats = null;
        boolean userStatsFound = false;
        for (UserStats us : userStatsRepository.findAll()) {
            if (us.getUser().getUsername().equals(username)) {
                userStats = us;
                userStatsFound = true;
                break;
            }
        }
        if (!userStatsFound) {
            return failure;     //Couldn't find user
        }
        userStats.chessLoss();
        userStatsRepository.flush();
        return success;
    }

    @ApiOperation(value = "Adds a Boxing win to stats given their username")
    @PutMapping(path = "/userStats/boxingWin/{username}")
    public String boxingW(@PathVariable String username) {
        UserStats userStats = null;
        boolean userStatsFound = false;
        for (UserStats us : userStatsRepository.findAll()) {
            if (us.getUser().getUsername().equals(username)) {
                userStats = us;
                userStatsFound = true;
                break;
            }
        }
        if (!userStatsFound) {
            return failure;     //Couldn't find user
        }
        userStats.boxingWin();
        userStatsRepository.flush();
        return success;
    }

    @ApiOperation(value = "Adds a Boxing loss to stats given their username")
    @PutMapping(path = "/userStats/boxingLoss/{username}")
    public String boxingL(@PathVariable String username) {
        UserStats userStats = null;
        boolean userStatsFound = false;
        for (UserStats us : userStatsRepository.findAll()) {
            if (us.getUser().getUsername().equals(username)) {
                userStats = us;
                userStatsFound = true;
                break;
            }
        }
        if (!userStatsFound) {
            return failure;     //Couldn't find user
        }
        userStats.boxingLoss();
        userStatsRepository.flush();
        return success;
    }

    @ApiOperation(value = "Adds a Chess Boxing win to stats given their username")
    @PutMapping(path = "/userStats/chessBoxingWin/{username}")
    public String chessBoxingW(@PathVariable String username) {
        UserStats userStats = null;
        boolean userStatsFound = false;
        for (UserStats us : userStatsRepository.findAll()) {
            if (us.getUser().getUsername().equals(username)) {
                userStats = us;
                userStatsFound = true;
                break;
            }
        }
        if (!userStatsFound) {
            return failure;     //Couldn't find user
        }
        userStats.chessBoxingWin();
        userStatsRepository.flush();
        return success;
    }

    @ApiOperation(value = "Adds a Chess Boxing loss to stats given their username")
    @PutMapping(path = "/userStats/chessBoxingLoss/{username}")
    public String chessBoxingL(@PathVariable String username) {
        UserStats userStats = null;
        boolean userStatsFound = false;
        for (UserStats us : userStatsRepository.findAll()) {
            if (us.getUser().getUsername().equals(username)) {
                userStats = us;
                userStatsFound = true;
                break;
            }
        }
        if (!userStatsFound) {
            return failure;     //Couldn't find user
        }
        userStats.chessBoxingLoss();
        userStatsRepository.flush();
        return success;
    }
}
