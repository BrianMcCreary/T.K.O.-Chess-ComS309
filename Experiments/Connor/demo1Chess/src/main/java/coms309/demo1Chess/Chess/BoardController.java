package coms309.demo1Chess.Chess;
import org.springframework.web.bind.annotation.*;
@RestController
public class BoardController {
    String playerW = "";
    String playerB = "";
    Board b;

    @PostMapping("/chess")
    public @ResponseBody String createChessBoard() {
        b = new Board();
        return "New board created:\n" + b.toString();
    }

    @GetMapping("/chess")
    public @ResponseBody String getChessBoardAndPlayers() {
        return playerW + " vs. " + playerB + "\n" + b.toString();
    }

    @PutMapping("/chess/{x}-{y}")
    public @ResponseBody String replaceTile(@RequestBody Tile tile, @PathVariable int x, @PathVariable int y) {
        b.replaceTile(tile, x, y);
        return "Tile replaced:\n" + b.toString();
    }

    @PutMapping("/chess/{origX}-{origY}/{destX}-{destY}")
    public @ResponseBody String movePiece(@PathVariable int origX, @PathVariable int origY, @PathVariable int destX, @PathVariable int destY) {
        b.movePiece(b.getTile(origX, origY), b.getTile(destX, destY));
        return b.toString();
    }

    @PostMapping("/chess/playerW")
    public @ResponseBody String setPlayerW(@RequestParam(value = "name", defaultValue = "") String playerW) {
        this.playerW = playerW;
        return playerW + " is now playing with the white pieces.";
    }

    @PostMapping("/chess/playerB")
    public @ResponseBody String setPlayerB(@RequestParam(value = "name", defaultValue = "") String playerB) {
        this.playerB = playerB;
        return playerB + " is now playing with the black pieces.";
    }

    @DeleteMapping("/chess")
    public @ResponseBody String deleteGame() {
        b = new Board();
        playerW = "";
        playerB = "";
        return "Previous game and players deleted";
    }
}
