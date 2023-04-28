package TotalKnockoutChess.Lobby;

import TotalKnockoutChess.Users.User;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "lobby")
public class Lobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Long code;

    private int userCount;

    @Column(columnDefinition = "TEXT")
    String owner;

    @Column(columnDefinition = "TEXT")
    String player1;

    private boolean player1Ready;

    @Column(columnDefinition = "TEXT")
    String player2;

    private boolean player2Ready;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> spectators;

    public Lobby(){
    }

    /**
     * Main constructor for lobby objects.
     * @param owner - User object who made the lobby
     */
    public Lobby(String owner) {
        spectators = new ArrayList<String>();
        spectators.add(owner);
        this.owner = owner;
        player1 = null;
        player2 = null;
        userCount = 1;
        player1Ready = false;
        player2Ready = false;
    }

    // Generate code for the lobby
    public Long generateLobbyCode(List<Lobby> lobbies){
        Random rand = new Random(System.currentTimeMillis());
        Long lobbyCode = Math.abs(rand.nextLong() % 900000) + 100000; // Values from 100,000 to 999,999
        boolean isUnique = false;
        boolean changed = false;

        // Make sure lobby code is unique
        while (!isUnique) {
            for (Lobby l : lobbies) {
                if (l.getCode().equals(lobbyCode)) {
                    lobbyCode = Math.abs(rand.nextLong() % 900000) + 100000;
                    changed = true;
                }
            }
            if (changed) {
                changed = false;
            }
            else {
                isUnique = true;
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
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner){
        this.owner = owner;
    }

    // Getter and Setter for player1
    public String getPlayer1() {
        return player1;
    }
    public void setPlayer1(String player1){
        this.player1 = player1;
    }

    // Getter and Setter for player2
    public String getPlayer2() {
        return player2;
    }
    public void setPlayer2(String player2){
        this.player2 = player2;
    }

    public boolean getPlayer1Ready() {
        return player1Ready;
    }

    public void setPlayer1Ready(boolean player1Ready) {
        this.player1Ready = player1Ready;
    }

    public boolean getPlayer2Ready() {
        return player2Ready;
    }

    public void setPlayer2Ready(boolean player2Ready) {
        this.player2Ready = player2Ready;
    }

    // Getter for spectators
    public List<String> getSpectators(){
        return spectators;
    }

    // The first time a player joins the lobby, they are a spectator. They can switch to be a player with
    // the "switchToPlayer" method.
    public void addToSpectators(String user){
        spectators.add(user);
    }

    /**
     *  Method to swap from spectator to player
     *  @param user - User object to switch from spectator to player.
     *  @param playerIndex - Index of which player slot 'user' will switch to. Must be either 1 or 2.
      */
    public void switchToPlayer(String user, int playerIndex){
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
    public void switchToSpectator(String user){
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
    public boolean contains(String user){

        // Check if user is spectating
        if (spectators.contains(user)) {
                return true;
        }

        // Check if both player objects are null (needed to not throw errors when calling .equals)
        if (player1 != null) {
            if (player1.equals(user)) {
                return true;
            }
        }
        if (player2 != null) {
            if (player2.equals(user)) {
                return true;
            }
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
    public void removeSpectator(String spectator) {
        spectators.remove(spectator);
    }
}
