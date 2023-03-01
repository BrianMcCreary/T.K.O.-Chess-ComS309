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
    private String username;
    private String password;

//    private List<User> friends;

    /**
     * List of this users incoming friend requests. User can accept or deny these requests.
     */
//    private List<User> pendingFriends;


    /**
     * Constructor to initialize a new user with a specified name and password.
     * New users have an empty friends and pendingFriends lists.
     * @param username - desired username
     * @param password - desired password. Must be at least 8 characters long
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
//        friends = new ArrayList<User>();
//        pendingFriends = new ArrayList<User>();
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

    /**
     * Admin method to set list of friends to a specific list of users.
     * @param friends - list of friends to be set.
     */
//    public void setFriends(List<User> friends) {
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
//    public List<User> getPendingFriends() {
//        return pendingFriends;
//    }
//
//    public List<User> getFriends() {
//        return friends;
//    }
}
