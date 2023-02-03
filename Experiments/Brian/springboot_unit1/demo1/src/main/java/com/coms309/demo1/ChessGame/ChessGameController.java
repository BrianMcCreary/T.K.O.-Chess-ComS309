package com.coms309.demo1.ChessGame;

import com.coms309.demo1.Players.Player;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
public class ChessGameController {

    private int chessGameID = 1;
    ArrayList<ChessGame> chessGames = new ArrayList<ChessGame>();

    @PostMapping("/chess")
    public @ResponseBody String newChessGame(/**@RequestParam (name="black", required = true) String whitePlayer, @RequestParam (name="white", required = true) String blackPlayer*/){
        ChessGame game = new ChessGame(chessGameID/**, whitePlayer, blackPlayer*/);
        chessGames.add(game);
        chessGameID++;

        return game.toString();
    }
    /** TODO
    @PutMapping("/chess/{id}")
    public @ResponseBody String updateChess(@PathVariable String id, @RequestParam (name=""))
    */
}
