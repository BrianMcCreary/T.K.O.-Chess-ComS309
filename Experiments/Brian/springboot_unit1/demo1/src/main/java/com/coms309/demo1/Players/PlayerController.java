package com.coms309.demo1.Players;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController("/players")
public class PlayerController {
    ArrayList<Player> players = new ArrayList<Player>();

    @PostMapping("/new")
    public String createPlayer(@RequestParam("username") String username){
        Player p = new Player(username);
        players.add(p);
        return "New player: " + p.getUsername() + "created.";
    }
}
