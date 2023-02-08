package coms309.demo1gamestate.game_experiments;

import org.springframework.web.bind.annotation.*;

@RestController
public class ChessGamestateController {
    ChessGamestate game = new ChessGamestate();

    @PostMapping("/ChessGamestate")
    public @ResponseBody String createChessGamestate(@RequestBody ChessGamestate gamestate) {
        System.out.println(gamestate.toString());
        game = new ChessGamestate(gamestate.getGamestate(), gamestate.getPlayerB(), gamestate.getPlayerW());
        return "Following Gamestate has been saved:\n" + game.toString();
    }

    @GetMapping("/ChessGamestate")
    public @ResponseBody ChessGamestate getChessGamestate() {
        return game;
    }

    @PutMapping("/ChessGamestate/gamestate/{gamestate}")
    public @ResponseBody void updateGameState(@PathVariable String gamestate) {
        game.setGamestate(gamestate);
    }

    @DeleteMapping("/ChessGamestate")
    public @ResponseBody String deleteChessGamestate() {
        game = new ChessGamestate();
        return "Game deleted.";
    }
}
