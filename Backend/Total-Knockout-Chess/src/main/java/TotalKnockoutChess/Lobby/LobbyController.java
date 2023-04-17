package TotalKnockoutChess.Lobby;

import TotalKnockoutChess.Users.User;
import TotalKnockoutChess.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lobby")
public class LobbyController {

    @Autowired
    LobbyRepository lobbyRepository;

    @Autowired
    UserRepository userRepository;

    // Messages to send to the frontend
    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";
//    private final String success = "success";
//    private final String failure = "failure";

    @GetMapping("/find/{lobbyCode}")
    public String findLobby(@PathVariable Long lobbyCode) {
        for (Lobby l : lobbyRepository.findAll()) {
            if (l.getCode().equals(lobbyCode)) {
                return success;
            }
        }
        return "{\"message\":\"Lobby not found\"}";
    }

    // Mapping to create a new lobby with the given User object as its owner.
//    @PostMapping("/host/{creator}")
//    public String hostLobby(@PathVariable String creator){
//        User owner = getUser(creator);
//
//        // If user doesn't exist, return as a failure
//        if(owner == null){
//            System.out.println("User not found");
//            return failure;
//        }
//        // If user is already in a lobby, delete old lobby
//        for(Lobby l : lobbyRepository.findAll()){
//            if(l.contains(owner)){
//                System.out.println("User is in another lobby");
//                deleteLobby(l.getId());
//            }
//        }
//
//        // Create new lobby, generate and set code.
//        Lobby lobby = new Lobby(owner);
//        Long lobbyCode = lobby.generateLobbyCode(lobbyRepository.findAll());
//        lobby.setCode(lobbyCode);
//
//        lobbyRepository.save(lobby);
//
//        return lobbyCode.toString(); // Return lobbyCode as a string
//    }

    // Mapping for people other than the owner to join the lobby.
//    @PutMapping("/join/{lobbyCode}/{joiner}")
//    public String joinLobby(@PathVariable Long lobbyCode, @PathVariable String joiner){
//        Lobby lobby = lobbyRepository.getByCode(lobbyCode);
//        User usr = getUser(joiner);
//        System.out.println("Entered joinLobby method.");
//
//        // Method will return failure if any of the following:
//        // 1 - user is not found in database
//        // 2 - lobby is not found in database
//        // 3 - user is already in a lobby in the database
//        if(usr == null || lobby == null || lobby.contains(usr)){
//            System.out.println("Going to return failure.");
//            return failure;
//        }
//
//        // When you first join a lobby you start as a spectator.
//        lobby.addToSpectators(usr);
//        lobby.incrementUserCount();
//        lobbyRepository.flush();
//        System.out.println("Going to return success.");
//
//        return success;
//    }

    // Mapping for users to swap from playing in the lobby to spectating.
//    @PutMapping("/spectate/{lobbyCode}/{username}")
//    public String spectate(@PathVariable Long lobbyCode, @PathVariable String username){
//        User usr = null;
//        Lobby lobby = lobbyRepository.getByCode(lobbyCode);
//        String lobbyPlayer1Username = lobby.getPlayer1().getUsername();
//        String lobbyPlayer2Username = lobby.getPlayer2().getUsername();
//
//        // If both player slots in the lobby do not match the username, method will fail.
//        if(!lobbyPlayer1Username.equals(username) && !lobbyPlayer2Username.equals(username)){
//            return failure;
//        }
//        // If player slot 1 matches username
//        else if(lobbyPlayer1Username.equals(username)){
//            usr = lobby.getPlayer1();
//        }
//        // If player slot 2 matches username
//        else{
//            usr = lobby.getPlayer2();
//        }
//
//        lobby.switchToSpectator(usr);
//        lobbyRepository.flush();
//        return success;
//    }

    // Mapping for users to swap from spectating in the lobby to playing. This will fail if both player slots are full.
//    @PutMapping("/play/{lobbyCode}/{username}")
//    public String play(@PathVariable Long lobbyCode, @PathVariable String username){
//        User usr = getUser(username);
//
//        // User was not found
//        if(usr == null){ return failure; }
//
//        lobbyRepository.flush();
//        Lobby lobby = lobbyRepository.getByCode(lobbyCode);
//        boolean isSpectating = false;
//
//        // If user is not spectating, method will fail.
//        for(User spectator : lobby.getSpectators()) {
//            if (spectator.equals(usr)) {
//                isSpectating = true;
//                break;
//            }
//        }
//        if(!isSpectating) {
//            return failure;
//        }
//        // If both player slots are full, method will fail.
//        else if(lobby.getPlayer1() != null && lobby.getPlayer2() != null){
//            return failure;
//        }
//        // If player slot 1 is open, user will fill that slot.
//        else if(lobby.getPlayer1() == null){
//            lobby.switchToPlayer(usr, 1);
//            lobbyRepository.flush();
//            return success;
//
//        }
//        // If player slot 2 is open and user isn't already in player slot 1, user will fill slot 2.
//        else if(lobby.getPlayer2() == null && !lobby.getPlayer1().equals(usr)){
//            lobby.switchToPlayer(usr, 2);
//            lobbyRepository.flush();
//            return success;
//
//        }
//        lobbyRepository.flush();
//        return failure;
//    }

    // Mapping to leave a lobby.
//    @PutMapping("/leave/{lobbyCode}/{username}")
//    public String leaveLobby(@PathVariable Long lobbyCode, @PathVariable String username){
//        User usr = getUser(username);
//        if(usr == null){ return failure; }
//        Lobby lobby = lobbyRepository.getByCode(lobbyCode);
//
//        // Phantom lobby
//        if(lobby.getUserCount() < 1){
//            deleteLobby(lobby.getId());
//            return failure;
//        }
//
//        // Last user in the lobby will delete the lobby
//        if(lobby.getUserCount() == 1 && lobby.contains(usr)){
//            deleteLobby(lobby.getId());
//            return success;
//        }
//
//        if(lobby.getPlayer1() != null && usr.equals(lobby.getPlayer1())){
//            lobby.setPlayer1(lobby.getPlayer2()); // Shift player 2 slot to player 1
//            lobby.setPlayer2(null);               // Fill player 2 slot with null object.
//            lobby.decrementUserCount();
//        }
//        else if(lobby.getPlayer2() != null && usr.equals(lobby.getPlayer2())){
//            lobby.setPlayer2(null);               // Fill player 2 slot with null object.
//            lobby.decrementUserCount();
//        }
//        else{
//            for(User spectator: lobby.getSpectators()){
//                if(spectator.equals(usr)){
//                    lobby.removeSpectator(spectator);
//                    lobby.decrementUserCount();
//                    break;
//                }
//            }
//        }
//        lobbyRepository.flush();
//        return success;
//    }

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
