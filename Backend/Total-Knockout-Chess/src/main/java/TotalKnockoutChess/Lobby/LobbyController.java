package TotalKnockoutChess.Lobby;

import TotalKnockoutChess.Users.User;
import TotalKnockoutChess.Users.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value = "LobbyController", description = "Controller used to manage Lobby entities")
@RestController
@RequestMapping("/lobby")
public class LobbyController {

    @Autowired
    LobbyRepository lobbyRepository;

    @Autowired
    UserRepository userRepository;

    private final String success = "success";
    private final String failure = "failure";

    @ApiOperation(value = "Returns whether any of the lobbies in the repository matches the given lobby code")
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
    @ApiOperation(value = "Deletes a lobby from the repository from the given lobby id")
    @DeleteMapping("/delete/{lobbyId}")
    public void deleteLobby(@PathVariable int lobbyId){
        Lobby l = lobbyRepository.findById(lobbyId);
        l.setOwner(null);
        lobbyRepository.delete(l);
    }

    // Mapping to get all current lobbies.
    @ApiOperation(value = "Returns a list of all lobby entities in the repository")
    @GetMapping("/all")
    public List<Lobby> getLobbies(){
        return lobbyRepository.findAll();
    }

    @ApiOperation(value = "Returns a list of strings representing all spectators in the lobby, specified by the lobby code")
    @GetMapping("/spectators/{lobbyCode}")
    public List<String> getLobbySpectators(@PathVariable Long lobbyCode){
        Lobby lobby = lobbyRepository.getByCode(lobbyCode);
        return lobby.getSpectators();
    }

    //Method used when a player joins a lobby, gives them all the information they need
    @ApiOperation(value = "Returns a list of strings containing the username and ready status of each user in the lobby, specified by the lobby code")
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
