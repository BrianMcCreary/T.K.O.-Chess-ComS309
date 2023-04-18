package TotalKnockoutChess.Users;

import TotalKnockoutChess.Lobby.Lobby;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;        //User username
    private String password;        //User password

    @ManyToOne
    private Lobby lobby;

    @ElementCollection
    private List<String> incomingFriendRequests;        //User's incoming friend requests
    @ElementCollection
    private List<String> outgoingFriendRequests;        //User's outgoing friend requests
    @ElementCollection
    private List<String> friends;       //User's friends

    /**
     * Constructor to initialize a new user with a specified name and password.
     * New users have an empty friends and pendingFriends lists.
     * @param username - desired username
     * @param password - desired password. Must be at least 8 characters long
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        incomingFriendRequests = new ArrayList<String>();
        outgoingFriendRequests = new ArrayList<String>();
        friends = new ArrayList<String>();
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Method to update this user's username.
     * @param username - String of requested username to update to. The argument 'name'
     *             must be available and different from the current username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Method to update this user's password.
     * @param password - String of requested password to update to. The argument 'password'
     *             must be at least 8 characters and be different from the current username.
     * @return String message indicating success or failure.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public void addIncomingRequest(String username) {
        incomingFriendRequests.add(username);
    }

    public void removeIncomingRequest(String username) {
        incomingFriendRequests.remove(username);
    }

    public List<String> getIncomingRequests() {
        return incomingFriendRequests;
    }

    public void addOutgoingRequest(String username) {
        outgoingFriendRequests.add(username);
    }

    public void removeOutgoingRequest(String username) {
        outgoingFriendRequests.remove(username);
    }

    public List<String> getOutgoingRequests() {
        return outgoingFriendRequests;
    }

    public void addFriend(String username) {
        friends.add(username);
    }

    public void removeFriend(String username) {
        friends.remove(username);
    }

    public List<String> getFriends() {
        return friends;
    }

    public String toString(){
        String str = "";
        str += "ID: " + id + "\n";
        str += "Username: " + username + "\n";
        str += "Password: " + password + "\n";
        if(incomingFriendRequests != null) {
            str += "IncomingFriendRequests: \n";
            for (String s : incomingFriendRequests) {
                str += "- " + s + "\n";
            }
        }
        if(outgoingFriendRequests != null) {
            str += "OutgoingFriendRequests: \n";
            for (String s : outgoingFriendRequests) {
                str += "- " + s + "\n";
            }
        }
        if(friends != null) {
            str += "Friends: \n";
            for (String s : friends) {
                str += "- " + s + "\n";
            }
        }
        return str;
    }
}

