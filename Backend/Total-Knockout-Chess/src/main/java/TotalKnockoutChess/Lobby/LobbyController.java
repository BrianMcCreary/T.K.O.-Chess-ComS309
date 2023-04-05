package TotalKnockoutChess.Lobby;

import TotalKnockoutChess.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LobbyController {

    @Autowired
    LobbyRepository lobbyRepository;

    // Messages to send to the frontend
    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";

    // Mapping to create a new lobby with the given User object as its owner.
    @PostMapping("/lobby")
    public String hostLobby(@RequestBody User owner){
        // If user doesn't exist or already is in a lobby, return as a failure
//        if(owner == null){
//            return failure;
//        }



        Lobby lobby = new Lobby(owner);
        Long lobbyCode = lobby.generateLobbyCode(lobbyRepository.findAll());
        lobby.setCode(lobbyCode);
        lobbyRepository.save(lobby);
        return success;
    }

    // Mapping for people other than the owner to join the lobby.
    @PutMapping("/{lobbyCode}")
    public String joinLobby(@PathVariable Long lobbyCode, @RequestBody User user){
        Lobby lobby = lobbyRepository.getByCode(lobbyCode);
        if(lobby.contains(user)){
            return failure;
        }

        // When you first join a lobby you start as a spectator.
        lobby.addToSpectators(user);
        lobbyRepository.flush();
        return success;
    }

    // Mapping for users to swap from playing in the lobby to spectating.
    @PutMapping("/{lobbyCode}/spectate")
    public void spectate(@PathVariable Long lobbyCode, @RequestBody User user){
        Lobby lobby = lobbyRepository.getByCode(lobbyCode);
        lobby.switchToSpectator(user);
        lobbyRepository.flush();
    }

    // Mapping for users to swap from spectating in the lobby to playing. This will fail if both player slots are full.
    @PutMapping("/{lobbyCode}/play")
    public String play(@PathVariable Long lobbyCode, @RequestBody User user){
        lobbyRepository.flush();
        Lobby lobby = lobbyRepository.getByCode(lobbyCode);
        // If user is not already in the lobby, method will fail.
        if(!lobby.contains(user)){
            System.out.println("User is not in the lobby");
            return failure;
        }
        // If both player slots are full, method will fail.
        else if(lobby.getPlayer1() != null && lobby.getPlayer2() != null){
            System.out.println("Player slots are full");
            return failure;
        }
        // If player slot 1 is open, user will fill that slot.
        else if(lobby.getPlayer1() == null){
            lobby.switchToPlayer(user, 1);
        }
        // If player slot 2 is open, user will fill that slot.
        else if(lobby.getPlayer2() == null){
            lobby.switchToPlayer(user, 2);
        }
        lobbyRepository.flush();
        return success;
    }

    //Mapping to delete a lobby from the repository.
    @DeleteMapping("/lobby/{lobbyId}")
    public void deleteLobby(@PathVariable Long lobbyId){
        lobbyRepository.deleteById(lobbyId);
    }

    // Mapping to get all current lobbies.
    @GetMapping("/lobbies")
    public List<Lobby> getLobbies(){
        return lobbyRepository.findAll();
    }

    @GetMapping("/{lobbyCode}/spectators")
    public List<User> getLobbySpectators(@PathVariable Long lobbyCode){
        Lobby lobby = lobbyRepository.getByCode(lobbyCode);
        return lobby.getSpectators();
    }
}
