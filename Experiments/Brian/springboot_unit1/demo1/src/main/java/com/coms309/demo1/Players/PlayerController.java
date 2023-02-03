package com.coms309.demo1.Players;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

@RestController
public class PlayerController {
    ArrayList<Player> players = new ArrayList<Player>();
    protected int id = 1;

    @PostMapping("/players/{username}")
    public @ResponseBody String createPlayerByPath(@PathVariable String username){
        Player p = new Player(username, id);

        for(Player player : players){
            if(player.username.equals(username)){
                return "Request failed: user \"" + username + "\" already exists";
            }
        }

        players.add(p);
        id++;
        return "New player: \"" + p.getUsername() + "\" created.";
    }

    @PostMapping("/players/parameter")
    public @ResponseBody String createPlayerByParam(@RequestParam (value="username", required=true) String username) {
        Player p = new Player(username, id);

        for (Player player : players) {
            if (player.username.equals(username)) {
                return "Request failed: user \"" + username + "\" already exists";
            }
        }
        players.add(p);
        id++;
        return "New player: \"" + p.getUsername() + "\" created.";
    }

    @PostMapping("/players")
    public @ResponseBody String createPlayerByBody(@RequestBody Player new_player){
        Player p = new Player(new_player.username, id);

        for(Player player : players){
            if(player.username.equals(new_player.username)){
                return "Request failed: user \"" + new_player.username + "\" already exists";
            }
        }
        players.add(p);
        id++;
        return "New player: \"" + p.getUsername() + "\" created.";
    }

    @GetMapping("/players")
    public @ResponseBody ArrayList<Player> getAllPlayers(){
        return players;
    }

    @GetMapping("/players/{username}/stats")
    public @ResponseBody String getStats(@PathVariable String username){
        for(Player player : players){
            if(player.username.equals(username)){
                return player.getStats();
            }
        }

        return "Player \"" + username + "\" does not exist.";
    }

    @DeleteMapping("/players")
    public @ResponseBody String deletePlayer(@RequestParam (value="username", required=true) String username){
        for(Player player : players){
            if(player.username.equals(username)){
                players.remove(player);
                return "Player \"" + username + "\" deleted.";
            }
        }

        return "Player \"" + username + "\" does not exist.";
    }

}
