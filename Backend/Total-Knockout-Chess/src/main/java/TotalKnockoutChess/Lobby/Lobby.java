package TotalKnockoutChess.Lobby;

import TotalKnockoutChess.Users.User;

import javax.annotation.Generated;
import javax.persistence.*;
import java.util.List;
import java.util.Random;

@Entity
public class Lobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long code;

    @OneToOne
    @JoinColumn(name = "ownerId")
    User owner;

    @OneToOne
    @JoinColumn(name = "player1Id")
    User player1;

    @OneToOne
    @JoinColumn(name = "player2Id")
    User player2;

    @OneToMany
    List<User> spectators;

    public Lobby(User owner) {
        owner.setLobby(this);
        this.owner = owner;
    }

    // Generate code for the lobby
    public Long generateLobbyCode(){
        Random rand = new Random(System.currentTimeMillis());
        return rand.nextLong(1000000);
    }

    public Long getCode(){
        return code;
    }

    // Getter and Setter for the id of the lobby
    public Long getId() {
        return id;
    }
    public void setId(Long id){
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
    public void setPlayer1(){
        this.player1 = player1;
    }

    // Getter and Setter for player2
    public User getPlayer2() {
        return player2;
    }
    public void setPlayer2(){
        this.player2 = player2;
    }

    // Getter for spectators
    public List<User> getSpectators(){
        return spectators;
    }
}
