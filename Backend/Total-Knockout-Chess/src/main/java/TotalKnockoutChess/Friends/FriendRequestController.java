package TotalKnockoutChess.Friends;

import TotalKnockoutChess.Users.User;
import TotalKnockoutChess.Users.UserController;
import TotalKnockoutChess.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    FriendshipRepository friendshipRepository;

    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";
    private final String trueMessage = "{\"message\":\"true\"}";
    private final String falseMessage = "{\"message\":\"false\"}";

    @GetMapping(path = "/friendRequests/incoming/{username}")
    public List<User> getIncomingRequests(@PathVariable String username) {
        List<User> users = new ArrayList<User>();
        for (FriendRequest f : friendRequestRepository.findAll()) {
            if (f.getReceiver().getUsername().equals(username)) {
                users.add(f.getSender());
            }
        }
        return users;
    }

    @GetMapping(path = "/friendRequests/outgoing/{username}")
    public List<User> getOutgoingRequests(@PathVariable String username) {
        List<User> users = new ArrayList<User>();
        for (FriendRequest f : friendRequestRepository.findAll()) {
            if (f.getSender().getUsername().equals(username)) {
                users.add(f.getReceiver());
            }
        }
        return users;
    }

    @PostMapping(path = "/friendRequest/{sender}/{receiver}")
    public String sendFriendRequest(@PathVariable String sender, @PathVariable String receiver) {
        List<FriendRequest> friendRequests = friendRequestRepository.findAll();
        List<User> users = userRepository.findAll();
        User s = null;
        User r = null;
        //Check if the receiving user exists
        for (User u : users) {
            if (u.getUsername().equals(sender)) {
                s = u;
            }
            else if (u.getUsername().equals(receiver)) {
                r = u;
            }
        }
        if (r == null || s == null) {
            return failure;     //User not found
        }
        //Check if identical request already exists
        for (FriendRequest f : friendRequests) {
            if (f.getSender().getUsername().equals(sender) && f.getReceiver().getUsername().equals(receiver)) {
                return failure;     //You already send this user a friend request
            }
            if (f.getSender().getUsername().equals(receiver) && f.getReceiver().getUsername().equals(sender)) {
                return failure;     //This user has already sent you a request
            }
        }
        FriendRequest f = new FriendRequest(s, r);
        friendRequestRepository.save(f);
        s.addRequest(f);
        userRepository.flush();
        return success;     //Friend request sent
    }

    @PutMapping(path = "/friendRequest/{sender}/{receiver}")
    public String deleteFriendRequest(@PathVariable String sender, @PathVariable String receiver) {
        List<FriendRequest> friendRequests = friendRequestRepository.findAll();
        for (FriendRequest friendRequest : friendRequests) {
            if (friendRequest.getSender().getUsername().equals(sender) && friendRequest.getReceiver().getUsername().equals(receiver)) {
                User s = friendRequest.getSender();
                friendRequestRepository.delete(friendRequest);
                s.removeRequest(friendRequest);
                userRepository.flush();
                return success;     //Friend request deleted
            }
        }
        return failure;     //Friend request not found
    }

    @PutMapping(path = "/friendRequest/{sender}/{receiver}")
    public String acceptFriendRequest(@PathVariable String sender, @PathVariable String receiver) {
        List<FriendRequest> friendRequests = friendRequestRepository.findAll();
        for (FriendRequest friendRequest : friendRequests) {
            if (friendRequest.getSender().getUsername().equals(sender) && friendRequest.getReceiver().getUsername().equals(receiver)) {
                User s = friendRequest.getSender();
                User r = friendRequest.getSender();
                friendRequestRepository.delete(friendRequest);
                s.removeRequest(friendRequest);
                s.addFriend(r);
                r.addFriend(s);
                userRepository.flush();
                friendshipRepository.save(new Friendship(s, r));
                return success;     //Friendship saved successfully
            }
        }
        return failure;     //Couldn't find request
    }
}
