package TotalKnockoutChess.Statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserStatsController {

    @Autowired
    UserStatsRepository userStatsRepository;

    private final String success = "{\"message\":\"success\"}";     //Messages to return to frontend
    private final String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/userStats/{username}")
    public UserStats getUserStats(@PathVariable String username) {
        for (UserStats userStats : userStatsRepository.findAll()) {
            if (userStats.getUser().getUsername().equals(username)) {
                return userStats;
            }
        }
        return null;    //UserStats not found
    }

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
