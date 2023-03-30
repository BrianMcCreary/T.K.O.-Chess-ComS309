package TotalKnockoutChess.Users;

import TotalKnockoutChess.Friends.FriendRequest;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "friendship",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "friend_id")})
    @JsonManagedReference
    private List<User> friends = new ArrayList<User>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<FriendRequest> friendRequests = new ArrayList<FriendRequest>();

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

    public void addRequest(FriendRequest request) {
        friendRequests.add(request);
    }

    public void removeRequest(FriendRequest request) {
        friendRequests.remove(request);
    }

    public void addFriend(User u) {
        friends.add(u);
    }

    public void removeFriend(User u) {
        friends.remove(u);
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

