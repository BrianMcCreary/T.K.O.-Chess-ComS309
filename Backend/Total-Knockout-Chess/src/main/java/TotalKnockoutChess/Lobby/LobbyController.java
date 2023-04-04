package TotalKnockoutChess.Lobby;

import TotalKnockoutChess.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LobbyController {

    @Autowired
    LobbyRepository lobbyRepository;

    // Messages to send to the frontend
    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";

    @PostMapping("/host")
    public String hostLobby(@RequestBody User owner){
        // If user doesn't exist or already is in a lobby, return as a failure
//        if(owner == null){
//            return failure;
//        }

        Lobby lobby = new Lobby(owner);
        lobbyRepository.save(lobby);
        return success;
    }

    @GetMapping("/lobbies")
    public List<Lobby> getLobbies(){
        return lobbyRepository.findAll();
    }
}
