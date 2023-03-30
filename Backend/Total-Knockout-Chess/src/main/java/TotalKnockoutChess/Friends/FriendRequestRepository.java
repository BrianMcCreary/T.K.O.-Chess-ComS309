package TotalKnockoutChess.Friends;

import TotalKnockoutChess.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    FriendRequest findById(int id);

    void deleteById(int id);
}
