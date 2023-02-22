package com.coms309.demo1.system;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class WelcomeController {

    @GetMapping("/")
    public String welcome(){

        return "Welcome to the Chess Boxing project!\n" +
                "\nThe following URLs are available: \n" +
                "Homepage: localhost:8080/\n" +
                "The following URLS are prefixed by the homepage URL: \n" +
                "\n/players section: \n" +
                "GET list of all current players: /players\n" +
                "POST a new player: /players/{username}\n" +
                "POST a new player by parameter: /players/parameter\n" +
                "POST a new player by JSON body: /players\n" +
                "DELETE a player by parameter: /players\n" +
                "PUT new information about a player via parameter: /players/{username}\n" +
                "GET statistics of a player: /players/{username}/stats\n" +
                "\n/chess section: \n" +
                "POST a new game: /chess\n";
    }
}
