package coms309.demo1gamestate.game_experiments;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

public class ChessGamestateController {
    ChessGamestate game = new ChessGamestate();

    @PostMapping("/ChessGamestate")
    public @ResponseBody String createChessGamestate(@RequestBody ChessGamestate gamestate) {
        System.out.println(gamestate.toString());
        game.setGamestate(gamestate.getGamestate());
        game.setPlayerW(gamestate.getPlayerW());
        game.setPlayerB(gamestate.getPlayerB());
        return "Following Gamestate has been saved:\n" + gamestate.toString();
    }

    @GetMapping("/ChessGamestate")
    public @ResponseBody ChessGamestate getChessGamestate() {
        return game;
    }
}
