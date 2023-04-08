package TotalKnockoutChess.Friends;

import TotalKnockoutChess.Users.User;
import TotalKnockoutChess.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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

    //Method that returns a user's incoming friend requests given their username
    @GetMapping(path = "/friendRequests/incoming/{username}")
    public List<String> getIncomingRequests(@PathVariable String username) {
        for (User u : userRepository.findAll()) {
            if (u.getUsername().equals(username)) {
                return u.getIncomingRequests();
            }
        }
        return null;
    }

    //Method that returns a user's outgoing friend requests given their username
    @GetMapping(path = "/friendRequests/outgoing/{username}")
    public List<String> getOutgoingRequests(@PathVariable String username) {
        for (User u : userRepository.findAll()) {
            if (u.getUsername().equals(username)) {
                return u.getOutgoingRequests();
            }
        }
        return null;
    }

    //Method that sends a friend request given the sender's username, and the receiver's username
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
                return failure;     //You already sent this user a friend request
                //return "over here";
            }
            if (f.getSender().getUsername().equals(receiver) && f.getReceiver().getUsername().equals(sender)) {
                return failure;     //This user has already sent you a request
            }
        }
        //Check if already friends
        for (Friendship friendship : friendshipRepository.findAll()) {
            if ((friendship.getUser1().getUsername().equals(sender) && friendship.getUser2().getUsername().equals(receiver))  ||
                    (friendship.getUser1().getUsername().equals(receiver) && friendship.getUser2().getUsername().equals(sender))) {
                return failure; //You are already friends with this user
            }
        }
        FriendRequest f = new FriendRequest(s, r);
        friendRequestRepository.save(f);
        s.addOutgoingRequest(r.getUsername());
        r.addIncomingRequest(s.getUsername());
        userRepository.flush();
        return success;     //Friend request sent
    }

    //Method that deletes a friend request given the sender and receiver's usernames
    @PutMapping(path = "/deleteFriendRequest/{sender}/{receiver}")
    public String deleteFriendRequest(@PathVariable String sender, @PathVariable String receiver) {
        List<FriendRequest> friendRequests = friendRequestRepository.findAll();
        //Iterate through friend requests until correct one is found and update lists/tables accordingly
        for (FriendRequest friendRequest : friendRequests) {
            if (friendRequest.getSender().getUsername().equals(sender) && friendRequest.getReceiver().getUsername().equals(receiver)) {
                User s = friendRequest.getSender();
                User r = friendRequest.getReceiver();
                friendRequestRepository.delete(friendRequest);
                s.removeOutgoingRequest(r.getUsername());
                r.removeIncomingRequest(s.getUsername());
                userRepository.flush();
                return success;     //Friend request deleted
            }
        }
        return failure;     //Friend request not found
    }

    //Method that accepts a friend request given the sender and receiver's usernames
    @PostMapping(path = "/acceptFriendRequest/{sender}/{receiver}")
    public String acceptFriendRequest(@PathVariable String sender, @PathVariable String receiver) {
        List<FriendRequest> friendRequests = friendRequestRepository.findAll();
        //Iterate through all friend requests until correct one is found and update lists/repositories accordingly
        for (FriendRequest friendRequest : friendRequests) {
            if (friendRequest.getSender().getUsername().equals(sender) && friendRequest.getReceiver().getUsername().equals(receiver)) {
                User s = friendRequest.getSender();
                User r = friendRequest.getReceiver();
                friendRequestRepository.delete(friendRequest);
                s.removeOutgoingRequest(r.getUsername());
                r.removeIncomingRequest(s.getUsername());
                s.addFriend(r.getUsername());
                r.addFriend(s.getUsername());
                userRepository.flush();
                friendshipRepository.save(new Friendship(s, r));
                return success;     //Friendship saved successfully
            }
        }
        return failure;     //Couldn't find request
    }
}
