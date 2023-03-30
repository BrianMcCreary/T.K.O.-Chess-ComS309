package TotalKnockoutChess.Friends;

import TotalKnockoutChess.Users.User;
import TotalKnockoutChess.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.List;

public class FriendshipController {

    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";

    @Autowired
    FriendshipRepository friendshipRepository;

    @Autowired
    UserRepository userRepository;

    @PutMapping(path = "/friends/{remover}/{removee}")
    public String removeFriend (@PathVariable String remover, @PathVariable String removee) {
        List<Friendship> friendships = friendshipRepository.findAll();
        for (Friendship friendship : friendships) {
            if ((friendship.getUser1().getUsername().equals(remover) && friendship.getUser2().getUsername().equals(removee)) ||
                    (friendship.getUser1().getUsername().equals(removee) && friendship.getUser2().getUsername().equals(remover))) {
                User u1 = friendship.getUser1();
                User u2 = friendship.getUser2();
                u1.removeFriend(u2);
                u2.removeFriend(u1);
                userRepository.flush();
                friendshipRepository.delete(friendship);
                return success;     //Friendship deleted successfully
            }
        }
        return failure;     //Friendship not found
    }
}
