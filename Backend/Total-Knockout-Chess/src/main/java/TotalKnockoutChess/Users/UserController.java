package TotalKnockoutChess.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";
    private final String trueMessage = "{\"message\":\"true\"}";
    private final String falseMessage = "{\"message\":\"false\"}";

    @GetMapping(path = "/users")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping(path = "/users")
    public String createUser(@RequestBody User user) {
        if (user == null || user.getUsername().length() <= 0) {
            return failure;
        }
        if (user.getPassword().length() < 8) {
            return failure;
        }
        for (User u : userRepository.findAll()) {
            if (u.getUsername().equals(user.getUsername())) {
                    return failure;
            }
        }
        userRepository.save(user);
        return success;
    }

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

    @PostMapping(path = "/users/login")
    public @ResponseBody String login(@RequestBody User user) {
        for (User u : userRepository.findAll()) {
            if (u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())) {
                return trueMessage;
            }
        }
        return falseMessage;
    }

    @GetMapping(path = "/users/{id}")
    public User getUserById(@PathVariable int id) {
        return userRepository.findById(id);
    }

    @GetMapping(path = "/users/name/{id}")
    public String getUserName(@PathVariable int id) {
        return userRepository.findById(id).getUsername();
    }

    @GetMapping(path = "/users/password/{id}")
    public String getUserPassword(@PathVariable int id) {
        return userRepository.findById(id).getPassword();
    }

    @PutMapping(path = "/users/name/{id}")
    public @ResponseBody String changeUserName(@PathVariable int id, @RequestParam (value="username", required=true) String username) {
        for (User u : userRepository.findAll()) {
            if (u.getUsername().equals(username)) {
                return "Username is already: " + username + ". Please specify a different name to update.";
            }
        }
        userRepository.findById(id).setUsername(username);
        userRepository.flush();
        return "Username updated to: " + userRepository.findById(id).getUsername() + ".";
    }

    @PutMapping(path = "/users/password/{id}")
    public @ResponseBody String changeUserPassword(@PathVariable int id, @RequestParam String password) {
        if (userRepository.findById(id).getPassword().equals(password)) {
            return "New password matches current password. Please specify a different password to update.";
        }
        else if(password.length() < 8){
            return "Password must be at least 8 characters.";
        }
        userRepository.findById(id).setPassword(password);
        userRepository.flush();
        return "Password updated.";
    }

//    @PutMapping(path = "/sendFriendRequest/{fromId}/{toId}")
//    public void sendFriendRequest(@PathVariable int fromId, @PathVariable int toId) {
//        User sender = userRepository.findById(fromId);
//        User recipient = userRepository.findById(toId);
//        sender.sendFriendRequest(recipient);
//    }
//
//    @GetMapping(path = "/pendingFriendRequests/{id}")
//    public List<User> getPendingFriends(@PathVariable int id) {
//        return userRepository.findById(id).getPendingFriends();
//    }
//
//    @GetMapping(path = "/friends/{id}")
//    public List<User> getFriends(@PathVariable int id) {
//        return userRepository.findById(id).getFriends();
//    }
//
//    @PutMapping(path = "/acceptFriendRequest/{senderId}/{acceptorId}")
//    public void acceptFriendRequest(@PathVariable int senderId, @PathVariable int acceptorId) {
//        User sender = userRepository.findById(senderId);
//        User acceptor = userRepository.findById(acceptorId);
//        acceptor.acceptFriendRequest(sender);
//    }
//
//    @PutMapping(path = "/removeFriend/{removerId}/{removeeId}")
//    public void removeFriend(@PathVariable int removerId, @PathVariable int removeeId) {
//        User remover = userRepository.findById(removerId);
//        User removee = userRepository.findById(removeeId);
//        remover.removeFriend(removee);
//    }
//
//    @PutMapping(path = "/rejectFriendRequest/{rejectorId}/{rejecteeId}")
//    public void rejectFriendRequest(@PathVariable int rejectorId, @PathVariable int rejecteeId) {
//        User rejector = userRepository.findById(rejectorId);
//        User rejectee = userRepository.findById(rejecteeId);
//        rejector.rejectFriendRequest(rejectee);
//    }
}
