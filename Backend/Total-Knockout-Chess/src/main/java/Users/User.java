package Users;

import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String password;

    @OneToMany
    private ArrayList<User> friends;

    @OneToMany
    private ArrayList<User> pendingFriends;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        friends = new ArrayList<User>();
    }

    protected int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    public void sendFriendRequest(User friend) {
        friend.pendingFriends.add(this);
    }

    public void acceptFriendRequest(User friend) {
        if (pendingFriends.contains(friend)) {
            friends.add(friend);
            pendingFriends.remove(friend);
            friend.friends.add(this);
        }
    }

    public String removeFriend(User friend) {
        if (friends.contains(friend)) {
            friends.remove(friend);
            friend.friends.remove(this);
            return "Friend removed";
        }
        return "Friend not found";
    }
}
