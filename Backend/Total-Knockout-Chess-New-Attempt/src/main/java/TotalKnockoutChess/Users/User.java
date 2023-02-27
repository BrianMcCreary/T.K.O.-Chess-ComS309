package TotalKnockoutChess.Users;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String password;

//    private List<User> friends;

    /**
     * List of this users incoming friend requests. User can accept or deny these requests.
     */
//    private List<User> pendingFriends;

    /**
     * Constructor to initialize a new user with a specified name and password.
     * New users have an empty friends and pendingFriends lists.
     * @param name - desired username
     * @param password - desired password. Must be at least 8 characters long
     */
    public User(String name, String password) {
        this.name = name;
        this.password = password;
//        friends = new ArrayList<User>();
//        pendingFriends = new ArrayList<User>();
    }

    public User() {
        name = "Jimbo";
        password = "password7676";
    }

    protected int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    protected String getName() {
        return name;
    }

    /**
     * Method to update this user's username.
     * @param name - String of requested username to update to. The argument 'name'
     *             must be available and different from the current username.
     * @return String message indicating success or failure.
     */
    protected String setName(String name) {
        if(this.name.equals(name)){
            return "Username is already: " + name + ". Please specify a different name to update.";
        }
        this.name = name;
        return "Username updated to: " + name + ".";
    }

    protected String getPassword() {
        return password;
    }

    /**
     * Method to update this user's password.
     * @param password - String of requested password to update to. The argument 'password'
     *             must be at least 8 characters and be different from the current username.
     * @return String message indicating success or failure.
     */
    protected String setPassword(String password) {
        if(this.password.equals(password)){
            return "New password matches current password. Please specify a different password to update.";
        }
        else if(password.length() < 8){
            return "Password must be at least 8 characters.";
        }
        this.password = password;
        return "Password updated.";
    }

    /**
     * Admin method to set list of friends to a specific list of users.
     * @param friends - list of friends to be set.
     */
//    protected void setFriends(List<User> friends) {
//        this.friends = friends;
//        for (User f : friends) {
//            f.friends.add(this);
//        }
//    }
//
//    protected void sendFriendRequest(User friend) {
//        friend.pendingFriends.add(this);
//    }
//
//    protected void acceptFriendRequest(User friend) {
//        if (pendingFriends.contains(friend)) {
//            friends.add(friend);
//            pendingFriends.remove(friend);
//            friend.friends.add(this);
//        }
//    }
//
//    protected void rejectFriendRequest(User friend){
//        if (pendingFriends.contains(friend)) {
//            pendingFriends.remove(friend);
//        }
//    }
//
//    protected void removeFriend(User friend) {
//        if (friends.contains(friend)) {
//            friends.remove(friend);
//            friend.friends.remove(this);
//        }
//    }
//
//    protected List<User> getPendingFriends() {
//        return pendingFriends;
//    }
//
//    protected List<User> getFriends() {
//        return friends;
//    }
}
