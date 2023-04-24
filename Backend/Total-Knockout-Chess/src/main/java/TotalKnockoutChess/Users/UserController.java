package TotalKnockoutChess.Users;

import TotalKnockoutChess.Statistics.UserStats;
import TotalKnockoutChess.Friends.FriendRequest;
import TotalKnockoutChess.Friends.FriendRequestRepository;
import TotalKnockoutChess.Friends.Friendship;
import TotalKnockoutChess.Friends.FriendshipRepository;
import TotalKnockoutChess.Statistics.UserStatsRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api(value = "UserController", description = "Controller used to manage User entities")
@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    FriendshipRepository friendshipRepository;

    @Autowired
    UserStatsRepository userStatsRepository;

    private final String success = "{\"message\":\"success\"}";     //Messages to return to frontend
    private final String failure = "{\"message\":\"failure\"}";
    private final String trueMessage = "{\"message\":\"true\"}";
    private final String falseMessage = "{\"message\":\"false\"}";

    //Method that returns a list of all users
    @ApiOperation(value = "Returns list of all Users")
    @GetMapping(path = "/users")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //Method that creates a new user given the username is > 0 characters, the password is >= 8 characters, and the username isn't already taken
    @ApiOperation(value = "Create a user with the given username and password")
    @PostMapping(path = "/users/{username}/{password}/{confirmPassword}")
    public String createUser(@PathVariable String username,@PathVariable String password, @PathVariable String confirmPassword) {
        if (password.length() < 8) {
            return failure; //Password too short
        }
        if (!password.equals(confirmPassword)) {
            return failure; //Passwords do not match
        }
        if (username.length() <= 0) {
            return failure;     //Username too short
        }
        for (User u : userRepository.findAll()) {
            if (u.getUsername().equals(username)) {
                return failure;     //Username taken
            }
        }
        User u = new User(username, password);
        userRepository.save(u);
        UserStats us = new UserStats(u);
        userStatsRepository.save(us);
        return success;
    }

    //Method that deletes a user and all friend requests or friendships associated with it
    @ApiOperation(value = "Delete the user with the given username")
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
    @ApiOperation(value = "Returns a User object by their username")
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
    @ApiOperation(value = "Returns a true or false message allowing the user to login")
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
    @ApiOperation(value = "Returns a user object given its ID")
    @GetMapping(path = "/users/{id}")
    public User getUserById(@PathVariable int id) {
        return userRepository.findById(id);
    }

    //Method that returns a user's username given their ID
    @ApiOperation(value = "Returns a username given the user's ID")
    @GetMapping(path = "/users/name/{id}")
    public String getUserName(@PathVariable int id) {
        return userRepository.findById(id).getUsername();
    }

    //Method that returns a user's password given their ID
    @ApiOperation(value = "Returns a password given the user's ID")
    @GetMapping(path = "/users/password/{id}")
    public String getUserPassword(@PathVariable int id) {
        return userRepository.findById(id).getPassword();
    }

    //Method that changes a user's username given their current username, new username, and password
    @ApiOperation(value = "Allows the user to change their username as long as they know their password")
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
    @ApiOperation(value = "Allows the user to change their password as long as they know their old password")
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
