package TotalKnockoutChess.Lobby;

import TotalKnockoutChess.Users.User;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
public class Lobby {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Long code;

    private int userCount;

    @OneToOne
    @JoinColumn(name = "owner")
    User owner;

    @OneToOne
    @JoinColumn(name = "player1")
    User player1;

    @OneToOne
    @JoinColumn(name = "player2")
    User player2;

    @OneToMany
    List<User> spectators;

    public Lobby(){
    }

    /**
     * Main constructor for lobby objects.
     * @param owner - User object who made the lobby
     */
    public Lobby(User owner) {
        spectators = new ArrayList<>();
        this.owner = owner;
        spectators.add(owner);
        userCount = 1;
    }

    // Generate code for the lobby
    public Long generateLobbyCode(List<Lobby> lobbies){
        Random rand = new Random(System.currentTimeMillis());
        Long lobbyCode = Math.abs(rand.nextLong() % 900000) + 100000; // Values from 100,000 to 999,999

        // Make sure lobby code is unique
        for(Lobby l : lobbies){
            while (l.getCode().equals(lobbyCode)){
                lobbyCode = Math.abs(rand.nextLong() % 900000) + 100000; // Values from 100,000 to 999,999
            }
        }
        return lobbyCode;
    }

    // Getter and Setter for lobby code
    public Long getCode(){
        return code;
    }
    public void setCode(Long code) { this.code = code; }

    // Getter and Setter for the id of the lobby
    public int getId() {
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    // Getter and Setter for the owner of the lobby
    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner){
        this.owner = owner;
    }

    // Getter and Setter for player1
    public User getPlayer1() {
        return player1;
    }
    public void setPlayer1(User player1){
        this.player1 = player1;
    }

    // Getter and Setter for player2
    public User getPlayer2() {
        return player2;
    }
    public void setPlayer2(User player2){
        this.player2 = player2;
    }

    // Getter for spectators
    public List<User> getSpectators(){
        return spectators;
    }

    // The first time a player joins the lobby, they are a spectator. They can switch to be a player with
    // the "switchToPlayer" method.
    public void addToSpectators(User user){
        spectators.add(user);
    }

    /**
     *  Method to swap from spectator to player
     *  @param user - User object to switch from spectator to player.
     *  @param playerIndex - Index of which player slot 'user' will switch to. Must be either 1 or 2.
      */
    public void switchToPlayer(User user, int playerIndex){
        spectators.remove(user);
        if(playerIndex == 1){
            player1 = user;
        }
        else if(playerIndex == 2){
            player2 = user;
        }
    }

    /**
     *  Method to swap from player to spectator
     *  @param user - User object to switch from player to spectator.
     */
    public void switchToSpectator(User user){
        // Will only switch if user is not already spectating.
        if(!spectators.contains(user) && this.contains(user)) {

            // Before using .equals, must make sure player1 and player2 objects aren't null
            if (player1 != null && player2 != null) {
                if (player1.equals(user)) {
                    player1 = null;
                } else if (player2.equals(user)) {
                    player2 = null;
                }
            } else if (player1 != null && player1.equals(user)) {
                player1 = null;
            } else if (player2 != null && player2.equals(user)) {
                player2 = null;
            }

            spectators.add(user);
        }
    }

    // Method to return whether a specific user is in the lobby
    public boolean contains(User user){

        // Check if user is spectating
        for(User spectator : spectators) {
            if (spectator.equals(user)) {
                return true;
            }
        }

        // Check if both player objects are null (needed to not throw errors when calling .equals)
        if(player1 == null && player2 == null){
            return false;
        }
        else if(player1 == null){
            if(player2.equals(user)){
                return true;
            }
        }
        else if(player1.equals(user)){
            return true;
        }
        else if(player2.equals(user)){
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return "LobbyID: " + id + "\nLobby Code: " + code;
    }

    // Methods to control increase and decrease of users in the lobby
    public void incrementUserCount(){
        userCount++;
    }
    public void decrementUserCount(){
        if(userCount > 0) {
            userCount--;
        }
    }
    // Method to return number of users in the lobby
    public int getUserCount(){
        return userCount;
    }

    // Method to remove someone from the spectators list
    public void removeSpectator(User spectator) {
        spectators.remove(spectator);
    }
}
