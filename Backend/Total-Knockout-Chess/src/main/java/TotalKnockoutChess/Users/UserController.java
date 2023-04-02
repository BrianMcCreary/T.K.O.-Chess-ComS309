package TotalKnockoutChess.Users;

import TotalKnockoutChess.Friends.FriendRequest;
import TotalKnockoutChess.Friends.FriendRequestRepository;
import TotalKnockoutChess.Friends.Friendship;
import TotalKnockoutChess.Friends.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    FriendshipRepository friendshipRepository;

    private final String success = "{\"message\":\"success\"}";     //Messages to return to frontend
    private final String failure = "{\"message\":\"failure\"}";
    private final String trueMessage = "{\"message\":\"true\"}";
    private final String falseMessage = "{\"message\":\"false\"}";

    //Method that returns a list of all users
    @GetMapping(path = "/users")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //Method that creates a new user given the username is > 0 characters, the password is >= 8 characters, and the username isn't already taken
    @PostMapping(path = "/users")
    public String createUser(@RequestBody User user) {
        if (user == null || user.getUsername().length() <= 0) {
            return failure;     //Username too short
        }
        if (user.getPassword().length() < 8) {
            return failure;     //Password too short
        }
        for (User u : userRepository.findAll()) {
            if (u.getUsername().equals(user.getUsername())) {
                return failure;     //Username taken
            }
        }
        userRepository.save(user);
        return success;
    }

    //Method that deletes a user and all friend requests or friendships associated with it
    @PutMapping(path = "/users/{username}")
    public String deleteUser(@PathVariable String username) {
        for (FriendRequest fr : friendRequestRepository.findAll()) {        //Iterate through all friend requests and remove the one's associated with this user
            if (fr.getSender().getUsername().equals(username)) {
                fr.getReceiver().removeIncomingRequest(username);
                friendRequestRepository.delete(fr);
            }
            else if (fr.getReceiver().getUsername().equals(username)) {
                fr.getSender().removeOutgoingRequest(username);
                friendRequestRepository.delete(fr);
            }
        }
        for (Friendship f : friendshipRepository.findAll()) {           //Iterate through all friendships and remove the one's associated with this user
            if (f.getUser1().getUsername().equals(username)) {
                f.getUser2().removeFriend(username);
                friendshipRepository.delete(f);
            }
            else if (f.getUser2().getUsername().equals(username)) {
                f.getUser1().removeFriend(username);
                friendshipRepository.delete(f);
            }
        }
        for (User u : userRepository.findAll()) {           //Delete the user
            if (u.getUsername().equals(username)) {
                userRepository.delete(u);
                return success;
            }
        }
        return failure;
    }

    //Method that returns a user object given a username
    @GetMapping(path = "/users/getByName/{username}")
    public User getUserByName(@PathVariable String username) {
        List<User> userList = userRepository.findAll();
        for (User u : userList) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    //Method that allows the user to login
    @PostMapping(path = "/users/login")
    public @ResponseBody String login(@RequestBody User user) {
        for (User u : userRepository.findAll()) {
            if (u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())) {
                return trueMessage;
            }
        }
        return falseMessage;
    }

    //Method that returns a user object given their ID
    @GetMapping(path = "/users/{id}")
    public User getUserById(@PathVariable int id) {
        return userRepository.findById(id);
    }

    //Method that returns a user's username given their ID
    @GetMapping(path = "/users/name/{id}")
    public String getUserName(@PathVariable int id) {
        return userRepository.findById(id).getUsername();
    }

    //Method that returns a user's password given their ID
    @GetMapping(path = "/users/password/{id}")
    public String getUserPassword(@PathVariable int id) {
        return userRepository.findById(id).getPassword();
    }

    //Method that changes a user's username given their current username, new username, and password
    @PutMapping(path = "/users/name/{currentUsername}/{username}/{password}")
    public @ResponseBody String changeUserName(@PathVariable String currentUsername, @PathVariable String username, @PathVariable String password) {
        for (User u : userRepository.findAll()) {
            if (u.getUsername().equals(username)) {
                return failure;                         //username is taken
            }
        }
        for (User u : userRepository.findAll()) {
            if (u.getUsername().equals(currentUsername)) {      //find current user
                if (u.getPassword().equals(password)) {
                    u.setUsername(username);            //if given password matches, change username
                    userRepository.flush();
                    return success;
                }
                else {
                    return failure;     //if given password does not match, return failure
                }
            }
        }
        return failure;     //return failure if user isn't found
    }

    //Method that changes a user's password given their username, current password, and new password
    @PutMapping(path = "/users/password/{username}/{password}/{currentPassword}")
    public @ResponseBody String changeUserPassword(@PathVariable String username, @PathVariable String password, @PathVariable String currentPassword) {
        if (password.length() < 8) {
            return failure;     //if password is too short return failure
        }
        for (User u : userRepository.findAll()) {       //find user
            if (u.getUsername().equals(username)) {
                if (u.getPassword().equals(currentPassword)) {      //if they entered their old password correctly
                    u.setPassword(password);            //change password
                    userRepository.flush();
                    return success;
                }
                else {
                    return failure;     //if they entered their old password incorrectly, return failure
                }
            }
        }
        return failure;     //return failure if user isn't found
    }
}