package TotalKnockoutChess.Friends;

import org.springframework.data.jpa.repository.JpaRepository;
import javax.persistence.Table;

@Table
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    FriendRequest findById(int id);
    void deleteById(int id);
}
