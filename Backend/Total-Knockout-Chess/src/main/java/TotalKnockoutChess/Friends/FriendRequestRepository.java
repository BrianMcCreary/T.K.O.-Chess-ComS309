package TotalKnockoutChess.Friends;

import TotalKnockoutChess.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Table
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    FriendRequest findById(int id);

    void deleteById(int id);
}
