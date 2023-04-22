package TotalKnockoutChess.Lobby;

import TotalKnockoutChess.Users.User;
import TotalKnockoutChess.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/lobby")
public class LobbyController {

    @Autowired
    LobbyRepository lobbyRepository;

    @Autowired
    UserRepository userRepository;

    private final String success = "success";
    private final String failure = "failure";

    @GetMapping("/find/{lobbyCode}")
    public String findLobby(@PathVariable Long lobbyCode) {
        for (Lobby l : lobbyRepository.findAll()) {
            if (l.getCode().equals(lobbyCode)) {
                return success;
            }
        }
        return "Lobby not found";
    }

    //Mapping to delete a lobby from the repository.
    @DeleteMapping("/delete/{lobbyId}")
    public void deleteLobby(@PathVariable int lobbyId){
        Lobby l = lobbyRepository.findById(lobbyId);
        l.setOwner(null);
        lobbyRepository.delete(l);
    }

    // Mapping to get all current lobbies.
    @GetMapping("/all")
    public List<Lobby> getLobbies(){
        return lobbyRepository.findAll();
    }

    @GetMapping("/spectators/{lobbyCode}")
    public List<String> getLobbySpectators(@PathVariable Long lobbyCode){
        Lobby lobby = lobbyRepository.getByCode(lobbyCode);
        return lobby.getSpectators();
    }

    //Method used when a player joins a lobby, gives them all the information they need
    @GetMapping("/justJoined/{lobbyCode}")
    public List<String> getUsersInLobby(@PathVariable Long lobbyCode) {
        Lobby lobby = lobbyRepository.getByCode(lobbyCode);
        List<String> users = new ArrayList<String>();
        if (lobby != null) {
            if (lobby.getPlayer1() != null) {
                String readyStatus = "NotReady";
                if (lobby.getPlayer1Ready()) {
                    readyStatus = "Ready";
                }
                users.add(lobby.getPlayer1() + " Player1 " + readyStatus);
            }
            if (lobby.getPlayer2() != null) {
                String readyStatus = "NotReady";
                if (lobby.getPlayer2Ready()) {
                    readyStatus = "Ready";
                }
                users.add(lobby.getPlayer2() + " Player2 " + readyStatus);
            }
            if (!lobby.getSpectators().isEmpty()) {
                for (String spectator : lobby.getSpectators()) {
                    users.add(spectator + " Spectator NotReady");
                }
            }
        }
        return users;
    }

    // Helper method to get the user from the repository given their username
    private User getUser(String username){
        User usr = null;

        // Check repository for user that matches the username
        for(User user : userRepository.findAll()){
            if(user.getUsername().equals(username)){
                usr = user;
                break;
            }
        }

        return usr;
    }
}
