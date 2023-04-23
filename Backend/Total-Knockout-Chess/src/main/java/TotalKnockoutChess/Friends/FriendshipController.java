package TotalKnockoutChess.Friends;

import TotalKnockoutChess.Users.User;
import TotalKnockoutChess.Users.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "FriendshipController", description = "Controller used to manage Friendship entities")
@RestController
public class FriendshipController {

    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";

    @Autowired
    FriendshipRepository friendshipRepository;

    @Autowired
    UserRepository userRepository;

    //Method that removes a friendship given the users' usernames
    @ApiOperation(value = "Deletes a friendship given the the users' usernames")
    @PutMapping(path = "/friends/{remover}/{removee}")
    public String removeFriend (@PathVariable String remover, @PathVariable String removee) {
        List<Friendship> friendships = friendshipRepository.findAll();
        //Iterate through friendships until correct one is found and update lists/repositories accordingly
        for (Friendship friendship : friendships) {
            if ((friendship.getUser1().getUsername().equals(remover) && friendship.getUser2().getUsername().equals(removee)) ||
                    (friendship.getUser1().getUsername().equals(removee) && friendship.getUser2().getUsername().equals(remover))) {
                User u1 = friendship.getUser1();
                User u2 = friendship.getUser2();
                u1.removeFriend(u2.getUsername());
                u2.removeFriend(u1.getUsername());
                userRepository.flush();
                friendshipRepository.delete(friendship);
                return success;     //Friendship deleted successfully
            }
        }
        return failure;     //Friendship not found
    }

    //Method that returns a list of usernames of a user's friends given their username
    @ApiOperation(value = "Returns a list of a user's friends given their username")
    @GetMapping(path = "/friends/{username}")
    public List<String> getFriends(@PathVariable String username) {
        for (User u : userRepository.findAll()) {
            if (u.getUsername().equals(username)) {
                return u.getFriends();
            }
        }
        return null; //you weren't found?? lol
    }
}
