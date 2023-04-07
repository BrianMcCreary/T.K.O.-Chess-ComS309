package TotalKnockoutChess.Lobby;

import TotalKnockoutChess.Users.User;
import TotalKnockoutChess.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LobbyController {

    @Autowired
    LobbyRepository lobbyRepository;

    @Autowired
    UserRepository userRepository;

    // Messages to send to the frontend
    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";

    // Mapping to create a new lobby with the given User object as its owner.
    @PostMapping("/hostLobby")
    public String hostLobby(@RequestBody User owner){
        // If user doesn't exist, return as a failure
        if(owner == null){
            return failure;
        }

        // If user is already in a lobby, return as a failure.
        for(Lobby l : lobbyRepository.findAll()){
            if(l.contains(owner)){
                return failure;
            }
        }

        // Create new lobby, generate and set code.
        Lobby lobby = new Lobby(owner);
        Long lobbyCode = lobby.generateLobbyCode(lobbyRepository.findAll());
        lobby.setCode(lobbyCode);

        lobbyRepository.save(lobby);
        return "{\"message\":\"" + lobbyCode + "\"}";
    }

    // Mapping for people other than the owner to join the lobby.
    @PutMapping("/{lobbyCode}")
    public String joinLobby(@PathVariable Long lobbyCode, @RequestBody User user){
        Lobby lobby = lobbyRepository.getByCode(lobbyCode);
        User usr = userRepository.findById(user.getId());

        // If the lobby doesn't exist, method will fail
        if(lobby == null){
            return failure;
        }

        // If the user already is in the lobby, method will fail
        if(lobby.contains(usr)){
            return failure;
        }

        // When you first join a lobby you start as a spectator.
        lobby.addToSpectators(usr);
        lobby.incrementUserCount();
        lobbyRepository.flush();
        return success;
    }

    // Mapping for users to swap from playing in the lobby to spectating.
    @PutMapping("/{lobbyCode}/spectate")
    public void spectate(@PathVariable Long lobbyCode, @RequestBody User user){
        User usr = userRepository.findById(user.getId());
        Lobby lobby = lobbyRepository.getByCode(lobbyCode);
        lobby.switchToSpectator(usr);
        lobbyRepository.flush();
    }

    // Mapping for users to swap from spectating in the lobby to playing. This will fail if both player slots are full.
    @PutMapping("/{lobbyCode}/play")
    public String play(@PathVariable Long lobbyCode, @RequestBody User user){
        lobbyRepository.flush();
        Lobby lobby = lobbyRepository.getByCode(lobbyCode);
        User usr = userRepository.findById(user.getId()); // Make sure usr has all updated fields from repository
        boolean isSpectating = false;

        // If user is not spectating, method will fail.
        for(User spectator : lobby.getSpectators()) {
            if (spectator.equals(usr)) {
                isSpectating = true;
                break;
            }
        }
        if(!isSpectating) {
            return failure;
        }
        // If both player slots are full, method will fail.
        else if(lobby.getPlayer1() != null && lobby.getPlayer2() != null){
            return failure;
        }
        // If player slot 1 is open, user will fill that slot.
        else if(lobby.getPlayer1() == null){
            lobby.switchToPlayer(usr, 1);
            lobbyRepository.flush();
            return success;

        }
        // If player slot 2 is open and user isn't already in player slot 1, user will fill slot 2.
        else if(lobby.getPlayer2() == null && !lobby.getPlayer1().equals(usr)){
            lobby.switchToPlayer(usr, 2);
            lobbyRepository.flush();
            return success;

        }
        lobbyRepository.flush();
        return failure;
    }

    // Mapping to leave a lobby.
    @PutMapping("/{lobbyCode}/leave")
    public void leaveLobby(@PathVariable Long lobbyCode, @RequestBody User user){
        User usr = userRepository.findById(user.getId());
        Lobby lobby = lobbyRepository.getByCode(lobbyCode);

        // Phantom lobby
        if(lobby.getUserCount() < 1){
            deleteLobby(lobby.getId());
            return;
        }

        // Last user in the lobby will delete the lobby
        if(lobby.getUserCount() == 1 && lobby.contains(usr)){
            deleteLobby(lobby.getId());
            return;
        }

        if(lobby.getPlayer1() != null && usr.equals(lobby.getPlayer1())){
            lobby.setPlayer1(lobby.getPlayer2()); // Shift player 2 slot to player 1
            lobby.setPlayer2(null);               // Fill player 2 slot with null object.
            lobby.decrementUserCount();
        }
        else if(lobby.getPlayer2() != null && usr.equals(lobby.getPlayer2())){
            lobby.setPlayer2(null);               // Fill player 2 slot with null object.
            lobby.decrementUserCount();
        }
        else{
            for(User spectator: lobby.getSpectators()){
                if(spectator.equals(usr)){
                    lobby.removeSpectator(spectator);
                    lobby.decrementUserCount();
                    break;
                }
            }
        }
        lobbyRepository.flush();
    }

    //Mapping to delete a lobby from the repository.
    @DeleteMapping("/lobby/{lobbyId}")
    public void deleteLobby(@PathVariable Long lobbyId){
        Lobby l = lobbyRepository.getById(lobbyId);
        l.setOwner(null);
        lobbyRepository.delete(l);
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
